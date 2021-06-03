package cn.xinhos.service.impl;

import cn.xinhos.dao.property.SiteProperty;
import cn.xinhos.entry.dto.SiteInfoDto;
import cn.xinhos.service.PropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/*
 * @ClassName: PropertyFileServiceImpl
 * @Description: 操作属性文件的服务（读写属性文件，TODO 部分属性在服务拆分后应该保存到数据库中）
 * @author: xinhos
 * @data: 2021-05-22-20:29
 */
@Service("propertyService")
public class PropertyServiceImpl implements PropertyService {
    @Resource private SiteProperty siteProperty;

    private volatile SiteInfoDto siteInfoDto = null;

    @Override public SiteInfoDto getSiteInfo() {
        if (siteInfoDto == null) {
            synchronized (PropertyServiceImpl.class) {
                if (siteInfoDto == null) {
                    siteInfoDto = new SiteInfoDto(siteProperty);
                }
            }
        }
        return siteInfoDto;
    }
}
