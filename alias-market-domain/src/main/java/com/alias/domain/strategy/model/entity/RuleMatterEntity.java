package com.alias.domain.strategy.model.entity;

import lombok.Data;

@Data
public class RuleMatterEntity {

    private String userId;

    private Long strategyId;

    private Long awardId;

    private String ruleModel;
}
