import cn.xinhos.Main;
import cn.xinhos.dao.mapper.BlogMapper;
import cn.xinhos.dao.property.SiteProperty;
import cn.xinhos.entry.Blog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.concurrent.CountDownLatch;

/*
 * @ClassName: DaoTest
 * @Description: 测试数据访问接口
 * @author: xinhos
 * @data: 2021-05-22-19:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class DaoTest {
    @Resource SiteProperty siteProperty;
    @Resource BlogMapper blogMapper;

    @Test public void testProperty() {
        System.out.println(siteProperty);
    }

    @Test public void readMd() {
        Blog blog = new Blog();
        String path = "C:\\Users\\xinhos\\Desktop\\test\\main.md";
        StringBuilder content = new StringBuilder();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tmp = null;
            while ((tmp = reader.readLine()) != null) {
                content.append(tmp);
                content.append("\\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        blog.setId(4);
        blog.setAbstracts(content.toString());
        System.out.println(content);
        blogMapper.updateBlogById(blog);
    }

    @Test public void readMdMutil() {
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(100);
        String path1 = "C:\\Users\\xinhos\\Desktop\\test\\main.md";
        for (int i = 0; i < 100; i++) {
            final int tmpi = i;
            Thread thread = new Thread(() -> {
                System.out.println(tmpi);
                System.out.println(read(path1));;
                latch.countDown();
            });
            thread.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private String read(String path) {
        StringBuilder content = new StringBuilder();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tmp = null;
            while ((tmp = reader.readLine()) != null) {
                content.append(tmp);
                content.append("\\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
