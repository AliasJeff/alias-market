package com.alias.test.domain;

import com.alias.domain.strategy.service.armory.IStrategyArmory;
import com.alias.domain.strategy.service.armory.IStrategyDispatch;
import com.alias.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryDispatchTest {
    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private IRedisService redisService;


    @Before
    public void test_AssembleLotteryStrategy() {
        log.info("开始装配...");
        boolean clearSuccess = redisService.clearCache();
        log.info("清除缓存: {}", clearSuccess ? "成功" : "失败");
        boolean success = strategyArmory.assembleLotteryStrategy(100001L);
        log.info("装配完成: {}", success ? "成功" : "失败");
    }

    @Test
    public void test_getRandomAwardId() {
        log.info("Test getRandomAwardId...");
        log.info("测试结果：{}", strategyDispatch.getRandomAwardId(100001L));
        log.info("测试结果：{}", strategyDispatch.getRandomAwardId(100001L));
        log.info("测试结果：{}", strategyDispatch.getRandomAwardId(100001L));
    }

    @Test
    public void test_getRandomAwardId_ruleWeightValue() {
        log.info("Test getRandomAwardId_ruleWeightValue...");
        log.info("测试结果：{} - 4000 策略配置", strategyDispatch.getRandomAwardId(100001L, "4000"));
        log.info("测试结果：{} - 5000 策略配置", strategyDispatch.getRandomAwardId(100001L, "5000"));
        log.info("测试结果：{} - 6000 策略配置", strategyDispatch.getRandomAwardId(100001L, "6000"));
    }
}
