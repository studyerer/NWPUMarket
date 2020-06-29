package edu.nwpu.market.service.impl;

import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.controller.vo.NWPUMarketIndexConfigGoodsVO;
import edu.nwpu.market.dao.IndexConfigMapper;
import edu.nwpu.market.dao.NWPUMarketGoodsMapper;
import edu.nwpu.market.entity.IndexConfig;
import edu.nwpu.market.entity.NWPUMarketGoods;
import edu.nwpu.market.service.NWPUMarketIndexConfigService;
import edu.nwpu.market.util.BeanUtil;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NWPUMarketIndexConfigServiceImpl implements NWPUMarketIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private NWPUMarketGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<NWPUMarketIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<NWPUMarketIndexConfigGoodsVO> NWPUMarketIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());//获取每一个对应商品的ID
            List<NWPUMarketGoods> NWPUMarketGoods = goodsMapper.selectByPrimaryKeys(goodsIds);//选出上面的得到ID对应的商品
            NWPUMarketIndexConfigGoodsVOS = BeanUtil.copyList(NWPUMarketGoods, NWPUMarketIndexConfigGoodsVO.class);//DAO数据和VO数据拷贝
            for (NWPUMarketIndexConfigGoodsVO nwpuMarketIndexConfigGoodsVO : NWPUMarketIndexConfigGoodsVOS) {
                String goodsName = nwpuMarketIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = nwpuMarketIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    nwpuMarketIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    nwpuMarketIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return NWPUMarketIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
