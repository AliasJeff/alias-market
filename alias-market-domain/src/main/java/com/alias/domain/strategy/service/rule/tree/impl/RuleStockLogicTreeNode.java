package com.alias.domain.strategy.service.rule.tree.impl;

import com.alias.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.alias.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.domain.strategy.service.armory.IStrategyDispatch;
import com.alias.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.alias.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description 库存扣减节点
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId, String ruleValue) {
        log.info("规则过滤 - 库存扣减节点, userId: {}, strategyId: {}, awardId: {}", userId, strategyId, awardId);
        Boolean status = strategyDispatch.subtractionAwardStock(strategyId, awardId);
        if (status) {
            // 写入延迟队列，延迟消费更新数据库记录。【在 trigger 的 job：UpdateAwardStockJob 下消费队列，更新数据库记录】
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder().StrategyId(strategyId).AwardId(awardId).build());
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue("")
                            .build())
                    .build();
        }

        // 库存不足，直接放行
        log.warn("规则过滤-库存扣减-告警，库存不足, userId: {}, strategyId: {}, awardId: {}", userId, strategyId, awardId);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
