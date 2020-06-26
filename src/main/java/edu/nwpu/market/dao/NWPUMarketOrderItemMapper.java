package edu.nwpu.market.dao;


import edu.nwpu.market.entity.NWPUMarketOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NWPUMarketOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(NWPUMarketOrderItem record);

    int insertSelective(NWPUMarketOrderItem record);

    NWPUMarketOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<NWPUMarketOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<NWPUMarketOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<NWPUMarketOrderItem> orderItems);

    int updateByPrimaryKeySelective(NWPUMarketOrderItem record);

    int updateByPrimaryKey(NWPUMarketOrderItem record);
}