package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.enums.ShowTypeEnums;
import com.tianfang.admin.service.IHomeMenuService;
import com.tianfang.business.dto.AlbumPictureDto;
import com.tianfang.dto.SubMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private IHomeMenuService homeMenuService;

    @RequestMapping(value = "index")
    public ModelAndView index(String mId){
        ModelAndView mv = getModelAndView();
        HomeMenuDto menu = homeMenuService.findById(mId);
        List<HomeMenuDto> subMenu = querySubMenu(mId);
        if (null != subMenu && subMenu.size() > 0){
            List<SubMenu> list = new ArrayList<>(subMenu.size());
            SubMenu m;
            for (HomeMenuDto sub : subMenu){
                m = new SubMenu();
                m.setMenu(sub);
                m.setPics(queryMenuPics(sub.getId()));

                list.add(m);
            }
            mv.addObject("subMenu", list);
        }

        mv.addObject("menu", menu); //issingle ("图片轮播 ", 2), three("图片列表", 3),
        mv.addObject("menuId", mId);
        mv.setViewName("/menu/index");
        return mv;
    }
}
