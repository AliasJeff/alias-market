package com.alias.domain.strategy.service.raffle;

import com.alias.domain.strategy.model.vo.RuleTreeVO;
import com.alias.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import com.alias.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.domain.strategy.service.AbstractRaffleStrategy;
import com.alias.domain.strategy.service.armory.IStrategyDispatch;
import com.alias.domain.strategy.service.rule.chain.ILogicChain;
import com.alias.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.alias.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.alias.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Long awardId) {
        StrategyAwardRuleModelVO awardRuleModelVO = repository.queryStrategyAwardRuleModel(strategyId, awardId);
        // 未配置规则 rule models
        if (null == awardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }

        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(awardRuleModelVO.getRuleModels());
        if (null == ruleTreeVO) {
            throw new RuntimeException(awardRuleModelVO.getRuleModels() + " 规则树未配置");
        }
        IDecisionTreeEngine decisionTreeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return decisionTreeEngine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Long awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }
}
