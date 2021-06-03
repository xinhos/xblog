package cn.xinhos.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @ClassName: Mark
 * @Description: 博客标签（映射标签表）
 * @author: xinhos
 * @data: 2021-05-21-21:08
 */
@NoArgsConstructor @AllArgsConstructor
@Data  public class Mark {
    private Integer id;
    private String describe;
    private String value;
}
