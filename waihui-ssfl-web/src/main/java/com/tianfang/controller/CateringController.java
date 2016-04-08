package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.service.IHomeMenuService;
import com.tianfang.dto.SubMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "catering")
public class CateringController extends BaseController{

    @Autowired
    private IHomeMenuService homeMenuService;

	/**		
	 * <p>Description: 外烩服务首页 </p>
	 * <p>Company: 上海天坊信息科技有限公司</p>
	 * @param
	 * @return
	 * @author wangxiang	
	 * @date 16/4/7 上午10:49
	 * @version 1.0
	 */
    @RequestMapping(value = "index")
    public ModelAndView index(String mId){
        ModelAndView mv = getModelAndView();
        HomeMenuDto service = homeMenuService.findById(mId);
        List<HomeMenuDto> subs = homeMenuService.findByParentId(mId);
        if (null != subs){
            List<SubMenu> subMenus = new ArrayList<>(subs.size());
            SubMenu menu;
            for (HomeMenuDto sub : subs){
                menu = new SubMenu();
                menu.setMenu(sub);
                menu.setPics(queryMenuPics(sub.getId()));

                subMenus.add(menu);
            }

            mv.addObject("subMenus", subMenus);
        }
        mv.addObject("menuId", mId);
        mv.addObject("service", service);
        mv.setViewName("/catering/index");
        return mv;
    }
}
