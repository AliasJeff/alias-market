package com.alias.domain.strategy.model.vo;

import com.alias.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.alias.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

    public String[] raffleMiddleRuleModelList() {
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModels = this.ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModels) {
            if (DefaultLogicFactory.LogicModel.isMiddle(ruleModelValue))
                ruleModelList.add(ruleModelValue);
        }
        return ruleModelList.toArray(new String[0]);
    }
}
