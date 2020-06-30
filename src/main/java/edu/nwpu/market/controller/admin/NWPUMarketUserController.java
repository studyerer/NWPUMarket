package edu.nwpu.market.controller.admin;

import edu.nwpu.market.service.NWPUMarketUserService;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.Result;
import edu.nwpu.market.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Controller
//设置标识admin
@RequestMapping("/admin")
public class NWPUMarketUserController {

    @Resource
    private NWPUMarketUserService newBeeMallUserService;
    //与users相关联
    @GetMapping("/users")
    public String usersPage(HttpServletRequest request) {
        //设置相关参数
        request.setAttribute("path", "users");
        return "admin/market_user";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        //参数情况判断
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //参数正确，新建
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallUserService.getNewBeeMallUsersPage(pageUtil));
    }

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     */
    @RequestMapping(value = "/users/lock/{lockStatus}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids, @PathVariable int lockStatus) {
        //参数异常
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //判断参数不是0或1的情况
        if (lockStatus != 0 && lockStatus != 1) {
            return ResultGenerator.genFailResult("操作非法！");
        }
        //禁用操作情况
        if (newBeeMallUserService.lockUsers(ids, lockStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("禁用失败");
        }
    }
}