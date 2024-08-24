package com.alias.domain.strategy.service.rule;

import com.alias.domain.strategy.model.entity.RuleActionEntity;
import com.alias.domain.strategy.model.entity.RuleMatterEntity;

public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
