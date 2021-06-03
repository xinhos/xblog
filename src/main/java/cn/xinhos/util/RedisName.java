package cn.xinhos.util;

/*
 * @ClassName: RedisName
 * @Description: 保存了实体在redis中的key
 * @author: xinhos
 * @data: 2021-05-25-15:15
 */
public class RedisName {
    public static String categoryInfoKey(int parentId) {
        return CONS.RN_CATEGORY_INFO.value + "_" + parentId;
    }

    public static String categoryBlogKey(int id) {
        return CONS.RN_CATEGORY_BLOG.value + "_" + id;
    }
}
