package edu.nwpu.market.service;

import edu.nwpu.market.controller.vo.NWPUMarketIndexConfigGoodsVO;
import edu.nwpu.market.entity.IndexConfig;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;



import java.util.List;

/**
 * @author XuXiaoJie
 *
 */
public interface NWPUMarketIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<NWPUMarketIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
