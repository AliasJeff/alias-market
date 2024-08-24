package com.alias.domain.strategy.service.armory;

import com.alias.domain.strategy.model.entity.StrategyAwardEntity;
import com.alias.domain.strategy.model.entity.StrategyEntity;
import com.alias.domain.strategy.model.entity.StrategyRuleEntity;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.types.common.Constants;
import com.alias.types.enums.ResponseCode;
import com.alias.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        // 权重策略配置 - 适用于 rule_weight 权重规则配置
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) {
            return true;
        }

        // 权重策略配置 - 适用于 rule_weight 权重规则配置【4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109】
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }

        Map<String, List<Long>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        for (String key : ruleWeightValueMap.keySet()) {
            List<Long> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(key), strategyAwardEntitiesClone);
        }

        return true;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream().map(StrategyAwardEntity::getAwardRate).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        // 获取概率总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream().map(StrategyAwardEntity::getAwardRate).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        // 生成策略奖品概率查找表（指在list集合中，存放上对应的奖品占位，占位越多概率越高）
        List<Long> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Long awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            // FIXME 循环次数过多，需要优化
            for (int i = 0; i < rateRange.multiply(awardRate).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        // 转换成 HashMap
        HashMap<Integer, Long> shuffledStrategyAwardSearchRateTables = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffledStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 存储到 Redis
        repository.storeStrategyAwardSearchRateTables(key, shuffledStrategyAwardSearchRateTables.size(), shuffledStrategyAwardSearchRateTables);

    }

    @Override
    public Long getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(strategyId);
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Long getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeightValue);
        int rateRange = repository.getRateRange(key);
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
