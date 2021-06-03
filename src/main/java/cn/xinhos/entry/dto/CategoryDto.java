package cn.xinhos.entry.dto;

import cn.xinhos.entry.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

/*
 * @ClassName: CategoryDto
 * @Description: 返回给页面的Category对象。
 * @author: xinhos
 * @data: 2021-05-21-22:27
 */
@NoArgsConstructor @AllArgsConstructor
@Data public class CategoryDto extends Category{
    private Integer articleNum; // 该分类下的文章数。
    private List<CategoryDto> topics;

    public CategoryDto(Category category) {
        this.setId(category.getId());
        this.setName(category.getName());
        this.setParentId(category.getParentId());
        this.setCreateDate(category.getCreateDate());
    }

    public CategoryDto(Category category, List<CategoryDto> topics, int articleNum) {
        this(category);
        this.topics = topics;
        this.articleNum = articleNum;
    }
}
