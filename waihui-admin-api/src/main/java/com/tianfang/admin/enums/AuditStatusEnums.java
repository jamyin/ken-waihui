package com.tianfang.admin.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jam
 *
 */
public enum AuditStatusEnums implements Serializable{
	ONE("待审核", 0), TWO("审核通过 ", 1), three("审核不通过", 2);
    
	@Getter
	@Setter
	private String name;
    
	@Getter
	@Setter
	private int index;

    // 构造方法
    private AuditStatusEnums(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (AuditStatusEnums c : AuditStatusEnums.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    
    public static List<AuditStatusEnums> getValus(){
    	AuditStatusEnums[] arr = AuditStatusEnums.values();
    	List<AuditStatusEnums> list = new ArrayList<AuditStatusEnums>(arr.length);
    	for (int i = 0; i < arr.length; i++){
    		list.add(arr[i]);
    	}
    	return list; 
    }
}