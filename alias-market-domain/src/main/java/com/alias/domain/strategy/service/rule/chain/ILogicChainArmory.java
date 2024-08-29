package com.alias.domain.strategy.service.rule.chain;

public interface ILogicChainArmory {

    ILogicChain appendNext(ILogicChain next);

    ILogicChain next();
}
