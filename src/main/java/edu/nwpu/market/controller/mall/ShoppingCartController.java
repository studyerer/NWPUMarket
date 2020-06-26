package edu.nwpu.market.controller.mall;

import edu.nwpu.market.common.Constants;
import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMallShoppingCartItemVO;
import edu.nwpu.market.controller.vo.NWPUMallUserVO;
import edu.nwpu.market.entity.NWPUMarketShoppingCartItem;
import edu.nwpu.market.service.NWPUMarketShoppingCartService;
import edu.nwpu.market.util.Result;
import edu.nwpu.market.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private NWPUMarketShoppingCartService nwpuMarketShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        NWPUMallUserVO user = (NWPUMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<NWPUMallShoppingCartItemVO> myShoppingCartItems = nwpuMarketShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //订单项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(NWPUMallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (NWPUMallShoppingCartItemVO nwpuMarketShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += nwpuMarketShoppingCartItemVO.getGoodsCount() * nwpuMarketShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveNWPUMarketShoppingCartItem(@RequestBody NWPUMarketShoppingCartItem nwpuMarketShoppingCartItem,
                                                 HttpSession httpSession) {
        NWPUMallUserVO user = (NWPUMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        nwpuMarketShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = nwpuMarketShoppingCartService.saveNewBeeMallCartItem(nwpuMarketShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateNWPUMarketShoppingCartItem(@RequestBody NWPUMarketShoppingCartItem nwpuMarketShoppingCartItem,
                                                   HttpSession httpSession) {
        NWPUMallUserVO user = (NWPUMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        nwpuMarketShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = nwpuMarketShoppingCartService.updateNewBeeMallCartItem(nwpuMarketShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @DeleteMapping("/shop-cart/{nwpuMarketShoppingCartItemId}")
    @ResponseBody
    public Result updateNWPUMarketShoppingCartItem(@PathVariable("nwpuMarketShoppingCartItemId") Long nwpuMarketShoppingCartItemId,
                                                   HttpSession httpSession) {
        NWPUMallUserVO user = (NWPUMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = nwpuMarketShoppingCartService.deleteById(nwpuMarketShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        NWPUMallUserVO user = (NWPUMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<NWPUMallShoppingCartItemVO> myShoppingCartItems = nwpuMarketShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (NWPUMallShoppingCartItemVO nwpuMarketShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += nwpuMarketShoppingCartItemVO.getGoodsCount() * nwpuMarketShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}
