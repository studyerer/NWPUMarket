/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package edu.nwpu.market.dao;

import edu.nwpu.market.entity.NWPUMarketGoods;
import edu.nwpu.market.entity.StockNumDTO;
import edu.nwpu.market.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NWPUMarketGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(NWPUMarketGoods record);

    int insertSelective(NWPUMarketGoods record);

    NWPUMarketGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(NWPUMarketGoods record);

    int updateByPrimaryKeyWithBLOBs(NWPUMarketGoods record);

    int updateByPrimaryKey(NWPUMarketGoods record);

    List<NWPUMarketGoods> findNWPUMarketGoodsList(PageQueryUtil pageUtil);

    int getTotalNWPUMarketGoods(PageQueryUtil pageUtil);

    List<NWPUMarketGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<NWPUMarketGoods> findNWPUMarketGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNWPUMarketGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("NWPUMarketGoodsList") List<NWPUMarketGoods> NWPUMarketGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}