package com.alias.domain.strategy.service.rule.chain.factory;

import com.alias.domain.strategy.model.entity.StrategyEntity;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainMap;

    private final IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainMap, IStrategyRepository repository) {
        this.logicChainMap = logicChainMap;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();

        if (null == ruleModels || 0 == ruleModels.length) {
            return logicChainMap.get("default");
        }

        ILogicChain logicChain = logicChainMap.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain next = logicChainMap.get(ruleModels[i]);
            current = current.appendNext(next);
        }
        current.appendNext(logicChainMap.get("default"));

        return logicChain;
    }
}
