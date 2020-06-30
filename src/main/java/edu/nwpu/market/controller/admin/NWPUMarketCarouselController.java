package edu.nwpu.market.controller.admin;

import edu.nwpu.market.common.ServiceResultEnum;
import edu.nwpu.market.entity.Carousel;
import edu.nwpu.market.service.NWPUMarketCarouselService;
import edu.nwpu.market.util.PageQueryUtil;
import edu.nwpu.market.util.Result;
import edu.nwpu.market.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Controller
//定义到admin文件夹
@RequestMapping("/admin")
public class NWPUMarketCarouselController {

    @Resource
    NWPUMarketCarouselService nwpuMarketCarouselService;
    //标注到carousels页面，控制carousels的相关操作
    @GetMapping("/carousels")
    public String carouselPage(HttpServletRequest request) {
        //页面路径设置
        request.setAttribute("path", "market_carousel");
        return "admin/market_carousel";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/carousels/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        //参数判断情况
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //参数正确，新建
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(nwpuMarketCarouselService.getCarouselPage(pageUtil));
    }

    /**
     * 添加Carousel
     */
    @RequestMapping(value = "/carousels/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Carousel carousel) {
        //进行相关变量的判断，如地址，范围
        if (StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //调用newBeeMallCarouselService添加carousel
        String result = nwpuMarketCarouselService.saveCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/carousels/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Carousel carousel) {
        //对相关变量进行判断，如id,地址，范围
        if (Objects.isNull(carousel.getCarouselId())
                || StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //调用newBeeMallCarouselService修改carousel
        String result = nwpuMarketCarouselService.updateCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/carousels/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
        //通过Id显示相关carousel详情
        Carousel carousel = nwpuMarketCarouselService.getCarouselById(id);
        if (carousel == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(carousel);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //根据id删除caroursels
        if (nwpuMarketCarouselService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}