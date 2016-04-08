package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.dto.MenuDto;
import com.tianfang.admin.service.IHomeMenuService;
import com.tianfang.business.dto.AlbumPictureDto;
import com.tianfang.business.service.IAlbumPicService;
import com.tianfang.common.util.PropertiesUtils;
import com.tianfang.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 外烩官网controller层共用属性封装</p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/1 下午2:31
 */
public class BaseController {
    @Autowired
    private IHomeMenuService homeMenuService;
    @Autowired
    private IAlbumPicService picService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final static String TAG = "tianfang";
    private final static String _MENU = "_menu";

    public ModelAndView getModelAndView(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("menuList", getCacheHomeMenu());
        mv.addObject("request_url",getRequest().getRequestURI());
        mv.addObject("wwwdomain", PropertiesUtils.getProperty("wwwdomain"));
        return mv;
    }

    /**
     * <p>Description: 缓存官网菜单(缓存时间1小时) </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @author wangxiang
     * @date 16/4/1 下午4:51
     * @version 1.0
     */
    public List<MenuDto> getCacheHomeMenu() {
        List<MenuDto> homeMenuList;
        String keyCode = TAG+_MENU;
        if(null != redisTemplate.opsForValue().get(keyCode)){
            homeMenuList = (List<MenuDto>)redisTemplate.opsForValue().get(keyCode);
        }else{
            HomeMenuDto param = new HomeMenuDto();
            param.setMenuType("1");
            homeMenuList = homeMenuService.findHomeMenuList(param);
            redisTemplate.opsForValue().set(keyCode, homeMenuList, 1, TimeUnit.HOURS);
        }
        return homeMenuList;
    }

    /**
     * <p>Description: 得到request对象 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param
     * @return
     * @author wangxiang
     * @date 16/4/1 下午4:52
     * @version 1.0
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * <p>Description: 根据菜单Id查询图片 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param mId 菜单id
     * @return List<AlbumPictureDto>
     * @author wangxiang
     * @date 16/4/8 下午3:10
     * @version 1.0
     */
    public List<AlbumPictureDto> queryMenuPics(String mId){
        if (StringUtils.isBlank(mId)){
            return null;
        }
        AlbumPictureDto param = new AlbumPictureDto();
        param.setMenuType(mId);
        List<AlbumPictureDto> pics = picService.findTeamAlbumPic(param);
        return pics;
    }

    /**		
     * <p>Description: 根据菜单id,查询该菜单下的子菜单 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param mId
     * @return List<HomeMenuDto>
     * @author wangxiang	
     * @date 16/4/8 下午3:12
     * @version 1.0
     */
    public List<HomeMenuDto> querySubMenu(String mId){
        if (StringUtils.isBlank(mId)){
            return null;
        }
        return homeMenuService.findByParentId(mId);
    }

}