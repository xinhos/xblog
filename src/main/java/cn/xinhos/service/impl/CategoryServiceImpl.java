package cn.xinhos.service.impl;

import cn.xinhos.dao.mapper.BlogMapper;
import cn.xinhos.entry.Category;
import cn.xinhos.entry.dto.CategoryDto;
import cn.xinhos.dao.mapper.CategoryMapper;
import cn.xinhos.service.CategoryService;
import cn.xinhos.util.CONS;
import cn.xinhos.util.RedisName;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/*
 * @ClassName: CategoryServiceImpl
 * @Description: 对目录的各种操作
 * @author: xinhos
 * @data: 2021-05-21-22:10
 */
@Service(value = "categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Resource private CategoryMapper categoryMapper;
    @Resource private BlogMapper blogMapper;
    @Resource private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<CategoryDto> getCategoryInfo() {
        return selectCategory(0);
    }

    @Override
    public List<CategoryDto> getTopicInfo(int parentId) {
        List<CategoryDto> categoryDtos = selectCategory(parentId);
        categoryDtos.forEach(c -> c.setArticleNum(blogMapper.countBlog(c.getId())));
        return categoryDtos;
    }

    // TODO redis中改为用hash存，且存取DTO对象
    /* 获取parent_id下的分类信息，0代表获取category，非0表要获取topic的parent_id（获取topic） */
    private List<CategoryDto> selectCategory(int parentId) {
        BoundListOperations<String, Object> lOps =
                redisTemplate.boundListOps(RedisName.categoryInfoKey(parentId));

        List<Category> categories = null;
        if (lOps.size() != 0) {
            categories = lOps.range(0, -1).stream().map(c -> (Category)c).collect(toList());
        } else {
            categories = categoryMapper.selectCategoryByParentId(parentId);
            if (categories.size() == 0) {
                return new ArrayList<>();
            }
            lOps.rightPushAll(categories.toArray(new Category[0]));
        }

        return categories.stream().map(CategoryDto::new).collect(toList());
    }

    /* 根据层级关系，将所给的Category列表构建成一颗Category树，层级关系可以由parent_id得到.
     * 当category为空时返回空集，否则返回一个根节点集合（parent_id为0） */
    private List<CategoryDto> buildCategoryTree(List<Category> categorys) {
        if (categorys == null) return new ArrayList<>();

        Map<Integer, List<CategoryDto>> table = categorys.stream()
                .map(CategoryDto::new)
                .collect(groupingBy(CategoryDto::getParentId));
        List<CategoryDto> result = table.get(0);
        result.forEach(c -> c.setTopics(table.get(c.getId())));
        return result;
    }
}
