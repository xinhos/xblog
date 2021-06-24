package cn.xinhos.controller.admin;

import cn.xinhos.entry.Token;
import cn.xinhos.entry.User;
import cn.xinhos.service.UserService;
import cn.xinhos.util.CONS;
import cn.xinhos.util.TokenHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("admin")
public class LoginController {
    @Resource private UserService userService;
    @Resource private TokenHelper tokenHelper;

    /* 初始化后台主页 */
    @GetMapping("")
    public ModelAndView initMain(ModelAndView mv) {
        mv.setViewName("main");
        return mv;
    }

    /* 初始化登录页面 TODO 对于已经登录了的用户，当它再次访问登录页时，应该直接转发*/
    @GetMapping("/login")
    public ModelAndView initLogin(ModelAndView mv) {
        mv.setViewName("adminLogin");
        return mv;
    }

    /* 用户登录验证。若勾选了30天免登录，则生成并返回token。 */
    @PostMapping("/login")
    public ModelAndView login(User user, Boolean keepLogin) {
        ModelAndView mv = new ModelAndView();

        if (userService.checkAdmin(user)) {
            user.setId(0);
            // 这里token会放到session中，重定向完成后将即可从session中删除。（拦截器中需要判断token）
            mv.addObject("token", tokenHelper.createToken(user, true));
            mv.addObject("adminer", user);
            mv.setViewName("redirect:/admin");
        } else {
            mv.addObject("code", CONS.CODE_LOGIN_FAIL.value);
            mv.setViewName("adminLogin");
        }

        return mv;
    }
}
