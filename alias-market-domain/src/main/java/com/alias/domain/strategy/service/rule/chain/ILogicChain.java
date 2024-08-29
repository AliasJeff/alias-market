package com.alias.domain.strategy.service.rule.chain;

public interface ILogicChain extends ILogicChainArmory {

    /**
     * 责任链接口
     * @param userId 用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    Long logic(String userId, Long strategyId);


}
