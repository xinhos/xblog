package cn.xinhos.config;

import cn.xinhos.controller.interceptor.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/*
 * @ClassName: WebMvcConfig
 * @Description: web mvc配置文件
 * @author: xinhos
 * @data: 2021-06-17-17:06
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource private PermissionInterceptor permissionInterceptor;

    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }
}
