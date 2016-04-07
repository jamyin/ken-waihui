package com.tianfang.dto;

import com.tianfang.admin.dto.HomeMenuDto;
import com.tianfang.business.dto.AlbumPictureDto;
import java.io.Serializable;
import java.util.List;

/**
 * <p>Description: 封装子级菜单和图片集合 </p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/7 上午11:52
 */
public class SubMenu implements Serializable{

    private HomeMenuDto menu;

    private List<AlbumPictureDto> pics;

    public HomeMenuDto getMenu() {
        return menu;
    }

    public void setMenu(HomeMenuDto menu) {
        this.menu = menu;
    }

    public List<AlbumPictureDto> getPics() {
        return pics;
    }

    public void setPics(List<AlbumPictureDto> pics) {
        this.pics = pics;
    }
}