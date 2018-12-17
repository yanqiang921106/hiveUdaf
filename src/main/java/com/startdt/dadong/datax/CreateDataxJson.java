package com.startdt.dadong.datax;

import com.alibaba.fastjson.JSONObject;
import com.startdt.dadong.utils.Configure;
import com.startdt.dadong.utils.XLSHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-11 20:56
 * @Description:
 */
public class CreateDataxJson {
    public static void main(String[] args) {
        String filePathxlsx = Configure.getValue("excelPath");
        try {
            Object[][] sheet = XLSHelper.readSheet(filePathxlsx, 0);
            List<Table> tables = getData(sheet);
            createDataxJson(tables);
        } catch (Exception e) {

        }

    }

    /**
     * 以如下格式创建sheet中的表：
     * a表名                         a表注释
     * 字段名	                    字段类型	                字段注释
     * 字段1	                    bigint	                日期（精确到小时）
     * 字段n	                    string	                注释
     * <p>
     * b表名	                    b表注释
     * 字段名	                    字段类型	                字段注释
     * 字段1	                    bigint	                日期（精确到小时）
     * 字段n	                    string	                注释
     *
     * @param sheet excel表的某个sheet
     * @throws Exception 异常
     */
    private static List<Table> getData(Object[][] sheet) throws Exception {
        String flg = null;
        List<Table> tables = new ArrayList();
        List<String> columns = new ArrayList();
        Table table = new Table();
        String tableName = "";
        for (Object[] row : sheet) {
            if ("表名".equals(row[0])) {
                if ("table".equals(flg) || "nothing".equals(flg)) {
                    table.tableName = tableName;
                    table.columns = columns;
                    tables.add(table);
                    table = new Table();
                    columns = new ArrayList();
                    tableName = "";
            }
            flg = "table";
            continue;
            } else if ("字段".equals(row[0])) {
                flg = "column";
                continue;
            } else if (null == row[0] || "".equals(row[0])) {
                flg = "nothing";
                continue;
            }

            if ("table".equals(flg)) {
                tableName = row[0].toString();
            } else if ("column".equals(flg)) {
                columns.add(row[0].toString());  //字段名
            }
        }
        table.tableName = tableName;
        table.columns = columns;
        tables.add(table);
        return tables;
    }

    /**
     * 格式化
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 生产datax json脚本
     * @param tables
     */
    public static void createDataxJson(List<Table> tables){
        if (tables == null) {
            return;
        }
        for (Table table : tables) {
            Writer writer = new Writer();
            Reader reader = new Reader();
            ParameterReader parameterReader = new ParameterReader();
            ParameterWriter parameterWriter = new ParameterWriter();
            parameterReader.table = table.tableName;
            parameterReader.column = table.columns;
            parameterWriter.table = table.tableName;
            parameterWriter.column = table.columns;
            reader.parameter = parameterReader;
            writer.parameter = parameterWriter;
            Setting setting = new Setting();
            Map<String, String> errorLimit = new HashMap<String, String>();
            errorLimit.put("record", "0");
            Map<String, String> speed = new HashMap<String, String>();
            speed.put("concurrent", "10");
            speed.put("mbps", "10");
            setting.errorLimit = errorLimit;
            setting.speed = speed;
            JSONObject configuration = new JSONObject();
            JSONObject subJson = new JSONObject();
            subJson.put("reader", reader);
            subJson.put("writer", writer);
            subJson.put("setting", setting);
            configuration.put("configuration", subJson);
            configuration.put("type", "job");
            configuration.put("version", "1.0");
            System.out.println(formatJson(configuration.toJSONString()));
        }
    }
    /**
     * 添加space
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
