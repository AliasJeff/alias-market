package com.alias.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alias.domain.strategy.model.entity.StrategyAwardEntity;
import com.alias.domain.strategy.model.valobj.RuleTreeVO;
import com.alias.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.alias.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.domain.strategy.service.AbstractRaffleStrategy;
import com.alias.domain.strategy.service.IRaffleAward;
import com.alias.domain.strategy.service.IRaffleStock;
import com.alias.domain.strategy.service.armory.IStrategyDispatch;
import com.alias.domain.strategy.service.rule.chain.ILogicChain;
import com.alias.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.alias.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.alias.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.alias.types.common.Constants;

import java.util.List;

/**
 * @ClassName DefaultRaffleStrategy
 * @Description 默认抽奖策略
 * @Author alex_shen
 * @Date 2024/3/5 - 16:46
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleAward, IRaffleStock {

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    /**
     * 抽奖计算，责任链抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    /**
     * 抽奖结果过滤，决策树抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 过滤结果【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
     */
    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        // 查询奖品的规则模型
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        if (null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }

        // 查询规则树
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (ruleTreeVO == null) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key, 未在库表 rule_tree, rule_tree_node, rule_tree_line 配置对应的规则树信息");
        }

        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    /**
     * 根据策略ID查询抽奖奖品列表配置
     *
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }
}
