package com.tianfang.controller;

import java.util.List;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.pojo.HomeMenu;
import com.tianfang.admin.service.IHomeMenuService;
import com.tianfang.business.dto.AlbumPictureDto;
import com.tianfang.business.service.IAlbumPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tianfang.admin.dto.MenuDto;
import com.tianfang.enums.HomeMenuEnum;


@Controller
@RequestMapping(value = "catering")
public class CateringController extends BaseController{

    @Autowired
    private IHomeMenuService homeMenuService;
    @Autowired
    private IAlbumPicService picService;

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
    public ModelAndView index(){
        ModelAndView mv = getModelAndView();
        HomeMenuDto service = homeMenuService.findById(HomeMenuEnum.SERVER.getId());
        HomeMenuDto partyType = homeMenuService.findById(HomeMenuEnum.PARTYTYPE.getId());
        AlbumPictureDto param = new AlbumPictureDto();
        param.setMenuType(partyType.getId());
        List<AlbumPictureDto> pics = picService.findTeamAlbumPic(param);

        mv.addObject("service", service);
        mv.addObject("partyType", partyType);
        mv.addObject("pics", pics);
        mv.setViewName("/catering/index");
        return mv;
    }


}
