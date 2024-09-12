package com.alias.domain.strategy.service.armory;

public interface IStrategyDispatch {

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    Long getRandomAwardId(Long strategyId);

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @param ruleWeightValue 权重规则值
     * @return 奖品ID
     */
    Long getRandomAwardId(Long strategyId, String ruleWeightValue);

    /**
     * 根据策略ID和奖品ID，扣减奖品缓存库存
     *
     * @param strategyId 策略ID
     * @param awardId 奖品ID
     * @return 是否扣减成功
     */
    Boolean subtractionAwardStock(Long strategyId, Long awardId);
}
