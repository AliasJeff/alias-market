package com.alias.domain.strategy.service;

import com.alias.domain.strategy.model.entity.RaffleAwardEntity;
import com.alias.domain.strategy.model.entity.RaffleFactorEntity;

public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);

}
