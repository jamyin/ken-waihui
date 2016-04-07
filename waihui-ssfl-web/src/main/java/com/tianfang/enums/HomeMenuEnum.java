package com.tianfang.enums;

/**
 * <p>Description: 外烩官网菜单与后台id对应枚举封装 </p>
 * <p>Company: 上海天坊信息科技有限公司</p>
 *
 * @author wangxiang
 * @version 1.0
 * @time 16/4/6 下午1:44
 */
public enum HomeMenuEnum {
    INDEX("4c8d507e-de2b-41cc-9667-e35e95914bb4"), // 首页
    BOOK("671a3971-2e80-4481-86f8-3048201764f1"), // 精选菜单
    SERVER("2df6cf99-9083-4eed-b7df-79630bbc7417"), // 外烩服务
    COMPANY("2b106446-321a-4301-9297-e294cd01061c"), // 公司资质
    ACITIVITY("502e343b-c4bc-4522-af54-2c0088c8b6ae"), // 活动案例
    AREA("d493bf67-c8fc-4dfa-9835-3f4d443e5abe"),      // 场地推荐
    CONTACT("aad34553-9009-4242-9672-35f3b45ebd8a"),   // 联系我们
    PARTYTYPE("29ab349b-337d-4cdb-8a26-339d250708ad");  // 宴会类型

    private String id;

    HomeMenuEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
