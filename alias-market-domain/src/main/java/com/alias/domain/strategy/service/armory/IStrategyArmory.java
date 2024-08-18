package com.alias.domain.strategy.service.armory;

public interface IStrategyArmory {

    void assembleLotteryStrategy(Long strategyId);

    Long getRandomAwardId(Long strategyId);
}
