package com.tianfang.business.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
/**
 * @author YIn
 * @time:2015年11月13日 下午5:59:20
 * @Fields serialVersionUID : TODO
 */
public class AlbumPictureDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
    private String id;

	@Getter
	@Setter
    private String albumId;

	@Getter
	@Setter
    private String pic;
	
	@Getter
	@Setter
	private String shadowPic;       //阴影图地址

	@Getter
	@Setter
    private Date lastUpdateTime;

	@Getter
	@Setter
    private Date createTime;
	
	@Getter
	@Setter
	private String createTimeStr;    //页面显示时间
	
	@Getter
	@Setter
	private String lastUpdateTimeStr;    //页面显示修改时间 

	@Getter
	@Setter
    private Integer stat;

	@Getter
	@Setter
    private String teamId;
	
	@Getter
	@Setter
	private String teamName; //所属球队名
	
	@Getter
	@Setter
    private String pics;   //相片Url数组
	
	@Getter
	@Setter
    private String title;   //中文标题
	
	@Getter
	@Setter
	private String enTitle;  //英文标题 
	
	@Getter
	@Setter
    private Integer limit;   //查询数量处理
	
	@Getter
	@Setter
	private String[] pictureList;
	
	@Getter
	@Setter
	private String described;   //中文描述
	
	@Getter
	@Setter
	private String enDescribe;   //英文描述
	
	@Getter
	@Setter
	private String userId; //对应的用户Id
	
	@Getter
	@Setter
	private Integer randNumber; //随机数
	
	@Getter
	@Setter
	private Integer picStatus; //图片审核状态 :0.待审核   1.审核通过 2.审核不通过
	
	@Getter
	@Setter
	private Integer picType; //相片类型（4：教练员培训）
	
	@Getter
	@Setter	
    private String menuType; //菜单类型
	
	@Getter
	@Setter
	private Integer orderNo; //照片排序
	
}