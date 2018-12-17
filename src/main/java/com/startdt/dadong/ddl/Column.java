package com.startdt.dadong.ddl;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-14 17:51
 * @Description:
 */
public class Column {
    String colName;
    String colType;
    String colDescribe;


    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getColDescribe() {
        return colDescribe;
    }

    public void setColDescribe(String colDescribe) {
        this.colDescribe = colDescribe;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
}
