package com.alias.domain.strategy.model.entity;

import com.alias.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {

        private Long strategyId;

        /**
         * 权重值 key，用于抽奖时选择权重抽奖
         */
        private String ruleWeightValueKey;

        private Integer awardId;
    }

    static public class RaffleAfterEntity extends RaffleEntity {

    }
}
