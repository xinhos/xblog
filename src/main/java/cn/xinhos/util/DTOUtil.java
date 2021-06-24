package cn.xinhos.util;

import cn.xinhos.entry.Blog;
import cn.xinhos.entry.dto.BlogDto;
import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.Source;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/*
 * @ClassName: DTOUtil
 * @Description: 将entry转换为对应的DTO并返回
 * @author: xinhos
 * @data: 2021-06-22-9:54
 */
@Slf4j
public class DTOUtil {
    /* 传入源类型实例src、目标类型，返回根据源实例转换后的目标类型实例 */
    public static <T> T transfer(Object src, Class<T> DTOType) {
        // 获取src所有声明了的字段，判断DTOType中是否具有同名字段，若有，则调用DTO相应的setter
        T dto = null;
        Callable
        try {
            dto = DTOType.getConstructor().newInstance();
            Field[] srcFields = src.getClass().getDeclaredFields();
            Set<String> dtoFields = Arrays.stream(DTOType.getDeclaredFields())
                    .map(Field::getName).collect(toSet());
            Class pClass = DTOType.getSuperclass();
            while (pClass != null && pClass != Object.class) {
                dtoFields.addAll(Arrays.stream(pClass.getDeclaredFields()).map(Field::getName)
                        .collect(Collectors.toList()));
                pClass = pClass.getSuperclass();
            }

            for (Field field : srcFields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(src);
                if (dtoFields.contains(fieldName) && value != null) {
                    Method m = DTOType.getMethod(setter(fieldName), value.getClass());
                    if (m.canAccess(dto)) {
                        m.invoke(dto, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return dto;
    }

    /* 将字段的第一个字母转为大写字母 */
    private static String upperCapture(String filed) {
        char[] chars = filed.toCharArray();
        if (chars.length == 0) return "";
        if ('A' < chars[0] && chars[0] < 'Z') return filed;
        chars[0] -= 32;
        return String.valueOf(chars);
    }

    private static String setter(String fieldName) {
        return "set" + upperCapture(fieldName);
    }

    private static String getter(String fieldName) {
        return "get" + upperCapture(fieldName);
    }

    public static void main(String[] args) {
        Blog blog = new Blog();
        blog.setId(1);
        blog.setAbstracts("abcdefg");
        blog.setCover("file://C:test.jpg");
        blog.setFileName("测试文件");
        blog.setAbstracts("摘要占位");
        blog.setTitle("测试标题");
        System.out.println(DTOUtil.transfer(blog, BlogDto.class));
    }
}
