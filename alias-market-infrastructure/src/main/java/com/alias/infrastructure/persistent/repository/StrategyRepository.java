package com.alias.infrastructure.persistent.repository;

import com.alias.domain.strategy.model.entity.StrategyAwardEntity;
import com.alias.domain.strategy.model.entity.StrategyEntity;
import com.alias.domain.strategy.model.entity.StrategyRuleEntity;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.infrastructure.persistent.dao.IStrategyAwardDao;
import com.alias.infrastructure.persistent.dao.IStrategyDao;
import com.alias.infrastructure.persistent.dao.IStrategyRuleDao;
import com.alias.infrastructure.persistent.po.Strategy;
import com.alias.infrastructure.persistent.po.StrategyAward;
import com.alias.infrastructure.persistent.po.StrategyRule;
import com.alias.infrastructure.persistent.redis.IRedisService;
import com.alias.types.common.Constants;
import com.alias.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alias.types.enums.ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY;

@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }

        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());

        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            strategyAwardEntity.setStrategyId(strategyAward.getStrategyId());
            strategyAwardEntity.setAwardId(strategyAward.getAwardId());
            strategyAwardEntity.setAwardCount(strategyAward.getAwardCount());
            strategyAwardEntity.setAwardCountSurplus(strategyAward.getAwardCountSurplus());
            strategyAwardEntity.setAwardRate(strategyAward.getAwardRate());
            strategyAwardEntities.add(strategyAwardEntity);
        }

        redisService.setValue(cacheKey, strategyAwardEntities);

        return strategyAwardEntities;

    }

    @Override
    public void storeStrategyAwardSearchRateTables(String key, int size, HashMap<Integer, Long> strategyAwardSearchRateTables) {
        // 存储抽奖策略范围值
        log.info(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, size);

        // 存储概率查找表
        Map<Integer, Long> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(strategyAwardSearchRateTables);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
        String cacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key;
        if (!redisService.isExists(cacheKey)) {
            throw new AppException(UN_ASSEMBLED_STRATEGY_ARMORY.getCode(), cacheKey + Constants.COLON + UN_ASSEMBLED_STRATEGY_ARMORY.getInfo());
        }
        return redisService.getValue(cacheKey);
    }

    @Override
    public Long getStrategyAwardAssemble(String key, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (null != strategyEntity) {
            return strategyEntity;
        }
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        strategyEntity = StrategyEntity.builder().strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule strategyRuleRes = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        return StrategyRuleEntity.builder().strategyId(strategyRuleRes.getStrategyId())
                .awardId(strategyRuleRes.getAwardId())
                .ruleType(strategyRuleRes.getRuleType())
                .ruleModel(strategyRuleRes.getRuleModel())
                .ruleValue(strategyRuleRes.getRuleValue())
                .ruleDesc(strategyRuleRes.getRuleDesc())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        strategyRule.setRuleModel(ruleModel);
        return strategyRuleDao.queryStrategyRuleValue(strategyRule);
    }
}
