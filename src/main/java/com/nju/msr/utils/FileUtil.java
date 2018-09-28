package com.nju.msr.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void appendContentToFile(String fileName, String content) {
        addContentToFile(fileName,content,true);
    }

    public static void addContentToFile(String fileName, String content, boolean append) {
        File file = new File(fileName);

        if (!file.exists()){
            try {
                File fileParent = file.getParentFile();
                if(fileParent!=null && !fileParent.exists()){
                    fileParent.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, append);
            writer.write(content);
            if (!content.endsWith("\n")) {
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
