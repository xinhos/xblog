package cn.xinhos.service;

import cn.xinhos.entry.dto.BlogDto;
import cn.xinhos.entry.dto.PageInfoDto;
import cn.xinhos.util.CONS;

import java.util.List;

public interface BlogService {
    List<BlogDto> mainPageArticle();

    List<BlogDto> mainPageArticle(Integer targetPage);

    List<BlogDto> getTopicArticle(int topicId, int curPage);

    List<BlogDto> getCategoryArticle(int categoryId, int curPage);

    PageInfoDto getPageInfo(CONS type);

    PageInfoDto getPageInfo(CONS type, Integer id, int targetPage);

    String getBlogContent(BlogDto blogDto);

    BlogDto getBlog(Integer blogId);
}
