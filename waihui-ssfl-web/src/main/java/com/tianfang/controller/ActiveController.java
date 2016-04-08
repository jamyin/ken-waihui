package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.service.IHomeMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>Description: 活动案例 </p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/8 下午1:38
 */
@Controller
@RequestMapping(value = "active")
public class ActiveController extends BaseController {

    @Autowired
    private IHomeMenuService homeMenuService;

    @RequestMapping(value = "index")
    public ModelAndView index(String mId){
        ModelAndView mv = getModelAndView();
        HomeMenuDto active = homeMenuService.findById(mId);
        List<HomeMenuDto> subMenu = querySubMenu(mId);

        mv.addObject("active", active);
        mv.addObject("subMenu", subMenu);
        mv.addObject("menuId", mId);
        mv.setViewName("/active/index");
        return mv;
    }

}
