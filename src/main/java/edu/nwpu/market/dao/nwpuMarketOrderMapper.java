package edu.nwpu.market.dao;

import edu.nwpu.market.entity.nwpuMarketOrder;
import edu.nwpu.market.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface nwpuMarketOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(nwpuMarketOrder record);

    int insertSelective(nwpuMarketOrder record);

    nwpuMarketOrder selectByPrimaryKey(Long orderId);

    nwpuMarketOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(nwpuMarketOrder record);

    int updateByPrimaryKey(nwpuMarketOrder record);

    List<nwpuMarketOrder> findnwpuMarketOrderList(PageQueryUtil pageUtil);

    int getTotalnwpuMarketOrders(PageQueryUtil pageUtil);

    List<nwpuMarketOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}