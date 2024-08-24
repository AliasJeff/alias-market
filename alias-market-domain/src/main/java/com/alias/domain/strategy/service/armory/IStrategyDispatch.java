package com.alias.domain.strategy.service.armory;

public interface IStrategyDispatch {

    Long getRandomAwardId(Long strategyId);

    Long getRandomAwardId(Long strategyId, String ruleWeightValue);
}
