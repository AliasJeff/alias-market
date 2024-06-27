package com.alias.infrastructure.persistent.dao;


import org.apache.ibatis.annotations.Mapper;
import com.alias.infrastructure.persistent.po.RuleTreeNodePO;

import java.util.List;

/**
 * @description 规则树节点表DAO
 * @create 2024-02-03 08:43
 */
@Mapper
public interface IRuleTreeNodeDao {

    List<RuleTreeNodePO> queryRuleTreeNodeListByTreeId(String treeId);

}
