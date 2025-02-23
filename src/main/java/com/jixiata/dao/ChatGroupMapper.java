package com.jixiata.dao;

import com.jixiata.model.Bo.ChatGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatGroupMapper {


    /**
     * 插入数据
     * @param chatGroup
     * @return
     */
    Integer insertChatGroup(ChatGroup chatGroup);

    /**
     * 通过用户名获取信息
     * @param condition
     * * @return
     */
    List<ChatGroup> getChatGroupByCondition(@Param("condition") ChatGroup condition, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 通过ChatGroupId更新信息
     * @param data
     * @return
     */
    Integer updateChatGroupByKeyId(ChatGroup data);

    /**
     * 根据Condition获取数量
     * @param chatGroup
     * @return
     */
    Integer getCountByCondition(ChatGroup chatGroup);
}
