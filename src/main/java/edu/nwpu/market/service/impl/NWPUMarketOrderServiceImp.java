/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package edu.nwpu.market.service.impl;

import edu.nwpu.market.common.NWPUMarketException;
import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMarketOrderItemVO;
import edu.nwpu.market.entity.NWPUMarketOrder;
import edu.nwpu.market.util.BeanUtil;
import edu.nwpu.market.common.*;
import edu.nwpu.market.controller.vo.*;
import edu.nwpu.market.dao.NWPUMarketGoodsMapper;
import edu.nwpu.market.dao.NWPUMarketOrderItemMapper;
import edu.nwpu.market.dao.NWPUMarketOrderMapper;
import edu.nwpu.market.dao.NWPUMarketShoppingCartItemMapper;
import edu.nwpu.market.entity.NWPUMarketGoods;
import edu.nwpu.market.entity.NWPUMarketOrder;
import edu.nwpu.market.entity.NWPUMarketOrderItem;
import edu.nwpu.market.entity.StockNumDTO;
import edu.nwpu.market.service.NWPUMarketOrderService;
import edu.nwpu.market.util.BeanUtil;
import edu.nwpu.market.util.NumberUtil;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class NWPUMarketOrderServiceImp implements NWPUMarketOrderService {

    @Autowired
    private NWPUMarketOrderMapper NWPUMarketOrderMapper;
    @Autowired
    private NWPUMarketOrderItemMapper NWPUMarketOrderItemMapper;
    @Autowired
    private NWPUMarketShoppingCartItemMapper NWPUMarketShoppingCartItemMapper;
    @Autowired
    private NWPUMarketGoodsMapper NWPUMarketGoodsMapper;

    @Override
    public PageResult getNWPUMarketOrdersPage(PageQueryUtil pageUtil) {
        List<NWPUMarketOrder> NWPUMarketOrders = NWPUMarketOrderMapper.findNWPUMarketOrderList(pageUtil);
        int total = NWPUMarketOrderMapper.getTotalNWPUMarketOrders(pageUtil);
        PageResult pageResult = new PageResult(NWPUMarketOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(NWPUMarketOrder npwuMarketOrder) {
        NWPUMarketOrder temp = NWPUMarketOrderMapper.selectByPrimaryKey(npwuMarketOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(npwuMarketOrder.getTotalPrice());
            temp.setUserAddress(npwuMarketOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (NWPUMarketOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<NWPUMarketOrder> orders = NWPUMarketOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (NWPUMarketOrder NWPUMarketOrder : orders) {
                if (NWPUMarketOrder.getIsDeleted() == 1) {
                    errorOrderNos += NWPUMarketOrder.getOrderNo() + " ";
                    continue;
                }
                if (NWPUMarketOrder.getOrderStatus() != 1) {
                    errorOrderNos += NWPUMarketOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (NWPUMarketOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<NWPUMarketOrder> orders = NWPUMarketOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (NWPUMarketOrder NWPUMarketOrder : orders) {
                if (NWPUMarketOrder.getIsDeleted() == 1) {
                    errorOrderNos += NWPUMarketOrder.getOrderNo() + " ";
                    continue;
                }
                if (NWPUMarketOrder.getOrderStatus() != 1 && NWPUMarketOrder.getOrderStatus() != 2) {
                    errorOrderNos += NWPUMarketOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (NWPUMarketOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<NWPUMarketOrder> orders = NWPUMarketOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (NWPUMarketOrder nwpuMarketOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (nwpuMarketOrder.getIsDeleted() == 1) {
                    errorOrderNos += nwpuMarketOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (nwpuMarketOrder.getOrderStatus() == 4 || nwpuMarketOrder.getOrderStatus() < 0) {
                    errorOrderNos += nwpuMarketOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (NWPUMarketOrderMapper.closeOrder(Arrays.asList(ids), NWPUMarketOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(NWPUMarketUserVO user, List<NWPUMarketShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(NWPUMarketShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(NWPUMarketShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<NWPUMarketGoods> nwpuMarketGoods = NWPUMarketGoodsMapper.selectByPrimaryKeys(goodsIds);
        Map<Long, NWPUMarketGoods> nwpuMarketGoodsMap = nwpuMarketGoods.stream().collect(Collectors.toMap(NWPUMarketGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (NWPUMarketShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!nwpuMarketGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                NWPUMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > nwpuMarketGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                NWPUMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(nwpuMarketGoods)) {
            if (NWPUMarketShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = NWPUMarketGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    NWPUMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                NWPUMarketOrder nwpuMarketOrder = new NWPUMarketOrder();
                nwpuMarketOrder.setOrderNo(orderNo);
                nwpuMarketOrder.setUserId(user.getUserId());
                nwpuMarketOrder.setUserAddress(user.getAddress());
                //总价
                for (NWPUMarketShoppingCartItemVO NWPUMarketShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += NWPUMarketShoppingCartItemVO.getGoodsCount() * NWPUMarketShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    NWPUMarketException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                nwpuMarketOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                nwpuMarketOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (NWPUMarketOrderMapper.insertSelective(nwpuMarketOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<NWPUMarketOrderItem> nwpuMarketOrderItems = new ArrayList<>();
                    for (NWPUMarketShoppingCartItemVO nwpuMarketShoppingCartItemVO : myShoppingCartItems) {
                        NWPUMarketOrderItem nwpuMarketOrderItem = new NWPUMarketOrderItem();
                        //使用BeanUtil工具类将NWPUMarketShoppingCartItemVO中的属性复制到NWPUMarketOrderItem对象中
                        BeanUtil.copyProperties(nwpuMarketShoppingCartItemVO, nwpuMarketOrderItem);
                        //NWPUMarketOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        nwpuMarketOrderItem.setOrderId(nwpuMarketOrder.getOrderId());
                        nwpuMarketOrderItems.add(nwpuMarketOrderItem);
                    }
                    //保存至数据库
                    if (NWPUMarketOrderItemMapper.insertBatch(nwpuMarketOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    NWPUMarketException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                NWPUMarketException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            NWPUMarketException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        NWPUMarketException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public NWPUMarketOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        NWPUMarketOrder NWPUMarketOrder = NWPUMarketOrderMapper.selectByOrderNo(orderNo);
        if (NWPUMarketOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            List<NWPUMarketOrderItem> orderItems = NWPUMarketOrderItemMapper.selectByOrderId(NWPUMarketOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<NWPUMarketOrderItemVO> nwpuMarketOrderItemVOS = BeanUtil.copyList(orderItems, NWPUMarketOrderItemVO.class);
                NWPUMarketOrderDetailVO nwpuMarketOrderDetailVO = new NWPUMarketOrderDetailVO();
                BeanUtil.copyProperties(NWPUMarketOrder, nwpuMarketOrderDetailVO);
                nwpuMarketOrderDetailVO.setOrderStatusString(NWPUMarketOrderStatusEnum.getNWPUMarketOrderStatusEnumByStatus(nwpuMarketOrderDetailVO.getOrderStatus()).getName());
                nwpuMarketOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(nwpuMarketOrderDetailVO.getPayType()).getName());
                nwpuMarketOrderDetailVO.setNWPUMarketOrderItemVOS(nwpuMarketOrderItemVOS);
                return nwpuMarketOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public NWPUMarketOrder getNWPUMarketOrderByOrderNo(String orderNo) {
        return NWPUMarketOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = NWPUMarketOrderMapper.getTotalNWPUMarketOrders(pageUtil);
        List<NWPUMarketOrder> nwpuMarketOrders = NWPUMarketOrderMapper.findNWPUMarketOrderList(pageUtil);
        List<NWPUMarketOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(nwpuMarketOrders, NWPUMarketOrderListVO.class);
            //设置订单状态中文显示值
            for (NWPUMarketOrderListVO nwpuMarketOrderListVO : orderListVOS) {
                nwpuMarketOrderListVO.setOrderStatusString(NWPUMarketOrderStatusEnum.getNWPUMarketOrderStatusEnumByStatus(nwpuMarketOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = nwpuMarketOrders.stream().map(NWPUMarketOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<NWPUMarketOrderItem> orderItems = NWPUMarketOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<NWPUMarketOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(NWPUMarketOrderItem::getOrderId));
                for (NWPUMarketOrderListVO nwpuMarketOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(nwpuMarketOrderListVO.getOrderId())) {
                        List<NWPUMarketOrderItem> orderItemListTemp = itemByOrderIdMap.get(nwpuMarketOrderListVO.getOrderId());
                        //将nwpuMarketOrderItem对象列表转换成nwpuMarketOrderItemVO对象列表
                        List<NWPUMarketOrderItemVO> nwpuMarketOrderItemVOS = BeanUtil.copyList(orderItemListTemp, NWPUMarketOrderItemVO.class);
                        nwpuMarketOrderListVO.setNWPUMarketOrderItemVOS(nwpuMarketOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        NWPUMarketOrder nwpuMarketOrder = NWPUMarketOrderMapper.selectByOrderNo(orderNo);
        if (nwpuMarketOrder != null) {
            if (NWPUMarketOrderMapper.closeOrder(Collections.singletonList(nwpuMarketOrder.getOrderId()), NWPUMarketOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        NWPUMarketOrder nwpuMarketOrder = NWPUMarketOrderMapper.selectByOrderNo(orderNo);
        if (nwpuMarketOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            nwpuMarketOrder.setOrderStatus((byte) NWPUMarketOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            nwpuMarketOrder.setUpdateTime(new Date());
            if (NWPUMarketOrderMapper.updateByPrimaryKeySelective(nwpuMarketOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        NWPUMarketOrder nwpuMarketOrder = NWPUMarketOrderMapper.selectByOrderNo(orderNo);
        if (nwpuMarketOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            nwpuMarketOrder.setOrderStatus((byte) NWPUMarketOrderStatusEnum.OREDER_PAID.getOrderStatus());
            nwpuMarketOrder.setPayType((byte) payType);
            nwpuMarketOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            nwpuMarketOrder.setPayTime(new Date());
            nwpuMarketOrder.setUpdateTime(new Date());
            if (NWPUMarketOrderMapper.updateByPrimaryKeySelective(nwpuMarketOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<NWPUMarketOrderItemVO> getOrderItems(Long id) {
        NWPUMarketOrder nwpuMarketOrder = NWPUMarketOrderMapper.selectByPrimaryKey(id);
        if (nwpuMarketOrder != null) {
            List<NWPUMarketOrderItem> orderItems = NWPUMarketOrderItemMapper.selectByOrderId(nwpuMarketOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<NWPUMarketOrderItemVO> nwpuMarketOrderItemVOS = BeanUtil.copyList(orderItems, NWPUMarketOrderItemVO.class);
                return nwpuMarketOrderItemVOS;
            }
        }
        return null;
    }
}