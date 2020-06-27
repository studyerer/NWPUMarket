package edu.nwpu.market.controller.mall;

import edu.nwpu.market.common.Constants;
import edu.nwpu.market.common.NWPUMarketException;
import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMarketOrderDetailVO;
import edu.nwpu.market.controller.vo.NWPUMarketShoppingCartItemVO;
import edu.nwpu.market.controller.vo.NWPUMarketUserVO;
import edu.nwpu.market.entity.NWPUMarketOrder;
import edu.nwpu.market.service.NWPUMarketOrderService;
import edu.nwpu.market.service.NWPUMarketShoppingCartService;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.Result;
import edu.nwpu.market.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Resource
    private NWPUMarketShoppingCartService NWPUMarketShoppingCartService;
    @Resource
    private NWPUMarketOrderService NWPUMarketOrderService;

    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        NWPUMarketUserVO user = (NWPUMarketUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NWPUMarketOrderDetailVO orderDetailVO = NWPUMarketOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }

    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        NWPUMarketUserVO user = (NWPUMarketUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", NWPUMarketOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession) {
        NWPUMarketUserVO user = (NWPUMarketUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<NWPUMarketShoppingCartItemVO> myShoppingCartItems = NWPUMarketShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //无收货地址
            NWPUMarketException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据则跳转至错误页
            NWPUMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        //保存订单并返回订单号
        String saveOrderResult = NWPUMarketOrderService.saveOrder(user, myShoppingCartItems);
        //跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }

    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        NWPUMarketUserVO user = (NWPUMarketUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = NWPUMarketOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        NWPUMarketUserVO user = (NWPUMarketUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = NWPUMarketOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        NWPUMarketUserVO user = (NWPUMarketUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NWPUMarketOrder NWPUMarketOrder = NWPUMarketOrderService.getNWPUMarketOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", NWPUMarketOrder.getTotalPrice());
        return "mall/pay-select";
    }

    @GetMapping("/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
        NWPUMarketUserVO user = (NWPUMarketUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        NWPUMarketOrder NWPUMarketOrder = NWPUMarketOrderService.getNWPUMarketOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", NWPUMarketOrder.getTotalPrice());
        if (payType == 1) {
            return "mall/alipay";
        } else {
            return "mall/wxpay";
        }
    }

    @GetMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType) {
        String payResult = NWPUMarketOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
