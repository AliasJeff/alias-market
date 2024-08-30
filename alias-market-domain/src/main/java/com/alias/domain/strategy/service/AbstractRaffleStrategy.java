package com.alias.domain.strategy.service;

import com.alias.domain.strategy.model.entity.RaffleAwardEntity;
import com.alias.domain.strategy.model.entity.RaffleFactorEntity;
import com.alias.domain.strategy.model.entity.RuleActionEntity;
import com.alias.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.alias.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.domain.strategy.service.armory.IStrategyDispatch;
import com.alias.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.alias.types.enums.ResponseCode;
import com.alias.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository repository;
    protected IStrategyDispatch strategyDispatch;

    private DefaultChainFactory defaultChainFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 责任链处理抽奖
        Long awardId = defaultChainFactory.openLogicChain(strategyId).logic(userId, strategyId);

        // 查询奖品规则：抽奖中（拿到奖品ID时，过滤规则），抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModel(strategyId, awardId);

        // 抽奖中，规则过滤
        RuleActionEntity<RuleActionEntity.RaffleMiddleEntity> ruleActionMiddleEntity = this.doCheckRaffleMiddleLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).awardId(awardId).build(), strategyAwardRuleModelVO.raffleMiddleRuleModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionMiddleEntity.getCode())) {
            return RaffleAwardEntity.builder()
                    .awardDesc("抽奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励.")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();

    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleMiddleEntity> doCheckRaffleMiddleLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}
