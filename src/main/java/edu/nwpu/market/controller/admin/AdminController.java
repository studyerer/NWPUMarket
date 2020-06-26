package edu.nwpu.market.controller.admin;

import edu.nwpu.market.common.ServiceResultEnum;
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

    @GetMapping("/changePassword")
    public String changePassword(HttpServletRequest request) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "changePassword");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/changePassword";
    }

    @PostMapping("/changePassword/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "redirect:admin/login";
        } else {
            return "修改失败";
        }
    }

    @PostMapping("/changePassword/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
            return ServiceResultEnum.SUCCESS.getResult();
        } else {
            return "修改失败";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //清空session中的数据，前端控制跳转至登录页，登出成功。
        request.getSession().invalidate();
        return "admin/login";
    }
}
