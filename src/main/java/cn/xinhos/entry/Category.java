package cn.xinhos.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

/*
 * @ClassName: Category
 * @Description: 对category表的映射
 * @author: xinhos
 * @data: 2021-05-21-22:19
 */
@NoArgsConstructor @AllArgsConstructor
@Data public class Category {
    private Integer id;
    private String name;
    private Integer parentId;
    private Date createDate;
}
