package com.alias.domain.award.service;

import com.alias.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @description 奖品服务接口
 * @create 2024-04-06 09:03
 */
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

}
