package edu.nwpu.market.service;
import edu.nwpu.market.entity.AdminUser;

public interface AdminUserService {

    /**
     * 登录
     * @param userName, password
     * @return
     */
    AdminUser login(String userName, String password);

    /**
     * 更新用户名称
     *
     * @param loginUserId
     * @param loginUserName
     * @param nickName
     * @return
     */
    boolean updateName(Integer loginUserId, String loginUserName, String nickName);

    /**
     * 更新用户密码
     *
     * @param loginUserId
     * @param originalPassword
     * @param newPassword
     * @return
     */
    boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

    /**
     * 获取用户信息
     *
     * @param loginUserId
     * @return
     */
    AdminUser getUserDetailById(Integer loginUserId);


}
