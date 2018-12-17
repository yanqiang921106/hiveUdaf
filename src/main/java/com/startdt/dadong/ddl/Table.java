package com.startdt.dadong.ddl;

import java.util.List;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-14 17:48
 * @Description:
 */
public class Table {
    String tableN;
    String tableCN;
    List<Column> list;

    public String getTableN() {
        return tableN;
    }

    public void setTableN(String tableN) {
        this.tableN = tableN;
    }

    public String getTableCN() {
        return tableCN;
    }

    public void setTableCN(String tableCN) {
        this.tableCN = tableCN;
    }

    public List<Column> getList() {
        return list;
    }

    public void setList(List<Column> list) {
        this.list = list;
    }
}
