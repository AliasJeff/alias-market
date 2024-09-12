package com.alias.domain.strategy.repository;

import com.alias.domain.strategy.model.entity.StrategyAwardEntity;
import com.alias.domain.strategy.model.entity.StrategyEntity;
import com.alias.domain.strategy.model.entity.StrategyRuleEntity;
import com.alias.domain.strategy.model.vo.RuleTreeVO;
import com.alias.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import com.alias.domain.strategy.model.vo.StrategyAwardStockKeyVO;

import java.util.HashMap;
import java.util.List;

public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTables(String key, int size, HashMap<Integer, Long> strategyAwardSearchRateTables);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Long getStrategyAwardAssemble(String key, int rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Long strategyId, Long awardId);

    /**
     * 根据规则树ID查询树结构
     *
     * @param treeId 规则树ID
     * @return 规则树结构
     */
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    /**
     * 缓存奖品库存
     *
     * @param cacheKey 缓存key
     * @param awardCount 奖品库存
     */
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    /**
     * 缓存 key，decr 方式扣减奖品库存
     * @param cacheKey 缓存key
     * @return 是否扣减成功
     */
    Boolean subtractionAwardStock(String cacheKey);

    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    StrategyAwardStockKeyVO takeQueueValue();

    void updateStrategyAwardStock(Long strategyId, Long awardId);
}
