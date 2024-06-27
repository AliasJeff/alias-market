package com.alias.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import com.alias.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.alias.infrastructure.persistent.po.StrategyAwardPO;

import java.util.List;

/**
 * @ClassName IStrategyAwardDao
 * @Description 抽奖策略奖品明细配置 Dao
 * @Author alex_shen
 * @Date 2024/3/2 - 22:23
 */
@Mapper
public interface IStrategyAwardDao {
    List<StrategyAwardPO> queryStrategyAwardList();

    List<StrategyAwardPO> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAwardPO strategyAwardPO);

    void updateStrategyAwardStock(StrategyAwardPO strategyAwardPO);

    StrategyAwardPO queryStrategyAward(StrategyAwardPO strategyAwardReq);
}
