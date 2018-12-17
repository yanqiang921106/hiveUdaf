package com.startdt.dadong.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-12 14:15
 * @Description:
 */
public class Configure {

    //    private static final Log log = LogFactory.getLog(ServerConfig.class);
    private static Properties config = null;
    private static String filePath = "conf.properties";
    static {
        config = new Properties();
        try {
            ClassLoader CL = Object.class.getClassLoader();
            InputStream in;
            if (CL != null) {
                in = CL.getResourceAsStream(filePath);
            }else {
                in = ClassLoader.getSystemResourceAsStream(filePath);
            }
            config.load(in);
            //    in.close();
        } catch (FileNotFoundException e) {
            //    log.error("服务器配置文件没有找到");
            System.out.println("服务器配置文件没有找到");
        } catch (Exception e) {
            //    log.error("服务器配置信息读取错误");
            System.out.println("服务器配置信息读取错误");
        }
    }


    public static String getValue(String key) {
        if (config.containsKey(key)) {
            String value = config.getProperty(key);
            return value;
        }else {
            return "";
        }
    }

    public static int getValueInt(String key) {
        String value = getValue(key);
        int valueInt = 0;
        try {
            valueInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return valueInt;
        }
        return valueInt;
    }
    public static void main(String[] args) {
        //Configure config = new Configure("conf.properties");
        System.out.println(Configure.getValue("excelPath"));
    }
}
