package edu.nwpu.market.controller.admin;

import edu.nwpu.market.entity.AdminUser;
import edu.nwpu.market.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/admin")
public class AdminController {
    //定义adminUserService
    @Resource
    private AdminUserService adminUserService;
    //定义与login页面相关联的login方法
    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }
    //定义与test页面相关联的test方法
    @GetMapping({"/test"})
    public String test() {
        return "admin/test";
    }

    /**
     *
     * @param request
     * @return 返回页面相关参数信息
     */
    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", 0);
        request.setAttribute("blogCount", 0);
        request.setAttribute("linkCount", 0);
        request.setAttribute("tagCount", 0);
        request.setAttribute("commentCount", 0);
        request.setAttribute("path", "index");
        return "admin/index";
    }
    //涉及到密码安全问题，所以用Post;当出现/login时会执行下述方法，以判断登陆情况。
    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        HttpSession session) {
        //实体类adminuser调用login方法，并执行login操作。
        AdminUser adminUser = adminUserService.login(userName, password);
        //登录操作
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            return "redirect:/admin/index";
        } else {
            //登录失败的页面显示信息
            session.setAttribute("errorMsg", "用户名或密码错误！");
            return "admin/login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //清空session中的数据，前端控制跳转至登录页，登出成功。
        request.getSession().invalidate();
        return "admin/login";
    }
}
