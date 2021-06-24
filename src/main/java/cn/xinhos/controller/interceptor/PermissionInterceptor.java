package cn.xinhos.controller.interceptor;

import cn.xinhos.entry.Token;
import cn.xinhos.util.TokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * @ClassName: PermissionInterceptor
 * @Description: 访问后台时的权限拦截器
 * @author: xinhos
 * @data: 2021-06-16-19:27
 */
@Component
@Slf4j public class PermissionInterceptor implements HandlerInterceptor {

    @Resource private TokenHelper tokenHelper;

    /* 检查请求中或session是否存在token */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        String method = ((HandlerMethod)handler).getMethod().getName();
        String tokenStr1 = request.getHeader("Authorization");
        String tokenStr2 = String.valueOf(request.getSession().getAttribute("token"));

        Token token = tokenHelper.parseToken(tokenStr1);
        boolean passed = true;
        if (token == null && (token = tokenHelper.parseToken(tokenStr2)) == null) {
            response.sendRedirect("/admin/adminLogin");
            passed = false;
        }

        log.info(String.format("【%s】，请求接口：【%s】，处理方法：【%s】，登录状态：【%s登录】",
                ip, path, method, (passed? "已": "未")));

        return passed;
    }
}
