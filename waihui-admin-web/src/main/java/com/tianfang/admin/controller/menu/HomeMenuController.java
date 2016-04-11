package com.tianfang.admin.controller.menu;

import com.tianfang.admin.controller.BaseController;
import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.enums.ActivityTypeEnums;
import com.tianfang.admin.enums.MenuTypeEnums;
import com.tianfang.admin.enums.ShowTypeEnums;
import com.tianfang.admin.enums.VideoTypeEnums;
import com.tianfang.admin.pojo.HomeMenu;
import com.tianfang.admin.service.IHomeMenuService;
import com.tianfang.admin.utils.PageData;
import com.tianfang.common.ext.ExtPageQuery;
import com.tianfang.common.model.MessageResp;
import com.tianfang.common.model.PageResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/menu")
public class HomeMenuController extends BaseController {

    protected static final Log logger = LogFactory.getLog(HomeMenuController.class);

    @Autowired
    private IHomeMenuService homeMenuService;

    @RequestMapping("/findPage")
    public ModelAndView findPage(HomeMenuDto dto, ExtPageQuery page) {
        ModelAndView mv = this.getModelAndView(this.getSessionUserId());
        PageResult<HomeMenuDto> results = homeMenuService.findPage(dto, page.changeToPageQuery());
        setPname(results);
        mv.setViewName("/menu/list");
        mv.addObject("pageList", results);
        mv.addObject("params", dto);
        mv.addObject("alls", getCacheHomeMenu());
        mv.addObject("menuTypes", MenuTypeEnums.getValus());
        mv.addObject("showTypes", ShowTypeEnums.getValus());
        mv.addObject("acTypes", ActivityTypeEnums.getValus());
        mv.addObject("videoTypes", VideoTypeEnums.getValus());
        return mv;
    }

    @RequestMapping("/openAddView")
    public ModelAndView openAddView() {
        logger.info("打开新增菜单页面");
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("/menu/add");
        mv.addObject("menuTypes", MenuTypeEnums.getValus());
        mv.addObject("showTypes", ShowTypeEnums.getValus());
        mv.addObject("acTypes", ActivityTypeEnums.getValus());
        mv.addObject("videoTypes", VideoTypeEnums.getValus());
        return mv;
    }

    /**
     * ajax查询上级菜单
     *
     * @param menuType
     * @return
     * @author xiang_wang
     * 2015年12月22日下午3:18:39
     */
    @RequestMapping("/selectMenumType")
    @ResponseBody
    public List<HomeMenuDto> selectMenumType(Integer menuType) {
        List<HomeMenuDto> HomeMenuDtos = null;
        if (null != menuType && menuType.intValue() > 1) {
            HomeMenuDtos = homeMenuService.findByMenuType(menuType - 1);
        }
        return HomeMenuDtos;
    }

    /**
     * 更新菜单排序
     *
     * @param homeMenuDto
     * @return
     * @author xiang_wang
     * 2015年12月23日下午3:36:44
     */
    @RequestMapping("/updateOrder")
    @ResponseBody
    public Map<String, Object> updateOrder(HomeMenuDto homeMenuDto) {
        Integer result = homeMenuService.edit(homeMenuDto);
        if (result == 1) {
            cleanCacheHomeMenu();
            return MessageResp.getMessage(true, "更新菜单排序成功");
        }
        return MessageResp.getMessage(false, "更新菜单排序失败");
    }

    /**
     * 新增菜单
     *
     * @param homeMenuDto
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(HomeMenuDto homeMenuDto) {
//    	if (null != homeMenuDto.getMenuType() && homeMenuDto.getMenuType().intValue() == MenuTypeEnums.ONE.getIndex()) {
//    		homeMenuDto.setParentId("0");
//    	}
        Integer result = homeMenuService.save(homeMenuDto);
        if (result == 1) {
            cleanCacheHomeMenu();
            return MessageResp.getMessage(true, "新增成功");
        }
        return MessageResp.getMessage(false, "新增失败");
    }

    @RequestMapping("/openEditView")
    public ModelAndView openEditView() {
        logger.info("打开编辑菜单页面");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String id = pd.getString("id");
        HomeMenuDto homeMenuDto = homeMenuService.findById(id);
//        if (null != homeMenuDto.getParentId() && !"0".equals(homeMenuDto.getParentId().trim()) && null != homeMenuDto.getMenuType() && homeMenuDto.getMenuType().intValue() > 1){
//        	List<HomeMenuDto> HomeMenuDtos = homeMenuService.findByMenuType(homeMenuDto.getMenuType() - 1);
//        	mv.addObject("parents", HomeMenuDtos);
//        }
        mv.setViewName("/menu/edit");
        mv.addObject("data", homeMenuDto);
        mv.addObject("menuTypes", MenuTypeEnums.getValus());
        mv.addObject("showTypes", ShowTypeEnums.getValus());
        mv.addObject("acTypes", ActivityTypeEnums.getValus());
        mv.addObject("videoTypes", VideoTypeEnums.getValus());

        return mv;
    }

    /**
     * 编辑菜单
     *
     * @param homeMenuDto
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Map<String, Object> edit(HomeMenuDto homeMenuDto) {
//    	if (null != HomeMenuDto.getMenuType() && HomeMenuDto.getMenuType().intValue() == MenuTypeEnums.ONE.getIndex()) {
//    		HomeMenuDto.setParentId("0");
//    	}
        Integer result = homeMenuService.edit(homeMenuDto);
        if (result == 1) {
            cleanCacheHomeMenu();
            return MessageResp.getMessage(true, "编辑成功");
        }
        return MessageResp.getMessage(false, "编辑失败");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        if (ids == null || "".equals(ids)) {
            return MessageResp.getMessage(false, "请求参数不能为空~");
        }
        homeMenuService.delete(ids);
        cleanCacheHomeMenu();
        return MessageResp.getMessage(true, "删除成功！");
    }

    /**		
     * <p>Description: 给对应的菜单添加父级菜单名 </p>
     * <p>Company: 上海天坊信息科技有限公司</p>
     * @param 
     * @return 
     * @author wangxiang	
     * @date 16/4/11 上午11:52
     * @version 1.0
     */
    private void setPname(PageResult<HomeMenuDto> results) {
        if (null != results && null != results.getResults() && results.getResults().size() > 0) {
            Map<String, HomeMenuDto> homeMenuMap = getHomeMenuMap();
            for (HomeMenuDto dto : results.getResults()){
                if (!dto.getMenuType().equals(MenuTypeEnums.ONE.getIndex()+"") && homeMenuMap.containsKey(dto.getParentId())){
                    dto.setPname(homeMenuMap.get(dto.getParentId()).getCname());
                }
            }
        }
    }
}
