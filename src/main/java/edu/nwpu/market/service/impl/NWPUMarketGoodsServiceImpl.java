package edu.nwpu.market.service.impl;

import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMarketSearchGoodsVO;
import edu.nwpu.market.dao.NWPUMarketGoodsMapper;
import edu.nwpu.market.entity.NWPUMarketGoods;
import edu.nwpu.market.service.NWPUMarketGoodsService;
import edu.nwpu.market.util.BeanUtil;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NWPUMarketGoodsServiceImpl implements NWPUMarketGoodsService {

    @Autowired
    private NWPUMarketGoodsMapper goodsMapper;

    @Override
    public PageResult getNWPUMarketGoodsPage(PageQueryUtil pageUtil) {
        List<NWPUMarketGoods> goodsList = goodsMapper.findNWPUMarketGoodsList(pageUtil);
        int total = goodsMapper.getTotalNWPUMarketGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveNWPUMarketGoods(NWPUMarketGoods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveNWPUMarketGoods(List<NWPUMarketGoods> NWPUMarketGoodsList) {
        if (!CollectionUtils.isEmpty(NWPUMarketGoodsList)) {
            goodsMapper.batchInsert(NWPUMarketGoodsList);
        }
    }

    @Override
    public String updateNWPUMarketGoods(NWPUMarketGoods goods) {
        NWPUMarketGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public NWPUMarketGoods getNWPUMarketGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchNWPUMarketGoods(PageQueryUtil pageUtil) {
        List<NWPUMarketGoods> goodsList = goodsMapper.findNWPUMarketGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalNWPUMarketGoodsBySearch(pageUtil);
        List<NWPUMarketSearchGoodsVO> NWPUMarketSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            NWPUMarketSearchGoodsVOS = BeanUtil.copyList(goodsList, NWPUMarketSearchGoodsVO.class);
            for (NWPUMarketSearchGoodsVO NWPUMarketSearchGoodsVO : NWPUMarketSearchGoodsVOS) {
                String goodsName = NWPUMarketSearchGoodsVO.getGoodsName();
                String goodsIntro = NWPUMarketSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    NWPUMarketSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    NWPUMarketSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(NWPUMarketSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
