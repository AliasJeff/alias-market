package com.alias.domain.strategy.service.rule.tree;

import com.alias.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

public interface ILogicTreeNode {

    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId);
}