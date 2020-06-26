package edu.nwpu.market.service;
import edu.nwpu.market.entity.AdminUser;

public interface AdminUserService {

    AdminUser login(String userName, String password);

}
