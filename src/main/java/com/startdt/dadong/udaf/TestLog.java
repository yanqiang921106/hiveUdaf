package com.startdt.dadong.udaf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestLog {
    static final Log log = LogFactory.getLog(TestLog.class.getName());

    public void showLog(){
        log.info("dfdfdfdfddfdf");
    }

    public static void main(String[] args) {
        System.out.println("aaaaa");
    }
}
