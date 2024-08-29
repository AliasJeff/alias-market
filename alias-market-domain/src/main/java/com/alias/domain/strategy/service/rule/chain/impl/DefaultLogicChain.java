package com.alias.domain.strategy.service.rule.chain.impl;

import com.alias.domain.strategy.service.armory.IStrategyDispatch;
import com.alias.domain.strategy.service.rule.chain.AbstractLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Override
    public Long logic(String userId, Long strategyId) {
        Long awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理. 用户ID：{}，策略ID：{}，规则模型：{}，奖品ID：{}", userId, strategyId, ruleModel(), awardId);
        return awardId;
    }

    @Override
    protected String ruleModel() {
        return "default";
    }
}
