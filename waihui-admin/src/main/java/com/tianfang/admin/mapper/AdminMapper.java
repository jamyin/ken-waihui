package com.tianfang.admin.mapper;

import com.tianfang.admin.pojo.Admin;
import com.tianfang.admin.pojo.AdminExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int countByExample(AdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int deleteByExample(AdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int insert(Admin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int insertSelective(Admin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    List<Admin> selectByExample(AdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    Admin selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int updateByPrimaryKeySelective(Admin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sst_admin
     *
     * @mbggenerated Mon Jan 11 15:53:21 CST 2016
     */
    int updateByPrimaryKey(Admin record);
}