package com.startdt.dadong.utils;



import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-13 21:05
 * @Description:
 */
public class XLSHelper {

    /**
     * 获取所有的sheet Name
     * @param xlsPath excel 路径
     * @return sheet Names and index
     * @throws Exception 异常
     */
    public static HashMap<String,Integer> getSheetNames(String xlsPath) throws Exception{
        File excelFile = new File(xlsPath);
        String xls = xlsPath.substring(xlsPath.indexOf('.'));
        Workbook workbook = null;
        Sheet sheet = null;
        switch (xls) {
            case ".xls": workbook = new HSSFWorkbook(new FileInputStream(excelFile)); break;
            case ".xlsx": workbook = new XSSFWorkbook(new FileInputStream(excelFile)); break;
            default: return null;
        }
        int sheetNum = workbook.getNumberOfSheets();
        List<? extends Name> allNames = workbook.getAllNames();
        HashMap<String,Integer> names = new HashMap<>();
        for(int i = 0 ; i < sheetNum ; i++){
            names.put(workbook.getSheetName(i),i);
        }
        return names;
    }

    /**
     * 获取所有的sheet
     * @param xlsPath 路径
     * @return 数据
     * @throws Exception 异常
     */
    public static Object[][][] read(String xlsPath) throws Exception{
        File excelFile = new File(xlsPath);
        /*
         * 判断给定文件的类型; 1.如果是xls的问价那类型就创建XSSFWorkBook ;
         * 2.如果是xlsx的文件类型就创建HSSFWorkBook ;
         */

        String xls = xlsPath.substring(xlsPath.indexOf('.'));
        Workbook workbook = null;
        Sheet sheet = null;
        if (xls.equals(".xls")) {
            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
        } else if (xls.equals(".xlsx")) {
            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
        }else {
            return null;
        }
        int sheetNum = workbook.getNumberOfSheets();
        Object[][][] object = new Object[sheetNum][][];

        for(int num = 0 ; num < sheetNum; num++){
            sheet = workbook.getSheetAt(num);
            int rowcount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            List<Object[]> list = new ArrayList<Object[]>();
            Row row;
            Cell cell;
            for (int i = 0; i < rowcount + 1; i++) {
                row = sheet.getRow(i);
                if(null == row || -1 == row.getLastCellNum()) continue;
                /*
                 * System.out.println("当前行是：" + (row.getRowNum() + 1) +
                 * " ;当前行的第一个单元格是：" + row.getFirstCellNum() + " ; 当前前的最后一个单元格是："
                 * + row.getLastCellNum() + "; ");
                 */
                Object[] obj = new Object[row.getLastCellNum()];

                // System.out.println("obj 数组的长度是 ：" + obj.length + " ;");
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if(null == cell ) continue;
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            obj[j] = cell.getRichStringCellValue().getString();
                        /*
                         * System.out.print(cell.getRichStringCellValue().
                         * getString()); System.out.print("|");
                         */
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                obj[j] = cell.getDateCellValue();
                                // System.out.print(String.valueOf(cell.getDateCellValue()));
                            } else {
                                obj[j] = cell.getNumericCellValue();
                                // System.out.print(cell.getNumericCellValue());
                            }
                            // System.out.print("|");
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            obj[j] = cell.getBooleanCellValue();
                        /*
                         * System.out.print(cell.getBooleanCellValue());
                         * System.out.print("|");
                         */
                            break;
                        default:
                    }

                }
                list.add(obj);
            }

            object[num] = new Object[list.size()][];
            for (int i = 0; i < object[num].length; i++) {
                object[num][i] = list.get(i);
            }
        }

        return object;

    }

    public static Object[][] readSheet(String xlsPath,int sheetIndex) throws Exception{
        File excelFile = new File(xlsPath);
        String xls = xlsPath.substring(xlsPath.indexOf('.'));
        Workbook workbook = null;
        Sheet sheet = null;
        if (xls.equals(".xls"))
            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
        else if (xls.equals(".xlsx"))
            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
        else
            return null;
        int sheetNum = workbook.getNumberOfSheets();

        if(sheetIndex>=0 && sheetIndex<sheetNum){
            sheet = workbook.getSheetAt(sheetIndex);
            System.out.println("该文件含 Sheet 数量为 : " + sheetNum + " 个。");
            int rowcount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            List<Object[]> list = new ArrayList<Object[]>();
            Row row;
            Cell cell;
            for (int i = 0; i < rowcount + 1; i++) {
                try{
                    row = sheet.getRow(i);
                    if(null == row) continue;
                    Object[] obj = new Object[row.getLastCellNum()];
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        cell = row.getCell(j);
                        if(null == cell ) continue;
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                obj[j] = cell.getRichStringCellValue().getString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell))
                                    obj[j] = cell.getDateCellValue();
                                else
                                    obj[j] = cell.getNumericCellValue();

                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                obj[j] = cell.getBooleanCellValue();
                                break;
                            default:
                        }

                    }
                    list.add(obj);
                }catch (Exception e){
                    System.out.println("        数据表[" + sheetIndex + "]的第 " + i + " 行有格式错误!");
                }
            }
            Object[][] object = new Object[list.size()][];
            for (int i = 0; i < object.length; i++) {
                object[i] = list.get(i);
            }
            return object;
        }else {
            System.out.println(" sheetIndex 不合法！");
            return null;
        }
    }

    public static Object[][] readSheet(String xlsPath,String sheetName) throws Exception{
        File excelFile = new File(xlsPath);
        String xls = xlsPath.substring(xlsPath.indexOf('.'));
        Workbook workbook = null;
        Sheet sheet = null;
        if (xls.equals(".xls"))
            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
        else if (xls.equals(".xlsx"))
            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
        else
            return null;
        int sheetNum = workbook.getNumberOfSheets();
        int sheetIndex = -1;
        // 获取指定sheetName的index
        for(int i = 0 ; i < sheetNum; i++){
            if(sheetName.equals(workbook.getSheetName(i))){
                sheetIndex = i;
                break;
            }
        }
        if(sheetIndex>=0 && sheetIndex<sheetNum){
            sheet = workbook.getSheetAt(sheetIndex);
            System.out.println("该文件含 Sheet 数量为 : " + sheetNum + " 个。");
            int rowcount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            List<Object[]> list = new ArrayList<Object[]>();
            Row row;
            Cell cell;
            for (int i = 0; i < rowcount + 1; i++) {
                try{
                    row = sheet.getRow(i);
                    if(null == row) continue;
                    Object[] obj = new Object[row.getLastCellNum()];
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        cell = row.getCell(j);
                        if(null == cell ) continue;
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                obj[j] = cell.getRichStringCellValue().getString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell))
                                    obj[j] = cell.getDateCellValue();
                                else
                                    obj[j] = cell.getNumericCellValue();

                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                obj[j] = cell.getBooleanCellValue();
                                break;
                            default:
                        }

                    }
                    list.add(obj);
                }catch (Exception e){
                    System.out.println("        数据表[" + sheetIndex + "]的第 " + i + " 行有格式错误!");
                }
            }
            Object[][] object = new Object[list.size()][];
            for (int i = 0; i < object.length; i++) {
                object[i] = list.get(i);
            }
            return object;
        }else {
            System.out.println(" sheetIndex 不合法！");
            return null;
        }
    }
}
