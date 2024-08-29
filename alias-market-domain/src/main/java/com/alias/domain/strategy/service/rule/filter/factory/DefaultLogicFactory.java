package com.alias.domain.strategy.service.rule.filter.factory;

import com.alias.domain.strategy.model.entity.RuleActionEntity;
import com.alias.domain.strategy.service.annotation.LogicStrategy;
import com.alias.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicModel().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖", "before"),
        RULE_WEIGHT("rule_weight", "权重规则", "before"),
        RULE_LOCK("rule_lock", "锁定规则", "middle"),
        RULE_LUCK_AWARD("rule_luck_award", "奖品兜底", "after"),
        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isMiddle(String code) {
            return "middle".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

    }
}
