package cn.xinhos.service.impl;

import cn.xinhos.dao.property.SiteProperty;
import cn.xinhos.entry.Blog;
import cn.xinhos.entry.dto.BlogDto;
import cn.xinhos.dao.mapper.BlogMapper;
import cn.xinhos.entry.dto.PageInfoDto;
import cn.xinhos.service.BlogService;
import cn.xinhos.util.CONS;
import cn.xinhos.util.RedisName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service("blogService")
@PropertySource(encoding = "utf-8",
        value = "classpath:/config/XBlog-${spring.profiles.active}.properties")
public class BlogServiceImpl implements BlogService {
    @Resource private SiteProperty siteProperty;
    @Resource private BlogMapper blogMapper;
    @Resource private RedisTemplate<String, Object> redisTemplate;
    @Value("${XBlog.articleDir}") private String articleDir;

    /* 获取首页推荐文章
    * 1. 查找redis，redis中存在则直接返回
    * 2. redis中不存在则查询数据库，并缓存到redis中
    * 3. 返回数据 */
    @Override public List<BlogDto> mainPageArticle() {
        HashOperations<String, String, Object> hOps = redisTemplate.opsForHash();
        List<BlogDto> result = new ArrayList<>();
        final String BLOG = CONS.RN_BLOG.value;
        final int targetSize = siteProperty.getMainPageArticleSize();

        // 若缓存中的最新博客不足，则向数据库请求最新博客，并存放到缓存中（最后统一从缓存中拿）
        List<Integer> blogIds = hOps.keys(BLOG).stream()
                .map(Integer::parseInt).collect(toList());
        int curSize = blogIds.size();
        if (curSize < targetSize) {
            List<Integer> newBlogIds = blogMapper.selectBlogIdByPage(0, targetSize);
            // 1. 删除过时的缓存（已经不是最新的了）
            blogIds.stream()
                    .filter(id -> !newBlogIds.contains(id))
                    .forEach(id -> hOps.delete(BLOG, id.toString()));
            // 2. 查询新博客并加入缓存中
            List<Integer> finalBlogIds = blogIds;
            newBlogIds.stream()
                    .filter(id -> !finalBlogIds.contains(id))
                    .forEach(id -> {
                        Blog aBlog = blogMapper.selectBlogById(id);
                        List<String> marks = blogMapper.selectMarkByBlogID(id);
                        BlogDto aBlogDto = new BlogDto(aBlog, marks);
                        hOps.putIfAbsent(BLOG, id.toString(), aBlogDto);
                    });
            blogIds = newBlogIds;
        }
        blogIds.forEach(id -> result.add((BlogDto) hOps.get(BLOG, id.toString())));
        // 将blogs按照时间排序，然后返回。
        result.sort(Comparator.comparing(BlogDto::getModifiedDate));
        return result.stream().limit(targetSize).collect(toList());
    }

    @Override public List<BlogDto> mainPageArticle(Integer targetPage) {
        return null;
    }

    /* 按照分页信息获取某个topic下的博客 */
    @Override public List<BlogDto> getTopicArticle(int topicId, int targetPage) {
        int pageSize = siteProperty.getCategoryPageSize();
        int begin = (targetPage - 1) * pageSize;

        List<Blog> blogs = blogMapper.selectShortBlogByTopicId(topicId, begin, pageSize);
        if (blogs.size() == 0) {
            return new ArrayList<>();
        }

        return blogs.stream().map(this::setDto).collect(toList());
    }

    // TODO 可改为将前3页存储在缓存中
    /* 查询某个category下的博客，若是第一页，则先从缓存中获取 */
    @Override public List<BlogDto> getCategoryArticle(int categoryId, int targetPage) {
        int pageSize = siteProperty.getCategoryPageSize();
        List<BlogDto> result = null;

        if (targetPage == 1) {
            String key = RedisName.categoryBlogKey(categoryId);
            BoundListOperations<String, Object> lOps = redisTemplate.boundListOps(key);
            final int targetSize = siteProperty.getCategoryPageSize();
            long cacheSize = lOps.size();
            if (cacheSize >= targetSize) {
                return lOps.range(0, -1).stream().map(e -> (BlogDto)e).collect(toList());
            }

            result = selectShortBlogDtoByCategoryId(categoryId, targetPage, pageSize);
            if (result.size() != cacheSize) {
                redisTemplate.delete(key);
                lOps.rightPushAll(result);
            }
            return result;
        }

        return selectShortBlogDtoByCategoryId(categoryId, targetPage, pageSize);
    }

    /* 获取首页分页信息 */
    @Override public PageInfoDto getPageInfo(CONS type) {
        if (type != CONS.PT_MAIN) {
            return null;
        }

        int dataSize = blogMapper.countBlog(null);
        int pageSize = siteProperty.getMainPageArticleSize();
        int pageNum = (dataSize + pageSize - 1) / pageSize;
        return new PageInfoDto(dataSize, pageSize, 1, pageNum);
    }

    /* 获取前端所需要的不同分页信息（2种：category分页信息、topic分页信息） */
    @Override public PageInfoDto getPageInfo(CONS type, Integer id, int targetPage) {
        int dataSize = 0;
        int pageSize = 0;
        switch (type) {
            case PT_CATEGORY:
                dataSize = blogMapper.countCategoryBlog(id);
                pageSize = siteProperty.getCategoryPageSize();
                break;
            case PT_TOPIC:
                dataSize = blogMapper.countBlog(id);
                pageSize = siteProperty.getCategoryPageSize();
                break;
        }
        int pageNum = (dataSize + pageSize - 1) / pageSize;
        return new PageInfoDto(dataSize, pageSize, targetPage, pageNum);
    }

    /* 读取博客文件内容，转成字符串返回 */
    @Override public String getBlogContent(BlogDto blogDto) {
        String path = Paths.get(articleDir, blogDto.getCategoryId().toString(),
                blogDto.getFileName()).toString();

        return "";
    }

    /* 根据ID返回博客DTO */
    @Override public BlogDto getBlog(Integer blogId) {
        return new BlogDto().reset(blogMapper.selectBlogById(blogId));
    }

    private List<BlogDto> selectShortBlogDtoByCategoryId(int categoryId, int begin, int pageSize) {
        return blogMapper.selectShortBlogByCategoryId(categoryId, begin, pageSize).stream()
                .map(this::setDto).collect(toList());
    }
    
    private BlogDto setDto(Blog blog) {
        BlogDto dto = new BlogDto(blog);
        dto.setMarks(blogMapper.selectMarkByBlogID(blog.getId()));
        return dto;
    }
}
