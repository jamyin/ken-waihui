package com.tianfang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Description: 精品菜单 </p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/8 上午11:19
 */
@Controller
@RequestMapping(value = "menu")
public class MenuController extends BaseController {

    @RequestMapping(value = "index")
    public ModelAndView index(String mId){
        ModelAndView mv = getModelAndView();

        mv.addObject("menuId", mId);
        mv.setViewName("/menu/index");
        return mv;
    }
}
