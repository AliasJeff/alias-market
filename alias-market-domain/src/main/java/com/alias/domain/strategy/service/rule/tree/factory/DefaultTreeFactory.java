package com.alias.domain.strategy.service.rule.tree.factory;

import com.alias.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.alias.domain.strategy.model.vo.RuleTreeVO;
import com.alias.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.alias.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.alias.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeMap;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeMap) {
        this.logicTreeNodeMap = logicTreeNodeMap;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeMap, ruleTreeVO);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity {
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardVO strategyAwardVO;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** 抽奖奖品ID，内部流转使用 */
        private Long awardId;
        /** 抽奖奖品规则 */
        private String awardRuleValue;
    }

}
