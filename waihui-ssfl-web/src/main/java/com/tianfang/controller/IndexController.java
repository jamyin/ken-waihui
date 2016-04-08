package com.tianfang.controller;

import com.tianfang.admin.dto.MenuDto;
import com.tianfang.business.dto.AlbumPictureDto;
import com.tianfang.business.service.IAlbumPicService;
import com.tianfang.common.model.PageQuery;
import com.tianfang.common.model.PageResult;
import com.tianfang.common.util.StringUtils;
import com.tianfang.enums.HomeMenuEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Description: 外烩官网首页</p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/1 下午2:23
 */
@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController{
    protected static final Log logger = LogFactory.getLog(IndexController.class);
    private final static int SERVER_NUM = 5;    // 外烩服务展示图片条数
    private final static int BOOK_NUM = 3;      // 精选菜单展示图片条数

    @Autowired
    private IAlbumPicService picService;

    /**
     * <p>Description: 外烩官网首页 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/6 下午2:10
     * @version 1.0
     */
    @RequestMapping(value = "index")
    public ModelAndView index(String mId){
        ModelAndView mv = getModelAndView();
        List<MenuDto> menus = getCacheHomeMenu();

        // 1.组装翻转菜单
        showMenus(menus, mv);
        // 2.外烩服务
        getWaihuiServer(getMenu(menus, HomeMenuEnum.SERVER), mv);
        // 3.精选菜单
        getSelectMenu(getMenu(menus, HomeMenuEnum.BOOK), mv);
        // 4.公司资质
        getCompany(getMenu(menus, HomeMenuEnum.COMPANY), mv);
        // 5.活动案例
        getActivity(getMenu(menus, HomeMenuEnum.ACITIVITY), mv);
        // 6.联系我们
        getContactUs(getMenu(menus, HomeMenuEnum.CONTACT), mv);

        if (StringUtils.isBlank(mId)){
            mId = (menus.get(0)).getHomeMenuDto().getId();
        }
        mv.addObject("menuId", mId);
        mv.setViewName("/index");
        return mv;
    }

    /**		
     * <p>Description: 组装需要翻转显示的菜单 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang	
     * @date 16/4/6 下午4:16
     * @version 1.0
     */
    private void showMenus(List<MenuDto> menus, ModelAndView mv) {
        if (null != menus && menus.size() > 0){
            List<MenuDto> list = new ArrayList<>(5);
            for (MenuDto menu : menus){
                if (null != menu.getHomeMenuDto()){
                    if (menu.getHomeMenuDto().getId().equals(HomeMenuEnum.SERVER.getId())){
                        list.add(menu);
                    }
                    if (menu.getHomeMenuDto().getId().equals(HomeMenuEnum.BOOK.getId())){
                        list.add(menu);
                    }
                    if (menu.getHomeMenuDto().getId().equals(HomeMenuEnum.COMPANY.getId())){
                        list.add(menu);
                    }
                    if (menu.getHomeMenuDto().getId().equals(HomeMenuEnum.ACITIVITY.getId())){
                        list.add(menu);
                    }
                    if (menu.getHomeMenuDto().getId().equals(HomeMenuEnum.AREA.getId())){
                        list.add(menu);
                    }
                }
            }
            mv.addObject("showMenus", list);
        }
    }

    /**
     * <p>Description: 组装外烩服务数据:1.外烩服务介绍,2.图片展示 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang	
     * @date 16/4/5 上午10:52
     * @version 1.0
     */
    private void getWaihuiServer(MenuDto menu, ModelAndView mv) {
        if (null != menu){
            if (null != menu.getHomeMenuDto()){
                List<AlbumPictureDto> pics = getPics(menu, SERVER_NUM);
                if (null != pics){
                    mv.addObject("server", menu.getHomeMenuDto());
                    mv.addObject("serverPics", pics);
                }
            }
        }
    }

    /**
     * <p>Description: 组装精选菜单数据:1.精选菜单介绍,2.菜单图片展示 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/5 上午10:58
     * @version 1.0
     */
    private void getSelectMenu(MenuDto menu, ModelAndView mv) {
        if (null != menu){
            if (null != menu.getHomeMenuDto()){
                List<AlbumPictureDto> pics = getPics(menu, BOOK_NUM);
                if (null != pics){
                    mv.addObject("book", menu.getHomeMenuDto());
                    mv.addObject("bookPics", pics);
                }
            }
        }
    }

    /**
     * <p>Description: 组装公司资质:1.公司资质介绍 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/5 上午10:59
     * @version 1.0
     */
    private void getCompany(MenuDto menu, ModelAndView mv) {
        if (null != menu){
            if (null != menu.getHomeMenuDto()){
                mv.addObject("company", menu.getHomeMenuDto());
            }
        }
    }

    /**
     * <p>Description: 组装活动案例:1.活动案例介绍 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/5 上午11:00
     * @version 1.0
     */
    private void getActivity(MenuDto menu, ModelAndView mv) {
        if (null != menu){
            if (null != menu.getHomeMenuDto()){
                mv.addObject("activity", menu.getHomeMenuDto());
            }
        }
    }

    /**
     * <p>Description: 组装联系我们数据 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/5 上午11:02
     * @version 1.0
     */
    private void getContactUs(MenuDto menu, ModelAndView mv) {
        if (null != menu){
            if (null != menu.getHomeMenuDto()){
                mv.addObject("contact", menu.getHomeMenuDto());
            }
        }
    }

    /**
     * <p>Description: 根据菜单枚举变量获取菜单信息,注:会对menus集合进行删除操作,减少下次查找数据基数 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/6 下午2:06
     * @version 1.0
     */
    private MenuDto getMenu(List<MenuDto> menus, HomeMenuEnum e){
        if (null != menus && menus.size() > 0 ){
            Iterator<MenuDto> it = menus.iterator();
            MenuDto menu;
            while (it.hasNext()){
                menu = it.next();
                if (null != menu.getHomeMenuDto() && menu.getHomeMenuDto().getId().equals(e.getId())){
                    it.remove();
                    return menu;
                }
            }
        }
        return null;
    }

    /**
     * <p>Description: 根据菜单类型查询图片信息 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/6 下午2:25
     * @version 1.0
     */
    private List<AlbumPictureDto> getPics(MenuDto menu, int pageSize) {
        AlbumPictureDto param = new AlbumPictureDto();
        param.setMenuType(menu.getHomeMenuDto().getId());
        PageQuery query = new PageQuery(pageSize);
        query.setCurrPage(1);
        PageResult<AlbumPictureDto> result = picService.findAlbumPicPage(param, query);
        if (null != result && null != result.getResults() && result.getResults().size() > 0){
            return result.getResults();
        }
        return null;
    }
}