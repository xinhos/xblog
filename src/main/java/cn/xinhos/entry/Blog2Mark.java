package cn.xinhos.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @ClassName: Blog2Mark
 * @Description: 博客-标签类（映射博客-标签表）
 * @author: xinhos
 * @data: 2021-05-21-21:06
 */
@NoArgsConstructor @AllArgsConstructor
@Data public class Blog2Mark {
    private Integer id;
    private Integer blogId;
    private Integer markId;
}
