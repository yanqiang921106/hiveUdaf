package com.startdt.dadong.datax;

import java.util.List;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-11 20:45
 * @Description:
 */
public class ParameterWriter {
    public String odpsServer = "http://service.odps.aliyun.com/api";
    public String tunnelServer = "http://dt.odps.aliyun.com";
    public String partition = "";
    public String truncate = "false";
    public String datasource = "odps_first";
    public List<String> column;
    public String table = "";
}
