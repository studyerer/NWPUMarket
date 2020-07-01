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
            temp.setGoodsCount(temp.getGoodsCount()+1);
            return updateNWPUMarketCartItem(temp);
        }
        NWPUMarketGoods nwpuMarketGoods = NWPUMarketGoodsMapper.selectByPrimaryKey(NWPUMarketShoppingCartItem.getGoodsId());
        //商品为空
        if (nwpuMarketGoods == null) {
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
    public String updateNWPUMarketCartItem(NWPUMarketShoppingCartItem nwpuMarketShoppingCartItem) {
        NWPUMarketShoppingCartItem nwpuMarketShoppingCartItemUpdate = NWPUMarketShoppingCartItemMapper.selectByPrimaryKey(nwpuMarketShoppingCartItem.getCartItemId());
        if (nwpuMarketShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (nwpuMarketShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        if (nwpuMarketShoppingCartItem.getGoodsCount() == nwpuMarketShoppingCartItemUpdate.getGoodsCount()){
            return ServiceResultEnum.OPERATE_ERROR.getResult();
        }
        //todo userId不同不能修改
        if (nwpuMarketShoppingCartItem.getUserId() != nwpuMarketShoppingCartItemUpdate.getUserId()){
            return ServiceResultEnum.OPERATE_ERROR.getResult();
        }
        nwpuMarketShoppingCartItemUpdate.setGoodsCount(nwpuMarketShoppingCartItem.getGoodsCount());
        nwpuMarketShoppingCartItemUpdate.setUpdateTime(new Date());
        //保存记录
        if (NWPUMarketShoppingCartItemMapper.updateByPrimaryKeySelective(nwpuMarketShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public NWPUMarketShoppingCartItem getNWPUMarketCartItemById(Long NWPUMarketShoppingCartItemId) {
        return NWPUMarketShoppingCartItemMapper.selectByPrimaryKey(NWPUMarketShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long NWPUMarketShoppingCartItemId) {
        //todo userId不同不能删除
        return NWPUMarketShoppingCartItemMapper.deleteByPrimaryKey(NWPUMarketShoppingCartItemId) > 0;
    }

    @Override
    public List<NWPUMarketShoppingCartItemVO> getMyShoppingCartItems(Long NWPUMarketUserId) {
        List<NWPUMarketShoppingCartItemVO> NWPUMarketShoppingCartItemVOS = new ArrayList<>();
        List<NWPUMarketShoppingCartItem> NWPUMarketShoppingCartItems = NWPUMarketShoppingCartItemMapper.selectByUserId(NWPUMarketUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(NWPUMarketShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> NWPUMarketGoodsIds = NWPUMarketShoppingCartItems.stream().map(NWPUMarketShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<NWPUMarketGoods> marketGoods = NWPUMarketGoodsMapper.selectByPrimaryKeys(NWPUMarketGoodsIds);
            Map<Long, NWPUMarketGoods> NWPUMarketGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(marketGoods)) {
                NWPUMarketGoodsMap = marketGoods.stream().collect(Collectors.toMap(NWPUMarketGoods::getGoodsId,
                        Function.identity(),
                        (entity1, entity2) -> entity1));
            }
            for (NWPUMarketShoppingCartItem NWPUMarketShoppingCartItem : NWPUMarketShoppingCartItems) {
                NWPUMarketShoppingCartItemVO NWPUMarketShoppingCartItemVO = new NWPUMarketShoppingCartItemVO();
                BeanUtil.copyProperties(NWPUMarketShoppingCartItem, NWPUMarketShoppingCartItemVO);
                if (NWPUMarketGoodsMap.containsKey(NWPUMarketShoppingCartItem.getGoodsId())) {
                    NWPUMarketGoods NWPUMarketGoodsTemp = NWPUMarketGoodsMap.get(NWPUMarketShoppingCartItem.getGoodsId());
                    NWPUMarketShoppingCartItemVO.setGoodsCoverImg(NWPUMarketGoodsTemp.getGoodsCoverImg());
                    String goodsName = NWPUMarketGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    NWPUMarketShoppingCartItemVO.setGoodsName(goodsName);
                    NWPUMarketShoppingCartItemVO.setSellingPrice(NWPUMarketGoodsTemp.getSellingPrice());
                    NWPUMarketShoppingCartItemVOS.add(NWPUMarketShoppingCartItemVO);
                }
            }
        }
        return NWPUMarketShoppingCartItemVOS;
    }
}
