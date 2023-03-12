package com.example.program.util.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Xurshidbek on 05.01.2018.
 */
public class ExcelReader {

    public static void main(String[] args) {
        try {
            List<List<Object>> rows = asList("D:\\project\\web\\java\\akfa-dealer-desktop\\test.xlsx", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<Object>> asList(String excelFilePath, int sheetIndex) throws IOException {
        List<List<Object>> rows = new ArrayList<>();

        Workbook workbook = getWorkbook(excelFilePath);
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row nextRow = rowIterator.next();
            Iterator<Cell> cellIterator = nextRow.iterator();

            List<Object> row = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();

                row.add(getCellValue(nextCell));
            }
            rows.add(row);
        }

        workbook.close();
        return rows;
    }

    private static Workbook getWorkbook(String excelFilePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(excelFilePath);

        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx"))
            workbook = new XSSFWorkbook(inputStream);
        else if (excelFilePath.endsWith("xls"))
            workbook = new HSSFWorkbook(inputStream);
        else
            throw new IllegalArgumentException("The specified file is not Excel file");
        return workbook;
    }

    private static String getCellValueAsString(Cell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell))
                    cellValue = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(cell.getDateCellValue());
                else
                    cellValue = new DecimalFormat("#.##").format(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
        }
        return cellValue;
    }

    private static Object getCellValue(Cell cell) {
        Object cellValue = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell))
                    cellValue = cell.getDateCellValue();
                else
                    cellValue = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue = cell.getErrorCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
        }
        return cellValue;
    }
}
