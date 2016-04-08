package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.service.IHomeMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>Description: ${TODO} </p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/8 下午2:24
 */
@Controller
@RequestMapping(value = "contactus")
public class ContactUsController extends BaseController {
    @Autowired
    private IHomeMenuService homeMenuService;

    @RequestMapping(value = "index")
    public ModelAndView index(String mId) {
        ModelAndView mv = getModelAndView();
        HomeMenuDto contactus = homeMenuService.findById(mId);
        List<HomeMenuDto> subMenu = homeMenuService.findByParentId(mId);

        mv.addObject("contactus", contactus);
        mv.addObject("subMenu", subMenu);
        mv.addObject("menuId", mId);
        mv.setViewName("/contactus/index");
        return mv;
    }
}
