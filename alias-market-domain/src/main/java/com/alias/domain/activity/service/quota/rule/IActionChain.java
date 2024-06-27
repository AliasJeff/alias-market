package com.alias.domain.activity.service.quota.rule;

import com.alias.domain.activity.model.entity.ActivityCountEntity;
import com.alias.domain.activity.model.entity.ActivityEntity;
import com.alias.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @ClassName IActionChain
 * @Description 下单规则过滤接口
 * @Author alex_shen
 * @Date 2024/4/1 - 17:19
 */
public interface IActionChain extends IActionChainArmory{

    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

}
