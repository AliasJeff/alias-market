package com.alias.infrastructure.persistent.dao;

import com.alias.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAwardDao {

    List<Award> queryAwardList();

}
