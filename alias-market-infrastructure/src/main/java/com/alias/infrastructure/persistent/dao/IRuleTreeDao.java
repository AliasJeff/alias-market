package com.alias.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import com.alias.infrastructure.persistent.po.RuleTreePO;

/**
 * @description 规则树表DAO
 * @create 2024-02-03 08:42
 */
@Mapper
public interface IRuleTreeDao {

    RuleTreePO queryRuleTreeByTreeId(String treeId);

}
