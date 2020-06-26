package edu.nwpu.market.dao;

import edu.nwpu.market.entity.AdminUser;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {
    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    /**
     * 登陆方法
     *
     * @param userName
     * @param password
     * @return
     */
    AdminUser login(@Param("userName") String userName, @Param("password") String password);

    /**
     * 根据登录用户Id选择管理员对象
     *
     * @param adminUserId
     * @return
     */
    AdminUser selectByPrimaryKey(Integer adminUserId);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);
}