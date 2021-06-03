package cn.xinhos.util;

import org.springframework.stereotype.Component;

import java.io.*;

/*
 * @ClassName: FileHelper
 * @Description: 存取博客文件
 * @author: xinhos
 * @data: 2021-05-30-16:25
 */
@Component("fileHelper")
public class FileHelper {
    public String readFileBIO(String path) {
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

    public String readFileNIO(String path) {
        StringBuilder content = new StringBuilder();

        return content.toString();
    }
}
