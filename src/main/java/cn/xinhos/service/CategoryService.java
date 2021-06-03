package cn.xinhos.service;

import cn.xinhos.entry.Category;
import cn.xinhos.entry.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    /*
     * 查询所有分类（单用户下查询全部目录，多用户时根据用户id来查找）
     * 并构建出层级关系，返回第一层。
     */
    List<CategoryDto> getCategoryInfo();
    List<CategoryDto> getTopicInfo(int parentId);
}
