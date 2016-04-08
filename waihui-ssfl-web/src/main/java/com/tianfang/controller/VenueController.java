package com.tianfang.controller;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.service.IHomeMenuService;
import com.tianfang.business.dto.AlbumPictureDto;
import com.tianfang.business.service.IAlbumPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>Description: 场地推荐 </p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/8 下午2:01
 */
@Controller
@RequestMapping(value = "venue")
public class VenueController extends BaseController {

    @Autowired
    private IHomeMenuService homeMenuService;
    @Autowired
    private IAlbumPicService picService;

    @RequestMapping(value = "index")
    public ModelAndView index(String mId){
        ModelAndView mv = getModelAndView();
        HomeMenuDto venue = homeMenuService.findById(mId);
        AlbumPictureDto param = new AlbumPictureDto();
        param.setMenuType(mId);
        List<AlbumPictureDto> pics = picService.findTeamAlbumPic(param);

        mv.addObject("pics", pics);
        mv.addObject("venue", venue);
        mv.addObject("menuId", mId);
        mv.setViewName("/venue/index");
        return mv;
    }
}
