package com.alias.domain.strategy.service.rule.chain.impl;

import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.alias.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.alias.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] split = ruleValue.split(Constants.COLON);
        Long awardId = Long.parseLong(split[0]);

        String[] userBlackIds = split[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("抽奖责任链-黑名单接管，userId: {}，strategyId：{}，ruleModel：{}， awardId: {}", userId, strategyId, ruleModel(), awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .build();
            }
        }
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
    }
}
