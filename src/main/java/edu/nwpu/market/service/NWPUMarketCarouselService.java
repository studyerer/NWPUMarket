package edu.nwpu.market.service;

import edu.nwpu.market.controller.vo.NWPUMarketIndexCarouselVO;
import edu.nwpu.market.entity.Carousel;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.PageResult;


import java.util.List;

public interface NWPUMarketCarouselService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<NWPUMarketIndexCarouselVO> getCarouselsForIndex(int number);
}
