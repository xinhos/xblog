import cn.xinhos.Main;
import cn.xinhos.service.BlogService;
import cn.xinhos.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/*
 * @ClassName: ServiceTest
 * @Description: 测试服务层代码
 * @author: xinhos
 * @data: 2021-05-20-12:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class ServiceTest {
    @Resource private BlogService blogService;
    @Resource private CategoryService categoryService;

    @Test public void testGetRedis() {
        System.out.println(blogService.mainPageArticle());
    }

    @Test public void testGetCategory() {
        System.out.println(categoryService.getCategoryInfo());
    }

    @Test public void testGetTopic() {
        System.out.println(categoryService.getTopicInfo(1));
    }
}
