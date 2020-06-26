package edu.nwpu.market.dao;


import edu.nwpu.market.entity.NWPUMarketOrder;
import edu.nwpu.market.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NWPUMarketOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(NWPUMarketOrder record);

    int insertSelective(NWPUMarketOrder record);

    NWPUMarketOrder selectByPrimaryKey(Long orderId);

    NWPUMarketOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(NWPUMarketOrder record);

    int updateByPrimaryKey(NWPUMarketOrder record);

    List<NWPUMarketOrder> findNWPUMarketOrderList(PageQueryUtil pageUtil);

    int getTotalNWPUMarketOrders(PageQueryUtil pageUtil);

    List<NWPUMarketOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}