package com.alias.domain.strategy.service.rule;

import com.alias.domain.strategy.model.entity.RuleActionEntity;
import com.alias.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @ClassName ILogicFilter
 * @Description 抽奖规则过滤接口
 * @Author alex_shen
 * @Date 2024/3/5 - 14:08
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
