package com.tianfang.admin.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.admin.enums.AuditStatusEnums;
import com.tianfang.admin.service.IHomeMenuService;
import com.tianfang.admin.utils.PageData;
import com.tianfang.business.dto.AlbumDto;
import com.tianfang.business.dto.AlbumPictureDto;
import com.tianfang.business.service.IAlbumPicService;
import com.tianfang.business.service.IAlbumService;
import com.tianfang.common.ext.ExtPageQuery;
import com.tianfang.common.model.MessageResp;
import com.tianfang.common.model.PageResult;
import com.tianfang.common.util.StringUtils;

/**
 * @author YIn
 * @time:2016年2月29日 下午3:22:32
 * @ClassName: AlbumPicController
 * @Description: 相片控制器
 * @
 */
@Controller
@RequestMapping(value = "/albumPic")
public class AlbumPicController extends BaseController{
	protected static final Log logger = LogFactory.getLog(AlbumPicController.class);
	
	@Autowired
	private IAlbumPicService iAlbumPicService;
	
	@Autowired
	private IAlbumService iAlbumService;
	
	@Autowired
	private IHomeMenuService iHomeMenuService;
	
	/**
	 * 去新增页面页面
	 */
	@RequestMapping(value="/goAlbumPicAdd")
	public ModelAndView goAdd(){
		logger.info("去相片新增页面");
		ModelAndView mv = this.getModelAndView(this.getSessionUserId());
		/*List<AlbumDto> albumList =  iAlbumService.findAlbum(new AlbumDto());
		mv.addObject("albumList", albumList);*/
		List<HomeMenuDto> list = iHomeMenuService.findAll();
		mv.addObject("menuTypes", list);
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("/albumPic/albumPic_add");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	/**
	 * 添加相片
	 */
	@ResponseBody
	@RequestMapping("/addAlbumPic.do")
	public Map<String, Object> addAlbumPic(AlbumPictureDto albumPictureDto, HttpSession session){
		logger.info("AlbumPictureDto"+albumPictureDto);
        Integer status = 0;
		if(StringUtils.isBlank(albumPictureDto.getPic())){
			String[] urls=albumPictureDto.getPics().split(",");
			for(int i = 0;i < urls.length;i++){
				String imgurl = urls[i];
				albumPictureDto.setPic(imgurl);
				status = iAlbumPicService.addAlbumPic(albumPictureDto);
			}
		}else{
			return MessageResp.getMessage(false,"未上传图片！");
		}
		if(status == 0){
			return MessageResp.getMessage(false,"添加相片失败！");
		}
		return MessageResp.getMessage(true,"添加相片成功！");
	}
	
	/**
	 * 去修改页面页面
	 */
	@RequestMapping(value="/goAlbumPicEdit")
	public ModelAndView goEdit(String id){
		logger.info("去相片修改页面");
		ModelAndView mv = this.getModelAndView(this.getSessionUserId());
		AlbumPictureDto dto = new AlbumPictureDto();
		dto.setId(id);
		AlbumPictureDto result =iAlbumPicService.selectAlbumPic(dto);
		List<HomeMenuDto> list = iHomeMenuService.findAll();
		mv.addObject("menuTypes", list);
		try {
			mv.setViewName("/albumPic/albumPic_edit");
			mv.addObject("msg", "update");
			mv.addObject("pd", result);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	
	/**
	 * 修改相片
	 * @author YIn
	 * @time:2015年11月13日 下午5:53:30
	 */
	
	@ResponseBody
	@RequestMapping("/updateAlbumPic.do")
	public Map<String, Object> updateAlbum(AlbumPictureDto albumPictureDto){
		logger.info("AlbumPictureDto"+albumPictureDto);
		int status = iAlbumPicService.updateAlbumPic(albumPictureDto);
		if(status == 0){
			return MessageResp.getMessage(false,"更新相片失败！");
		}
		return MessageResp.getMessage(true,"更新相片成功！");
	}
	
	/**
	 * 删除相片
	 * @author YIn
	 * @time:2015年11月13日 下午5:53:30
	 */
	@ResponseBody
	@RequestMapping("/delAlbumPic.do")
	public Map<String, Object> delAlbumPic(AlbumPictureDto albumPictureDto){
		logger.info("AlbumPictureDto"+albumPictureDto);
		int status = iAlbumPicService.delAlbumPic(albumPictureDto.getId());
		if(status == 0){
			return MessageResp.getMessage(false,"删除相片失败！");
		}
		return MessageResp.getMessage(true,"删除相片成功！");
	}
	
	/**
	 * 批量删除
	 * @author YIn
	 * @time:2015年11月16日 下午3:14:51
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/delByIds.do")
    public Map<String, Object> delByIds(String  ids) throws Exception{
	    if (StringUtils.isBlank(ids)) {
	        return MessageResp.getMessage(false, "请选择需要删除的项！");
	    }
	    Integer resObject =(Integer) iAlbumPicService.delByIds(ids);
	    if (resObject == 0) {
            return MessageResp.getMessage(false, "无此条记录！");
        }
	    if (resObject == 1) {
            return MessageResp.getMessage(true, "删除成功！");
        }
	    return MessageResp.getMessage(false, "未知错误！");
	}
	
	/**
	 * 分页查询相片(连接查询)
	 * @author YIn
	 * @time:2015年11月13日 下午5:53:30
	 */
	@RequestMapping(value = "findAlbumPicByPage.do")
	public ModelAndView findAlbumPicByPage(AlbumPictureDto query, ExtPageQuery page) {
		logger.info("AlbumPictureDto query : " + query);
		PageResult<AlbumPictureDto> result = iAlbumPicService.findAlbumPicPage(query,
				page.changeToPageQuery());
		ModelAndView mv = this.getModelAndView(this.getSessionUserId());
		List<HomeMenuDto> list = iHomeMenuService.findAll();
		mv.addObject("menuTypes", list);
		List<AlbumDto> albumList = iAlbumService.findAlbum(new AlbumDto());
		mv.addObject("auditTypes", AuditStatusEnums.getValus());
		mv.addObject("allAlbum", albumList);
		mv.addObject("pageList", result);
		mv.addObject("query", query);
		mv.setViewName("/albumPic/albumPic_list");
		return mv;
	}
	
   /**
    * @Title: updateOrder
    * @Description: TODO(更新照片排序)
    * @param @param homeMenuDto
    * @param @return    设定文件
    * @return Map<String,Object>    返回类型
    * @throws
    */
    @RequestMapping("/updateOrder")
    @ResponseBody
    public Map<String, Object> updateOrder(AlbumPictureDto albumPictureDto) {
        Integer result = iAlbumPicService.updateAlbumPic(albumPictureDto);
        if (result == 1) {
            return MessageResp.getMessage(true, "更新照片排序成功");
        }
        return MessageResp.getMessage(false, "更新照片排序失败");
    }
}
