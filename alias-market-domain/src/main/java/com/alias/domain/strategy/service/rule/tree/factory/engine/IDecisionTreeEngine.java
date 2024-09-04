package com.alias.domain.strategy.service.rule.tree.factory.engine;

import com.alias.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Long awardId);

}
