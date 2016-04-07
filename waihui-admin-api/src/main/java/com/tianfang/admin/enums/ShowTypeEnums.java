package com.tianfang.admin.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jam .Y
 *
 */
public enum ShowTypeEnums implements Serializable{
	ONE("单页 ", 1), TWO("图片轮播 ", 2), three("图片列表", 3), FOUR("新闻资讯", 4);
    
	@Getter
	@Setter
	private String name;
    
	@Getter
	@Setter
	private int index;

    // 构造方法
    private ShowTypeEnums(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (ShowTypeEnums c : ShowTypeEnums.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    
    public static List<ShowTypeEnums> getValus(){
    	ShowTypeEnums[] arr = ShowTypeEnums.values();
    	List<ShowTypeEnums> list = new ArrayList<ShowTypeEnums>(arr.length);
    	for (int i = 0; i < arr.length; i++){
    		list.add(arr[i]);
    	}
    	return list; 
    }
}