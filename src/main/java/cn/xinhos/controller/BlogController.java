package cn.xinhos.controller;

import cn.xinhos.entry.dto.BlogDto;
import cn.xinhos.service.BlogService;
import cn.xinhos.service.CategoryService;
import cn.xinhos.service.PropertyService;
import cn.xinhos.util.CONS;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class BlogController {
    @Resource private BlogService blogService;
    @Resource private CategoryService categoryService;
    @Resource private PropertyService propertyService;

    /* 返回主页，主页中包含一页博客信息、category（大类）信息 */
    @GetMapping("/blog")
    public ModelAndView initMainPage(){
        ModelAndView mv = new ModelAndView("blog");
        mv.addObject("blogs", blogService.mainPageArticle());
        mv.addObject("categorys", categoryService.getCategoryInfo());
        mv.addObject("siteInfo", propertyService.getSiteInfo());
        mv.addObject("pageInfo", blogService.getPageInfo(CONS.PT_MAIN));
        return mv;
    }

    /* 返回首页的第page页信息 */
    @GetMapping("/blog/page/{targetPage}")
    @ResponseBody public List<BlogDto> mainPageBlog(@PathVariable Integer targetPage) {
        return blogService.mainPageArticle(targetPage);
    }

    // TODO 为了用URL可以分享文章，所以不得不让文章接口返回单独的页面，日后或用vue实现（要大改，S1、S2是共用的。）
    /* 返回id为blogId博客的文章页面 */
    @GetMapping("/blog/article/{blogId}")
    public ModelAndView initArticlePage(@PathVariable Integer blogId) {
        ModelAndView mv = new ModelAndView("article");
        BlogDto blogDto = blogService.getBlog(blogId);
        mv.addObject("blog", blogDto);
        mv.addObject("content", blogService.getBlogContent(blogDto));
        mv.addObject("siteInfo", propertyService.getSiteInfo());
        mv.addObject("categorys", categoryService.getCategoryInfo());
        return mv;
    }

    /* 获取category（大类）下的信息，包括该大类下的topic list、短博客 */
    @GetMapping("/category/{categoryId}/topic")
    @ResponseBody public Map<String, Object> getCategoryInfo(@PathVariable Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        result.put("topicList", categoryService.getTopicInfo(categoryId));
        result.put("shortBlogList", blogService.getCategoryArticle(categoryId, 1));
        result.put("pageInfo", blogService.getPageInfo(CONS.PT_CATEGORY, categoryId, 1));
        result.put("code", CONS.GET_SUCCESS.value);
        return result;
    }

    /* 获取category或topic下的第targetPage页博客 */
    @GetMapping("/category/{id}/page/{targetPage}/{flag}")
    @ResponseBody public Map<String, Object> getCategoryBlog(@PathVariable Integer id,
                                                       @PathVariable Integer targetPage,
                                                       @PathVariable Character flag) {
        Map<String, Object> result = new HashMap<>();
        List<BlogDto> shortBlogList = null;
        CONS sizeType = null;

        if (Objects.equals(flag, 'c')) {
            shortBlogList = blogService.getCategoryArticle(id, targetPage);
            sizeType = CONS.PT_CATEGORY;
        } else {
            shortBlogList = blogService.getTopicArticle(id, targetPage);
            sizeType = CONS.PT_TOPIC;
        }
        result.put("shortBlogList", shortBlogList);
        result.put("pageInfo", blogService.getPageInfo(sizeType, id, targetPage));
        result.put("code", CONS.GET_SUCCESS.value);
        return result;
    }
}
