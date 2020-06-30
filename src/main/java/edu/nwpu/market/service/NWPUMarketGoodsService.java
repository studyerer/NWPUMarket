package edu.nwpu.market.service;

import edu.nwpu.market.entity.NWPUMarketGoods;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;


import java.util.List;

public interface NWPUMarketGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNWPUMarketGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveNWPUMarketGoods(NWPUMarketGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param nwpuMarketGoodsList
     * @return
     */
    void batchSaveNWPUMarketGoods(List<NWPUMarketGoods> nwpuMarketGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateNWPUMarketGoods(NWPUMarketGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    NWPUMarketGoods getNWPUMarketGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchNWPUMarketGoods(PageQueryUtil pageUtil);
}
