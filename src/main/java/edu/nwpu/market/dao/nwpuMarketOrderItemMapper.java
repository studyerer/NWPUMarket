package edu.nwpu.market.dao;

import edu.nwpu.market.entity.nwpuMarketOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface nwpuMarketOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(nwpuMarketOrderItem record);

    int insertSelective(nwpuMarketOrderItem record);

    nwpuMarketOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<nwpuMarketOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<nwpuMarketOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<nwpuMarketOrderItem> orderItems);

    int updateByPrimaryKeySelective(nwpuMarketOrderItem record);

    int updateByPrimaryKey(nwpuMarketOrderItem record);
}