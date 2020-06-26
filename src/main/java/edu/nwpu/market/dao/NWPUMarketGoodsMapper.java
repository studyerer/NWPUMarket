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

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds, @Param("sellStatus") int sellStatus);

}