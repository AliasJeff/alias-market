package com.alias.domain.strategy.repository;

import com.alias.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface IstrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTables(Long strategyId, int size, HashMap<Integer, Long> strategyAwardSearchRateTables);

    int getRateRange(Long strategyId);

    Long getStrategyAwardAssemble(Long strategyId, int rateKey);
}
