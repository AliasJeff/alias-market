package com.alias.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import com.alias.infrastructure.persistent.po.RaffleActivityPO;

/**
 * @description 抽奖活动表Dao
 * @create 2024-03-09 10:04
 */
@Mapper
public interface IRaffleActivityDao {

    RaffleActivityPO queryRaffleActivityByActivityId(Long activityId);

}
