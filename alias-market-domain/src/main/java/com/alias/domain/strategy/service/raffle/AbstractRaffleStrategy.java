package com.alias.domain.strategy.service.raffle;

import com.alias.domain.strategy.model.entity.RaffleAwardEntity;
import com.alias.domain.strategy.model.entity.RaffleFactorEntity;
import com.alias.domain.strategy.model.entity.RuleActionEntity;
import com.alias.domain.strategy.model.entity.StrategyEntity;
import com.alias.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.alias.domain.strategy.repository.IStrategyRepository;
import com.alias.domain.strategy.service.IRaffleStrategy;
import com.alias.domain.strategy.service.armory.IStrategyDispatch;
import com.alias.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.alias.types.common.Constants;
import com.alias.types.enums.ResponseCode;
import com.alias.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository repository;
    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 策略查询
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);

        // 抽奖前 - 规则过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())) {
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 黑名单返回固定的奖品ID
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 权重根据返回的信息进行抽奖
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                // FIXME
                Long awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey.split(Constants.COLON)[0]);
                return RaffleAwardEntity.builder()
                        .awardId(Math.toIntExact(awardId))
                        .build();
            }
        }

        // 4. 默认抽奖流程
        Long awardId = strategyDispatch.getRandomAwardId(strategyId);

        return RaffleAwardEntity.builder()
                .awardId(Math.toIntExact(awardId))
                .build();

    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String ...logics);
}
