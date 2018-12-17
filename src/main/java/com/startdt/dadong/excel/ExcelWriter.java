package com.startdt.dadong.excel;


import com.startdt.dadong.ddl.Column;
import com.startdt.dadong.ddl.Table;
import com.startdt.dadong.utils.FileReader;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-13 21:05
 * @Description:
 */
public class ExcelWriter {

    /**
     * 判断文件是否存在.
     *
     * @param fileDir 文件路径
     * @return
     */
    public static boolean fileExist(String fileDir) {
        boolean flag = false;
        File file = new File(fileDir);
        flag = file.exists();
        return flag;
    }

    /**
     * 判断文件的sheet是否存在.
     *
     * @param fileDir   文件路径
     * @param sheetName 表格索引名
     * @return
     */
    public static boolean sheetExist(String fileDir, String sheetName) throws Exception {
        boolean flag = false;
        File file = new File(fileDir);
        if (file.exists()) {    //文件存在
            //创建workbook
            try {
                HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
                //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
                HSSFSheet sheet = workbook.getSheet(sheetName);
                if (sheet != null)
                    flag = true;
            } catch (Exception e) {
                throw e;
            }

        } else {    //文件不存在
            flag = false;
        }
        return flag;
    }

    /**
     * 创建新excel.
     *
     * @param fileDir   excel的路径
     * @param sheetName 要创建的表格索引
     * @param titleRow  excel的第一行即表格头
     */
    public static void createExcel(String fileDir, String sheetName, String titleRow[]) throws Exception {
        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        sheet1.setColumnWidth(0, 60 * 252);
        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            HSSFRow row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            for (short i = 0; i < titleRow.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(titleRow[i]);
            }
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件.
     *
     * @param fileDir 文件路径
     */
    public static boolean deleteExcel(String fileDir) {
        boolean flag = false;
        File file = new File(fileDir);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                file.delete();
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 往excel中写入(已存在的数据无法写入).
     *
     * @param fileDir   文件路径
     * @param sheetName 表格索引
     * @throws Exception
     */
    public static void writeToExcel(String fileDir, String sheetName, List<Table> list) throws Exception {
        //创建workbook
        HSSFWorkbook workBook = new HSSFWorkbook();
        CellStyle style = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setFontName("微软雅黑");
        style.setFont(font);
        //文件流
        FileOutputStream out = null;
        HSSFSheet sheet = workBook.createSheet(sheetName);
        sheet.setDefaultColumnStyle(0, style);
        sheet.setDefaultColumnStyle(1, style);
        sheet.setDefaultColumnStyle(2, style);
        sheet.setColumnWidth(0, 256 * 50);
        sheet.setColumnWidth(1, 256 * 30);
        sheet.setColumnWidth(2, 256 * 30);
        try {
            int i = 0;
            for (Table table : list) {
                HSSFRow row0 = sheet.createRow(i);
                HSSFCell cell0 = row0.createCell(0);
                HSSFCell cell1 = row0.createCell(1);
                cell0.setCellStyle(style);
                cell1.setCellStyle(style);
                cell0.setCellValue("表名");
                cell1.setCellValue("表描述");
                HSSFRow row1 = sheet.createRow(i + 1);
                row1.createCell(0).setCellValue(table.getTableN());
                row1.createCell(1).setCellValue(table.getTableCN());
                HSSFRow row2 = sheet.createRow(i + 2);
                HSSFCell colCell0 = row2.createCell(0);
                HSSFCell colCell1 = row2.createCell(1);
                HSSFCell colCell2 = row2.createCell(2);
                colCell0.setCellValue("字段");
                colCell1.setCellValue("字段类型");
                colCell2.setCellValue("字段描述");
                List<Column> columns = table.getList();
                if (columns != null) {
                    for (int j = 0; j < columns.size(); j++) {
                        Column column = columns.get(j);
                        HSSFRow row = sheet.createRow(i + 3 + j);
                        row.createCell(0).setCellValue(column.getColName());
                        row.createCell(1).setCellValue(column.getColType());
                        row.createCell(2).setCellValue(column.getColDescribe());
                    }
                }
                HSSFRow nullRow = sheet.createRow(i + 4 + columns.size());
                nullRow.createCell(0).setCellValue("");
                i = sheet.getLastRowNum();
            }
            out = new FileOutputStream(fileDir);
            workBook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据ddl语句生产Table列表
     *
     * @param lines
     * @return
     */
    public static List<Table> createTable(List<String> lines) {
        List<Table> list = new ArrayList<Table>();
        if (lines == null || lines.isEmpty()) {
            return list;
        }
        Table table = new Table();
        List<Column> cols = new ArrayList();
        Column column = new Column();
        boolean isColumn = false;
        for (String line : lines) {
            if (line.contains("CREATE TABLE")) {
                String tableName = line.substring(line.indexOf("`") + 1, line.lastIndexOf("`"));
                table = new Table();
                cols = new ArrayList();
                table.setTableN(tableName);
                isColumn = true;
                continue;
            }
            if (isColumn && !line.contains("ENGINE=InnoDB") && !line.contains("PRIMARY KEY")) {
                column = new Column();
                String colName = line.substring(line.indexOf("`") + 1, line.lastIndexOf("`"));
                String[] strArray = line.split(" ");
                String colype = strArray[3];
                if (strArray[3].indexOf(")") >= 0) {
                    colype = strArray[3].substring(0, strArray[3].indexOf(")") + 1);
                }
                String colDescribe = "";
                int commentIndex = line.indexOf("COMMENT");
                if (commentIndex > 0) {
                    String commentStr = line.substring(commentIndex);
                    colDescribe = commentStr.substring(commentStr.indexOf("'") + 1, commentStr.lastIndexOf("'"));
                }
                column.setColName(colName);
                column.setColType(colype);
                column.setColDescribe(colDescribe);
                cols.add(column);
                continue;
            }
            if (line.contains("ENGINE=InnoDB")) {
                String tableCN = "";
                if (line.indexOf("'") >= 0) {
                    tableCN = line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"));
                }
                table.setTableCN(tableCN);
                table.setList(cols);
                list.add(table);
                isColumn = false;
                continue;
            }

        }
        return list;
    }

    public static void main(String[] args) throws Exception {

        String dirPath = "/Users/yanchuan/Desktop/test";
        List<String> files = FileReader.readDir(dirPath);
        for (String filePath : files) {
            List<String> list = FileReader.readFileByLines(filePath);
            List<Table> tables = createTable(list);
            File file = new File(filePath);
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            String excelPath = file.getParent().concat("/excel/");
            File excelDir = new File(excelPath);
            if (!excelDir.exists()) {
                excelDir.mkdir();
            }
            String excelFile = excelDir.getAbsolutePath().concat("/" + fileName + ".xls");
            writeToExcel(excelFile, fileName, tables);
        }
    }
}