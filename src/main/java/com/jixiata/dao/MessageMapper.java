package com.jixiata.dao;

import com.jixiata.model.Bo.Message;
import com.jixiata.model.Vo.NoMarkNumAndTimeVo;
import com.jixiata.model.Vo.QueryNoMarkMessageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {


    /**
     * 插入数据
     * @param message
     * @return
     */
    Integer insertMessage(Message message);

    /**
     * 通过用户名获取信息
     * @param condition
     * * @return
     */
    List<Message> getMessageByCondition(@Param("condition") Message condition, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 通过MessageId更新信息
     * @param data
     * @return
     */
    Integer updateMessageByKeyId(Message data);

    /**
     * 根据Condition获取数量
     * @param message
     * @return
     */
    Integer getCountByCondition(Message message);

    /**
     * 根据当前用户ID获取存在聊天记录的用户ID
     * @param userId
     * @return
     */
    List<String> getMineExistMessageUser(@Param("userId") String userId);

    /**
     * 查询最后消息时间和未读条数 (单对单)
     * @param mineUserId
     * @param oppoUserId
     * @return
     */
    NoMarkNumAndTimeVo getLastTimeAndNoMarkNumber(@Param("mineUserId") String mineUserId,@Param("oppoUserId") String oppoUserId);
    /**
     * 查询最后消息时间和未读条数 (群)
     * @param mineUserId
     * @return
     */
    NoMarkNumAndTimeVo getLastTimeAndNoMarkNumberOfGroup(@Param("mineUserId") String mineUserId,@Param("groupId") String groupId);

    /**
     * 查询未读消息
     * @param vo
     * @return
     */
    List<Message> queryNoMarkMessage(QueryNoMarkMessageVo vo);

    /**
     * 更新未读消息为已读
     * @param vo
     * @return
     */
    Integer updateNoMarkingMessages(QueryNoMarkMessageVo vo);
}
