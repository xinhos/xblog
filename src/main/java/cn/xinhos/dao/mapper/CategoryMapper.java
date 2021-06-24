package cn.xinhos.dao.mapper;

import cn.xinhos.entry.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface CategoryMapper {

    /* 查询所有分类（单用户下查询全部目录，多用户时根据用户id来查找） */
    @Results(id = "category", value = {
            @Result(property = "id",         column = "id"),
            @Result(property = "name",       column = "name"),
            @Result(property = "parentId",   column = "parent_id"),
            @Result(property = "createDate", column = "create_date")
    })
    @Select("select id, name, parent_id, create_date from category")
    List<Category> selectAllData();

    @ResultMap("category")
    @Select("select id, name, parent_id, create_date from category where parent_id = #{parentId}")
    List<Category> selectCategoryByParentId(int parentId);
}
