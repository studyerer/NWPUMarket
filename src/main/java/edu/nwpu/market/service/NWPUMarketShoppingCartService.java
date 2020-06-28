package edu.nwpu.market.service;

import edu.nwpu.market.controller.vo.NWPUMarketShoppingCartItemVO;
import edu.nwpu.market.entity.NWPUMarketShoppingCartItem;

import java.util.List;

/**
 * @author GCZ
 *
 */
public interface NWPUMarketShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param NWPUMarketShoppingCartItem
     * @return
     */
    String saveNWPUMarketCartItem(NWPUMarketShoppingCartItem NWPUMarketShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param NWPUMarketShoppingCartItem
     * @return
     */
    String updateNWPUMarketCartItem(NWPUMarketShoppingCartItem NWPUMarketShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param NWPUMarketShoppingCartItemId
     * @return
     */
    NWPUMarketShoppingCartItem getNWPUMarketCartItemById(Long NWPUMarketShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param NWPUMarketShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long NWPUMarketShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param NWPUMarketUserId
     * @return
     */
    List<NWPUMarketShoppingCartItemVO> getMyShoppingCartItems(Long NWPUMarketUserId);
}
