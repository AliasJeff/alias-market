package com.alias.test.infrastructure;

import com.alias.domain.strategy.model.vo.RuleTreeVO;
import com.alias.infrastructure.persistent.repository.StrategyRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRepositoryTest {

    @Resource
    private StrategyRepository strategyRepository;

    @Test
    public void test_queryRuleTreeVOByTreeId() {
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId("tree_lock");
        log.info("ruleTreeVO: {}", JSON.toJSONString(ruleTreeVO));
    }
}
