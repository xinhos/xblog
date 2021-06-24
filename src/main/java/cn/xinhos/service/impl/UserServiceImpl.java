package cn.xinhos.service.impl;

import cn.xinhos.entry.User;
import cn.xinhos.entry.dto.SiteInfoDto;
import cn.xinhos.service.PropertyService;
import cn.xinhos.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource private PropertyService propertyService;

    /* 管理员登录 */
    @Override public boolean checkAdmin(User user) {
        SiteInfoDto siteInfoDto = propertyService.getSiteInfo();
        return siteInfoDto.getAdminName().equalsIgnoreCase(user.getName()) &&
                siteInfoDto.getPassword().equalsIgnoreCase(user.getPassword());
    }
}
