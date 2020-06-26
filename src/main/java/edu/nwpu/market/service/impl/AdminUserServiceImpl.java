package edu.nwpu.market.service.impl;

import edu.nwpu.market.dao.AdminUserMapper;
import edu.nwpu.market.entity.AdminUser;
import edu.nwpu.market.service.AdminUserService;
import edu.nwpu.market.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(userName, passwordMd5);
    }
}
