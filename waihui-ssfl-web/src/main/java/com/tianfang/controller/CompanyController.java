package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.service.IHomeMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>Description: 公司资质 </p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/8 上午11:19
 */
@Controller
@RequestMapping(value = "company")
public class CompanyController extends BaseController {
    @Autowired
    private IHomeMenuService homeMenuService;

    @RequestMapping(value = "index")
    public ModelAndView index(String mId){
        ModelAndView mv = getModelAndView();
        List<HomeMenuDto> subMenu = homeMenuService.findByParentId(mId);
        mv.addObject("subMenu", subMenu);
        mv.addObject("menuId", mId);
        mv.setViewName("/company/index");
        return mv;
    }
}
