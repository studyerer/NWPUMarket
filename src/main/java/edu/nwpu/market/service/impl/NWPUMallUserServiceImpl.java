
package edu.nwpu.market.service.impl;

import edu.nwpu.market.common.Constants;
import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMallUserVO;
import edu.nwpu.market.dao.MallUserMapper;
import edu.nwpu.market.entity.NWPUMallUser;
import edu.nwpu.market.service.NWPUMallUserService;
import edu.nwpu.market.util.BeanUtil;
import edu.nwpu.market.util.MD5Util;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class NWPUMallUserServiceImpl implements NWPUMallUserService {

    @Autowired
    private MallUserMapper mallUserMapper;

    @Override
    public PageResult getMallUsersPage(PageQueryUtil pageUtil) {
        List<NWPUMallUser> NWPUMallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(NWPUMallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        NWPUMallUser registerUser = new NWPUMallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        NWPUMallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            NWPUMallUserVO newBeeMallUserVO = new NWPUMallUserVO();
            BeanUtil.copyProperties(user, newBeeMallUserVO);
            //设置购物车中的数量
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, newBeeMallUserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public NWPUMallUserVO updateUserInfo(NWPUMallUser NWPUMallUser, HttpSession httpSession) {
        NWPUMallUser user = mallUserMapper.selectByPrimaryKey(NWPUMallUser.getUserId());
        if (user != null) {
            user.setNickName(NWPUMallUser.getNickName());
            user.setAddress(NWPUMallUser.getAddress());
            user.setIntroduceSign(NWPUMallUser.getIntroduceSign());
            if (mallUserMapper.updateByPrimaryKeySelective(user) > 0) {
                NWPUMallUserVO newBeeMallUserVO = new NWPUMallUserVO();
                user = mallUserMapper.selectByPrimaryKey(NWPUMallUser.getUserId());
                BeanUtil.copyProperties(user, newBeeMallUserVO);
                httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, newBeeMallUserVO);
                return newBeeMallUserVO;
            }
        }
        return null;
    }

    @Override
    public Boolean lockUsers(Integer[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}
