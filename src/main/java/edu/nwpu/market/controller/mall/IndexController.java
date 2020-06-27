package edu.nwpu.market.controller.mall;

import edu.nwpu.market.common.Constants;
import edu.nwpu.market.common.IndexConfigTypeEnum;
import edu.nwpu.market.controller.vo.NWPUMarketIndexCarouselVO;
import edu.nwpu.market.controller.vo.NWPUMarketIndexCategoryVO;
import edu.nwpu.market.controller.vo.NWPUMarketIndexConfigGoodsVO;
import edu.nwpu.market.service.NWPUMarketCarouselService;
import edu.nwpu.market.service.NWPUMarketCategoryService;
import edu.nwpu.market.service.NWPUMarketIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private NWPUMarketCarouselService nwpuMarketCarouselService;

    @Resource
    private NWPUMarketIndexConfigService nwpuMarketIndexConfigService;

    @Resource
    private NWPUMarketCategoryService nwpuMarketCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<NWPUMarketIndexCategoryVO> categories = nwpuMarketCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<NWPUMarketIndexCarouselVO> carousels = nwpuMarketCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<NWPUMarketIndexConfigGoodsVO> hotGoodses = nwpuMarketIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<NWPUMarketIndexConfigGoodsVO> newGoodses = nwpuMarketIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<NWPUMarketIndexConfigGoodsVO> recommendGoodses = nwpuMarketIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
}
