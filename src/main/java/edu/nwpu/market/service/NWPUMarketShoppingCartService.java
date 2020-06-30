package edu.nwpu.market.service;

import edu.nwpu.market.controller.vo.NWPUMarketShoppingCartItemVO;
import edu.nwpu.market.entity.NWPUMarketShoppingCartItem;

import java.util.List;

public interface NWPUMarketShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param nwpuMarketShoppingCartItem
     * @return
     */
    String saveNWPUMarketCartItem(NWPUMarketShoppingCartItem nwpuMarketShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param nwpuMarketShoppingCartItem
     * @return
     */
    String updateNWPUMarketCartItem(NWPUMarketShoppingCartItem nwpuMarketShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param nwpuMarketShoppingCartItem
     * @return
     */
    NWPUMarketShoppingCartItem getNWPUMarketCartItemById(Long nwpuMarketShoppingCartItem);

    /**
     * 删除购物车中的商品
     *
     * @param nwpuMarketShoppingCartItem
     * @return
     */
    Boolean deleteById(Long nwpuMarketShoppingCartItem);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param nwpuMarketUserId
     * @return
     */
    List<NWPUMarketShoppingCartItemVO> getMyShoppingCartItems(Long nwpuMarketUserId);
}
