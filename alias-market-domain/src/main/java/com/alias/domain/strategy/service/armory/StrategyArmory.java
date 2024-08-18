package com.alias.domain.strategy.service.armory;

import com.alias.domain.strategy.model.entity.StrategyAwardEntity;
import com.alias.domain.strategy.repository.IstrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory {

    @Resource
    private IstrategyRepository repository;

    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream().map(StrategyAwardEntity::getAwardRate).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        // 获取概率总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream().map(StrategyAwardEntity::getAwardRate).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        //
        List<Long> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Long awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        // 转换成 HashMap
        HashMap<Integer, Long> shuffledStrategyAwardSearchRateTables = new HashMap<>(strategyAwardSearchRateTables.size());
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffledStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 存储到 Redis
        repository.storeStrategyAwardSearchRateTables(strategyId, shuffledStrategyAwardSearchRateTables.size(), shuffledStrategyAwardSearchRateTables);

    }

    @Override
    public Long getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(strategyId);
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }
}
