package com.startdt.dadong.utils;

/**
 * Created by Administrator on 2018\4\11 0011.
 */

import com.startdt.dadong.ddl.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-14 21:05
 * @Description:
 */
public class FileReader {

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static List<String> readFileByLines(String fileName) {
        List<String> list = new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new java.io.FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                if(tempString.length() > 0) {
                    list.add(tempString);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;
    }

    /**
     * 获取目录下所有文件名
     *
     * @param dirPath
     * @return
     */
    public static List<String> readDir(String dirPath) {
        List<String> list = new ArrayList<>();
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String prefix = file.getName().substring(file.getName().lastIndexOf(".")+1);
                    if (file.isFile() && "sql".equals(prefix)) {
                        System.out.println("fileName=" + file.getAbsolutePath());
                        list.add(file.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("该路径不是一个目录");
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        String dirPath = "/Users/yanchuan/Desktop/tet";
        List<String> list = readDir(dirPath);
        for (String filePath : list) {

        }
        String filePath = "/Users/yanchuan/Desktop/Druid";
//        List<String> list = readTxtFileIntoStringArrList("/Users/yanchuan/Desktop/ddl.sql");
//        for(String str : list) {
//            System.out.println(str);
//        }
        readDir(filePath);
        //readFileByLines(filePath);
    }
}



