package com.tianfang.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tianfang.admin.dto.MenuDto;
import com.tianfang.enums.HomeMenuEnum;


@Controller
@RequestMapping(value = "catering")
public class CateringController extends BaseController{

	/**
	 * 
		 * 此方法描述的是：
		 * @author: cwftalus@163.com
		 * @version: 2016年4月7日 上午9:12:17
	 */
    @RequestMapping(value = "index")
    public ModelAndView index(){
        ModelAndView mv = getModelAndView(); 

        mv.setViewName("/catering/index");
        return mv;
    }

}
