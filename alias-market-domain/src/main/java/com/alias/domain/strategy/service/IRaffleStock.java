package com.alias.domain.strategy.service;

import com.alias.domain.strategy.model.vo.StrategyAwardStockKeyVO;

/**
 * 抽奖库存相关服务，获取库存消耗队列
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存 key 信息
     * @throws InterruptedException 异常
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新策略奖品库存小号记录
     *
     * @param strategyId 策略 ID
     * @param awardId 奖品 ID
     */
    void updateStrategyAwardStock(Long strategyId, Long awardId);
}
