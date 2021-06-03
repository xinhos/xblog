package cn.xinhos.dao.mapper;

import cn.xinhos.entry.Blog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface BlogMapper {
    @Results(id = "blog",value = {
            @Result(property = "id"          , column = "id", id = true),
            @Result(property = "categoryId"  , column = "category_id"),
            @Result(property = "citeId"      , column = "cite_id"),
            @Result(property = "title"       , column = "title"),
            @Result(property = "abstracts"   , column = "abstract"),
            @Result(property = "cover"       , column = "cover"),
            @Result(property = "publishDate" , column = "publish_date"),
            @Result(property = "modifiedDate", column = "modified_date"),
            @Result(property = "fileName"    , column = "file_name"),
            @Result(property = "visitNum"    , column = "visit_num"),
            @Result(property = "commentNum"  , column = "comment_num"),
            @Result(property = "state"       , column = "state")
    })
    @Select("select * from blog where id = #{id}")
    Blog selectBlogById(int id);

    @ResultMap("blog")
    @Select("select id, title, publish_date " +
            "from blog " +
            "where category_id = #{topicId} " +
            "   order by publish_date " +
            "   limit #{begin}, #{size}")
    List<Blog> selectShortBlogByTopicId(int topicId, int begin, int size);

    @ResultMap("blog")
    @Select("select id, title, publish_date " +
            "from blog " +
            "where category_id in (select id from category where parent_id = #{categoryId}) " +
            "   order by publish_date " +
            "   limit #{begin}, #{size}")
    List<Blog> selectShortBlogByCategoryId(int categoryId, int begin, int size);

    @ResultMap("blog")
    @Select("select * from blog where match(title), against(#{title})")
    List<Blog> selectBlogLikeTitle(String title);

    @Select("select id from blog order by publish_date desc limit #{begin}, #{size}")
    List<Integer> selectBlogIdByPage(int begin, int size);

    @Select("select m.value " +
            "from mark as m inner join blog_to_mark as b2m " +
            "where b2m.mark_id = m.id and b2m.blog_id = #{id}")
    List<String> selectMarkByBlogID(int id);

    @Select("<script>" +
            "select count(id) from blog" +
            "  <where>" +
            "    <if test='topicId != null'>category_id = #{topicId}</if>" +
            "  </where>" +
            "</script>")
    Integer countBlog(Integer topicId);

    @Select("select count(id) from blog " +
            "where category_id " +
            "   in (select id from category where parent_id = #{categoryId})")
    Integer countCategoryBlog(int categoryId);


    @Select("select count(id) from blog where state = #{state}")
    Integer countBlogByState(int state);

    @Update({"<script>",
            "update blog",
            "  <set>",
            "    <if test='blog.title        != null'>title         = #{blog.title},</if>",
            "    <if test='blog.cover        != null'>cover         = #{blog.cover},</if>",
            "    <if test='blog.abstracts    != null'>abstract      = #{blog.abstracts},</if>",
            "    <if test='blog.state        != null'>state         = #{blog.state},</if>",
            "    <if test='blog.modifiedDate != null'>modified_date = #{blog.modifiedDate},</if>",
            "    <if test='blog.commentNum   != null'>comment_num   = #{blog.commentNum},</if>",
            "    <if test='blog.visitNum     != null'>visit_num     = #{blog.visitNum},</if>",
            "    <if test='blog.categoryId   != null'>category_id   = #{blog.categoryId}</if>",
            "  </set>",
            "where id = #{blog.id}",
            "</script>"})
    int updateBlogById(@Param("blog") Blog blog);
}
