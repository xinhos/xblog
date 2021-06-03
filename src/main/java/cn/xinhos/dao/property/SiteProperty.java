package cn.xinhos.dao.property;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * @ClassName: SiteInfoProperty
 * @Description: 访问网站信息的接口。
 * @author: xinhos
 * @data: 2021-05-22-12:17
 */
@Component("siteProperty")
@PropertySource(encoding = "utf-8",
        value = "classpath:/config/XBlog-${spring.profiles.active}.properties")
@Data public class SiteProperty {
    @Value("${website.userName}")
    private String userName;

    @Value("${website.userHeader}")
    private String userHeader;

    @Value("${website.motto}")
    private String motto;

    @Value("${website.mainPageArticleSize}")
    private Integer mainPageArticleSize;

    @Value("${website.categoryPageSize}")
    private Integer categoryPageSize;

    @Value("#{'${website.singlePages.codes}'.split(',')}")
    private List<String> codes;

    @Value("#{'${website.singlePages.titles}'.split(',')}")
    private List<String> titles;
}

