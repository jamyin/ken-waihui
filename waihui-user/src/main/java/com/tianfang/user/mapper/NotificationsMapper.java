package com.tianfang.user.mapper;

import com.tianfang.user.pojo.Notifications;
import com.tianfang.user.pojo.NotificationsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NotificationsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int countByExample(NotificationsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int deleteByExample(NotificationsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int insert(Notifications record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int insertSelective(Notifications record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    List<Notifications> selectByExample(NotificationsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    Notifications selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int updateByExampleSelective(@Param("record") Notifications record, @Param("example") NotificationsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int updateByExample(@Param("record") Notifications record, @Param("example") NotificationsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int updateByPrimaryKeySelective(Notifications record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sport_notifications
     *
     * @mbggenerated Wed Mar 16 09:30:12 CST 2016
     */
    int updateByPrimaryKey(Notifications record);
}