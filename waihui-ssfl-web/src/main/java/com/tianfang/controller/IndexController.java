package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.dto.MenuDto;
import com.tianfang.business.dto.AlbumPictureDto;
import com.tianfang.business.service.IAlbumPicService;
import com.tianfang.common.model.PageQuery;
import com.tianfang.common.model.PageResult;
import com.tianfang.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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
    private final static int SERVER_INDEX = 2;  // 外烩服务下标
    private final static int MENU_INDEX = 1;    // 精品菜单下标
    private final static int COMPANY_INDEX = 3; // 公司资质下标
    private final static int ACTIVE_INDEX = 4;  // 活动案例下标
    private final static int CONTACT_INDEX = 6; // 联系我们下标


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
        getWaihuiServer(menus, mv);
        // 3.精选菜单
        getSelectMenu(menus, mv);
        // 4.公司资质
        getCompany(menus, mv);
        // 5.活动案例
        getActivity(menus, mv);
        // 6.联系我们
        getContactUs(menus, mv);

        if (StringUtils.isBlank(mId)){
            if (null != menus && menus.size() > 0){
                if (null != menus.get(0).getHomeMenuDto())
                mId = (menus.get(0)).getHomeMenuDto().getId();
            }
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
            for (int i = 0; i < 5; i++){
                if (menus.size() > i+1){
                    list.add(menus.get(i+1));
                }else{
                    break;
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
    private void getWaihuiServer(List<MenuDto> menus, ModelAndView mv) {
        if (null != menus && menus.size() > SERVER_INDEX){
            if (null != menus.get(SERVER_INDEX).getHomeMenuDto()){
                List<AlbumPictureDto> pics = getPics(menus.get(SERVER_INDEX), SERVER_NUM);
                if (null != pics){
                    mv.addObject("server", menus.get(SERVER_INDEX).getHomeMenuDto());
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
    private void getSelectMenu(List<MenuDto> menus, ModelAndView mv) {
        if (null != menus && menus.size() > MENU_INDEX){
            if (null != menus.get(MENU_INDEX).getHomeMenuDto()){
                List<AlbumPictureDto> pics = getPics(menus.get(MENU_INDEX), BOOK_NUM);
                if (null != pics){
                    mv.addObject("book", menus.get(MENU_INDEX).getHomeMenuDto());
                    List<HomeMenuDto> subMenu = querySubMenu(menus.get(MENU_INDEX).getHomeMenuDto().getId());
                    mv.addObject("subMenu", subMenu);
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
    private void getCompany(List<MenuDto> menus, ModelAndView mv) {
        if (null != menus && menus.size() > COMPANY_INDEX){
            if (null != menus.get(COMPANY_INDEX).getHomeMenuDto()){
                mv.addObject("company", menus.get(COMPANY_INDEX).getHomeMenuDto());
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
    private void getActivity(List<MenuDto> menus, ModelAndView mv) {
        if (null != menus && menus.size() > ACTIVE_INDEX){
            if (null != menus.get(ACTIVE_INDEX).getHomeMenuDto()){
                mv.addObject("activity", menus.get(ACTIVE_INDEX).getHomeMenuDto());
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
    private void getContactUs(List<MenuDto> menus, ModelAndView mv) {
        if (null != menus && menus.size() > CONTACT_INDEX){
            if (null != menus.get(CONTACT_INDEX).getHomeMenuDto()) {
                mv.addObject("contact", menus.get(CONTACT_INDEX).getHomeMenuDto());
            }
        }
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