package com.alias.domain.strategy.service.rule.tree.impl;

import com.alias.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.alias.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.alias.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.alias.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId, String ruleValue) {
        log.info("规则过滤 - 兜底奖品, userId: {}, strategyId: {}, awardId: {}, ruleValue: {}", userId, strategyId, awardId, ruleValue);
        String[] split = ruleValue.split(Constants.COLON);
        if (split.length == 0) {
            log.error("规则过滤 - 兜底奖品未配置告警, userId: {}, strategyId: {}, awardId: {}, ruleValue: {}", userId, strategyId, awardId, ruleValue);
            throw new RuntimeException("规则过滤 - 兜底奖品未配置告警 " + ruleValue);
        }

        Long luckAwardId = Long.valueOf(split[0]);
        String awardRuleValue = split.length > 1 ? split[1] : "";
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                        .awardId(luckAwardId)
                        .awardRuleValue(awardRuleValue)
                        .build())
                .build();
    }
}
