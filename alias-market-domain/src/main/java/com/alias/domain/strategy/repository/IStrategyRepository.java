package com.alias.domain.strategy.repository;

import com.alias.domain.strategy.model.entity.StrategyAwardEntity;
import com.alias.domain.strategy.model.entity.StrategyEntity;
import com.alias.domain.strategy.model.entity.StrategyRuleEntity;
import com.alias.domain.strategy.model.vo.RuleTreeVO;
import com.alias.domain.strategy.model.vo.StrategyAwardRuleModelVO;

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

    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);
}
