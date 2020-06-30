package edu.nwpu.market.controller.admin;


import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMarketOrderItemVO;
import edu.nwpu.market.entity.NWPUMarketOrder;
import edu.nwpu.market.service.NWPUMarketOrderService;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.Result;
import edu.nwpu.market.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class NWPUMarketOrderController {
    @Resource
    private NWPUMarketOrderService nwpuMarketOrderService;//Controller的服务提供对象

    @GetMapping("/orders")
    public String ordersPage(HttpServletRequest request) {
        request.setAttribute("path", "orders");
        return "admin/nwpu_market_order";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(nwpuMarketOrderService.getNWPUMarketOrdersPage(pageUtil));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody NWPUMarketOrder nwpuMarketOrder) {
        if (Objects.isNull(nwpuMarketOrder.getTotalPrice())
                || Objects.isNull(nwpuMarketOrder.getOrderId())
                || nwpuMarketOrder.getOrderId() < 1
                || nwpuMarketOrder.getTotalPrice() < 1
                || StringUtils.isEmpty(nwpuMarketOrder.getUserAddress())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = nwpuMarketOrderService.updateOrderInfo(nwpuMarketOrder);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/order-items/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        List<NWPUMarketOrderItemVO> orderItems = nwpuMarketOrderService.getOrderItems(id);
        if (!CollectionUtils.isEmpty(orderItems)) {
            return ResultGenerator.genSuccessResult(orderItems);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    /**
     * 配货
     */
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = nwpuMarketOrderService.checkDone(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 出库
     */
    @RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
    @ResponseBody
    public Result checkOut(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = nwpuMarketOrderService.checkOut(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/orders/close", method = RequestMethod.POST)
    @ResponseBody
    public Result closeOrder(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = nwpuMarketOrderService.closeOrder(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }
}
