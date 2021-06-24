package cn.xinhos.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * @ClassName: FileHelper
 * @Description: 存取博客文件
 * @author: xinhos
 * @data: 2021-05-30-16:25
 */
@Component("fileHelper")
public class FileHelper {
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public String readFileBIO(String path) {
        Future<String> future = executorService.submit(() -> {
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
                return "open file error.";
            }
            return content.toString();
        });

        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return "read file error.";
        }
    }

    public String readFileNIO(String path) {
        StringBuilder content = new StringBuilder();

        return content.toString();
    }

    public static String encode(String content) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String content) {
        return new String(Base64.getDecoder().decode(content), StandardCharsets.UTF_8);
    }
}
