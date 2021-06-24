package cn.xinhos.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CONS {
    RN_BLOG(1, "mainPageBlog"),          // redis name，数据保存在redis中的key
    RN_CATEGORY_INFO(2, "categoryInfo"), // 存储子分类信息
    RN_CATEGORY_BLOG(3, "categoryBlog"), // 存储分类下的博客信息

    GET_SUCCESS(11, "GET_DATA_OK"),
    GET_ERROR(21, "GET_DATA_ERROR"),

    PT_CATEGORY(31, "category"),         // PT：page type，分页类型
    PT_TOPIC(32, "topic"),
    PT_MAIN(33, "main"),

    CODE_LOGIN_FAIL(40, "M0001"),
    CODE_LOGIN_EXPIRE(41, "M0002");
    public final Integer id;
    public final String value;
}
