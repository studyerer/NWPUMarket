package edu.nwpu.market.service.impl;

import edu.nwpu.market.common.Constants;
import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMarketShoppingCartItemVO;
import edu.nwpu.market.dao.NWPUMarketGoodsMapper;
import edu.nwpu.market.dao.NWPUMarketShoppingCartItemMapper;
import edu.nwpu.market.entity.NWPUMarketGoods;
import edu.nwpu.market.entity.NWPUMarketShoppingCartItem;
import edu.nwpu.market.service.NWPUMarketShoppingCartService;
import edu.nwpu.market.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NWPUMarketShoppingCartServiceImpl implements NWPUMarketShoppingCartService {

    @Autowired
    private NWPUMarketShoppingCartItemMapper NWPUMarketShoppingCartItemMapper;

    @Autowired
    private NWPUMarketGoodsMapper NWPUMarketGoodsMapper;

    //todo 修改session中购物项数量

    @Override
    public String saveNWPUMarketCartItem(NWPUMarketShoppingCartItem NWPUMarketShoppingCartItem) {
        NWPUMarketShoppingCartItem temp = NWPUMarketShoppingCartItemMapper.selectByUserIdAndGoodsId(NWPUMarketShoppingCartItem.getUserId(), NWPUMarketShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(NWPUMarketShoppingCartItem.getGoodsCount());
            return updateNWPUMarketCartItem(temp);
        }
        NWPUMarketGoods NWPUMarketGoods = NWPUMarketGoodsMapper.selectByPrimaryKey(NWPUMarketShoppingCartItem.getGoodsId());
        //商品为空
        if (NWPUMarketGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = NWPUMarketShoppingCartItemMapper.selectCountByUserId(NWPUMarketShoppingCartItem.getUserId());
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (NWPUMarketShoppingCartItemMapper.insertSelective(NWPUMarketShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNWPUMarketCartItem(NWPUMarketShoppingCartItem NWPUMarketShoppingCartItem) {
        NWPUMarketShoppingCartItem nwpuMarketShoppingCartItemUpdate = NWPUMarketShoppingCartItemMapper.selectByPrimaryKey(NWPUMarketShoppingCartItem.getCartItemId());
        if (nwpuMarketShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (NWPUMarketShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        nwpuMarketShoppingCartItemUpdate.setGoodsCount(NWPUMarketShoppingCartItem.getGoodsCount());
        nwpuMarketShoppingCartItemUpdate.setUpdateTime(new Date());
        //保存记录
        if (NWPUMarketShoppingCartItemMapper.updateByPrimaryKeySelective(nwpuMarketShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public NWPUMarketShoppingCartItem getNWPUMarketCartItemById(Long nwpuMarketShoppingCartItemId) {
        return NWPUMarketShoppingCartItemMapper.selectByPrimaryKey(nwpuMarketShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long nwpuMarketShoppingCartItemId) {
        //todo userId不同不能删除
        return NWPUMarketShoppingCartItemMapper.deleteByPrimaryKey(nwpuMarketShoppingCartItemId) > 0;
    }

    @Override
    public List<NWPUMarketShoppingCartItemVO> getMyShoppingCartItems(Long nwpuMarketUserId) {
        List<NWPUMarketShoppingCartItemVO> nwpuMarketShoppingCartItemVOS = new ArrayList<>();
        List<NWPUMarketShoppingCartItem> nwpuMarketShoppingCartItems = NWPUMarketShoppingCartItemMapper.selectByUserId(nwpuMarketUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(nwpuMarketShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> nwpuMarketGoodsIds = nwpuMarketShoppingCartItems.stream().map(NWPUMarketShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<NWPUMarketGoods> marketGoods = NWPUMarketGoodsMapper.selectByPrimaryKeys(nwpuMarketGoodsIds);
            Map<Long, NWPUMarketGoods> nwpuMarketGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(marketGoods)) {
                nwpuMarketGoodsMap = marketGoods.stream().collect(Collectors.toMap(NWPUMarketGoods::getGoodsId,
                        Function.identity(),
                        (entity1, entity2) -> entity1));
            }
            for (NWPUMarketShoppingCartItem nwpuMarketShoppingCartItem : nwpuMarketShoppingCartItems) {
                NWPUMarketShoppingCartItemVO nwpuMarketShoppingCartItemVO = new NWPUMarketShoppingCartItemVO();
                BeanUtil.copyProperties(nwpuMarketShoppingCartItem, nwpuMarketShoppingCartItemVO);
                if (nwpuMarketGoodsMap.containsKey(nwpuMarketShoppingCartItem.getGoodsId())) {
                    NWPUMarketGoods nwpuMarketGoodsTemp = nwpuMarketGoodsMap.get(nwpuMarketShoppingCartItem.getGoodsId());
                    nwpuMarketShoppingCartItemVO.setGoodsCoverImg(nwpuMarketGoodsTemp.getGoodsCoverImg());
                    String goodsName = nwpuMarketGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    nwpuMarketShoppingCartItemVO.setGoodsName(goodsName);
                    nwpuMarketShoppingCartItemVO.setSellingPrice(nwpuMarketGoodsTemp.getSellingPrice());
                    nwpuMarketShoppingCartItemVOS.add(nwpuMarketShoppingCartItemVO);
                }
            }
        }
        return nwpuMarketShoppingCartItemVOS;
    }
}
