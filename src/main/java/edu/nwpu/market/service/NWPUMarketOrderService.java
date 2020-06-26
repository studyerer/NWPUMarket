package edu.nwpu.market.service;

import edu.nwpu.market.controller.vo.NWPUMarketOrderDetailVO;
import edu.nwpu.market.controller.vo.NWPUMarketOrderItemVO;
import edu.nwpu.market.controller.vo.NWPUMarketShoppingCartItemVO;
import edu.nwpu.market.controller.vo.NWPUMarketUserVO;
import edu.nwpu.market.entity.NWPUMarketOrder;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;

import java.util.List;

public interface NWPUMarketOrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNWPUMarketOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param NWPUMarketOrder
     * @return
     */
    String updateOrderInfo(NWPUMarketOrder NWPUMarketOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(NWPUMarketUserVO user, List<NWPUMarketShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    NWPUMarketOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    NWPUMarketOrder getNWPUMarketOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<NWPUMarketOrderItemVO> getOrderItems(Long id);
}
