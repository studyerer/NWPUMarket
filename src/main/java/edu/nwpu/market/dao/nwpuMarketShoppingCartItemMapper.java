package edu.nwpu.market.dao;

import edu.nwpu.market.entity.nwpuMarketShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface nwpuMarketShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(nwpuMarketShoppingCartItem record);

    int insertSelective(nwpuMarketShoppingCartItem record);

    nwpuMarketShoppingCartItem selectByPrimaryKey(Long cartItemId);

    nwpuMarketShoppingCartItem selectByUserIdAndGoodsId(@Param("nwpuMarketUserId") Long nwpuMarketUserId, @Param("goodsId") Long goodsId);

    List<nwpuMarketShoppingCartItem> selectByUserId(@Param("nwpuMarketUserId") Long nwpuMarketUserId, @Param("number") int number);

    int selectCountByUserId(Long nwpuMarketUserId);

    int updateByPrimaryKeySelective(nwpuMarketShoppingCartItem record);

    int updateByPrimaryKey(nwpuMarketShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}