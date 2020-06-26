package edu.nwpu.market.dao;

import edu.nwpu.market.entity.NWPUMarketShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NWPUMarketShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(NWPUMarketShoppingCartItem record);

    int insertSelective(NWPUMarketShoppingCartItem record);

    NWPUMarketShoppingCartItem selectByPrimaryKey(Long cartItemId);

    NWPUMarketShoppingCartItem selectByUserIdAndGoodsId(@Param("NWPUMarketUserId") Long NWPUMarketUserId, @Param("goodsId") Long goodsId);

    List<NWPUMarketShoppingCartItem> selectByUserId(@Param("NWPUMarketUserId") Long NWPUMarketUserId, @Param("number") int number);

    int selectCountByUserId(Long NWPUMarketUserId);

    int updateByPrimaryKeySelective(NWPUMarketShoppingCartItem record);

    int updateByPrimaryKey(NWPUMarketShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}