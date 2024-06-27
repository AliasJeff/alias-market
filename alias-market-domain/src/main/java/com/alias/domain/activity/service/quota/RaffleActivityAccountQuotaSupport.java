package com.alias.domain.activity.service.quota;

import com.alias.domain.activity.model.entity.ActivityCountEntity;
import com.alias.domain.activity.model.entity.ActivityEntity;
import com.alias.domain.activity.model.entity.ActivitySkuEntity;
import com.alias.domain.activity.repository.IActivityRepository;
import com.alias.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;

/**
 * @ClassName RaffleActivityAccountQuotaSupport
 * @Description 抽奖活动支持类
 * @Author alex_shen
 * @Date 2024/4/1 - 17:10
 */
public class RaffleActivityAccountQuotaSupport {
    protected IActivityRepository activityRepository;
    protected DefaultActivityChainFactory defaultActivityChainFactory;

    public RaffleActivityAccountQuotaSupport(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory = defaultActivityChainFactory;
    }

    public ActivitySkuEntity queryActivitySku(Long sku) {
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}
