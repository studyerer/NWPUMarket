package edu.nwpu.market.controller.admin;

import edu.nwpu.market.common.Constants;
import edu.nwpu.market.common.NWPUMarketCategoryLevelEnum;
import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.entity.GoodsCategory;
import edu.nwpu.market.entity.NWPUMarketGoods;
import edu.nwpu.market.service.NWPUMarketCategoryService;
import edu.nwpu.market.service.NWPUMarketGoodsService;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.Result;
import edu.nwpu.market.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
@Controller
@RequestMapping("/admin")
public class NWPUMarketGoodsController {

    @Resource
    private NWPUMarketGoodsService NWPUMarketGoodsService;
    @Resource
    private NWPUMarketCategoryService NWPUMarketCategoryService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "market_goods");
        return "admin/market_goods";
    }

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NWPUMarketCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), NWPUMarketCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), NWPUMarketCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/market_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        NWPUMarketGoods NWPUMarketGoods = NWPUMarketGoodsService.getNWPUMarketGoodsById(goodsId);
        if (NWPUMarketGoods == null) {
            return "error/error_400";
        }
        if (NWPUMarketGoods.getGoodsCategoryId() > 0) {
            if (NWPUMarketGoods.getGoodsCategoryId() != null || NWPUMarketGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory = NWPUMarketCategoryService.getGoodsCategoryById(NWPUMarketGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == NWPUMarketCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<GoodsCategory> firstLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NWPUMarketCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<GoodsCategory> thirdLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), NWPUMarketCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = NWPUMarketCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), NWPUMarketCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firestCategory = NWPUMarketCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (NWPUMarketGoods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NWPUMarketCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), NWPUMarketCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories = NWPUMarketCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), NWPUMarketCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", NWPUMarketGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/market_goods_edit";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(NWPUMarketGoodsService.getNWPUMarketGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody NWPUMarketGoods NWPUMarketGoods) {
        if (StringUtils.isEmpty(NWPUMarketGoods.getGoodsName())
                || StringUtils.isEmpty(NWPUMarketGoods.getGoodsIntro())
                || StringUtils.isEmpty(NWPUMarketGoods.getTag())
                || Objects.isNull(NWPUMarketGoods.getOriginalPrice())
                || Objects.isNull(NWPUMarketGoods.getGoodsCategoryId())
                || Objects.isNull(NWPUMarketGoods.getSellingPrice())
                || Objects.isNull(NWPUMarketGoods.getStockNum())
                || Objects.isNull(NWPUMarketGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(NWPUMarketGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(NWPUMarketGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = NWPUMarketGoodsService.saveNWPUMarketGoods(NWPUMarketGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody NWPUMarketGoods NWPUMarketGoods) {
        if (Objects.isNull(NWPUMarketGoods.getGoodsId())
                || StringUtils.isEmpty(NWPUMarketGoods.getGoodsName())
                || StringUtils.isEmpty(NWPUMarketGoods.getGoodsIntro())
                || StringUtils.isEmpty(NWPUMarketGoods.getTag())
                || Objects.isNull(NWPUMarketGoods.getOriginalPrice())
                || Objects.isNull(NWPUMarketGoods.getSellingPrice())
                || Objects.isNull(NWPUMarketGoods.getGoodsCategoryId())
                || Objects.isNull(NWPUMarketGoods.getStockNum())
                || Objects.isNull(NWPUMarketGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(NWPUMarketGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(NWPUMarketGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = NWPUMarketGoodsService.updateNWPUMarketGoods(NWPUMarketGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        NWPUMarketGoods goods = NWPUMarketGoodsService.getNWPUMarketGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (NWPUMarketGoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

}