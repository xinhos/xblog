package cn.xinhos.entry.dto;

import cn.xinhos.dao.property.SiteProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/*
 * @ClassName: SiteInfoDto
 * @Description: 站点信息
 * @author: xinhos
 * @data: 2021-05-23-15:58
 */
@NoArgsConstructor @AllArgsConstructor
@Data public class SiteInfoDto {
    private String userName;
    private String userHeader;
    private String motto;
    private Integer mainPageArticleSize;
    private List<String> codes;
    private List<String> titles;
    private String adminName;
    private String password;
    private Integer userId;
    private String secretKey;
    private Integer keepLoginNum;

    public SiteInfoDto(SiteProperty siteProperty) {
        this.userName = siteProperty.getUserName();
        this.userHeader = siteProperty.getUserHeader();
        this.motto = siteProperty.getMotto();
        this.mainPageArticleSize = siteProperty.getMainPageArticleSize();
        this.codes = siteProperty.getCodes();
        this.titles = siteProperty.getTitles();
        this.adminName = siteProperty.getAdminName();
        this.password = siteProperty.getPassword();
        this.userId = siteProperty.getUserId();
        this.secretKey = siteProperty.getSecretKey();
        this.keepLoginNum = siteProperty.getKeepLoginNum();
    }
}
