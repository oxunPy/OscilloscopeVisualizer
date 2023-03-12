package com.example.program.util.excel;

import com.example.program.util.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ExcelWriter<T> {

    private CellStyle getStyleHeader(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
//        font.setFontHeightInPoints((short) 12);
        font.setBold(true);

        style.setFont(font);

        return style;
    }

    private CellStyle getStyleHeader2(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
//        font.setFontHeightInPoints((short) 12);

        style.setFont(font);

        return style;
    }

    private CellStyle getStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
//        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
//        font.setBold(true);

        style.setFont(font);

        return style;
    }

    private CellStyle getTextStyleBold(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
        font.setBold(true);

        style.setFont(font);
        return style;
    }

    private CellStyle getTextStyleBoldRight(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.RIGHT);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
        font.setBold(true);

        style.setFont(font);
        return style;
    }


    private CellStyle getStyleIndex(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
//        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
//        font.setBold(true);

        style.setFont(font);

        return style;
    }

    private String getOutputFilePath(String filePrefix) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(100);
        String xlsxName = filePrefix + "_" + formatDate + "_" + random + ".xlsx";

        String userHome = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");

        return userHome + fileSeparator + xlsxName;
    }

    public String export(String title, String period, String[] headers, String[] fields, Collection<T> dataset, Collection<T> footer, String outputFilePathPrefix) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);

        CellStyle amountStyle = getStyle(workbook);
        amountStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
        amountStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numberStyle = getStyle(workbook);
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle styleHeader = getStyleHeader(workbook);
        CellStyle style = getStyle(workbook);
        CellStyle styleIndex = getStyleIndex(workbook);

        int index = 0;
        Row row = sheet.createRow(index++);
        createCell(row, 1, null, title);

        row = sheet.createRow(index++);
        createCell(row, 1, null, period);

        index++;
        row = sheet.createRow(index);
        for (short i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(styleHeader);
            cell.setCellValue(headers[i]);
        }

        Iterator<T> it = dataset.iterator();
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = it.next();

            for (short i = 0; i < fields.length; i++) {

                String fieldName = fields[i];
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    if (i != 0 && value instanceof Number) {
                        if (value instanceof Integer || value instanceof Long)
                            createCell(row, i, numberStyle, value);
                        else
                            createCell(row, i, numberStyle, value);
                    } else if (value instanceof ExcelItem.Amount) {
                        createCell(row, i, getAmountStyle(workbook, dataFormat, ((ExcelItem.Amount) value).getCurrencyCode(), false), ((ExcelItem.Amount) value).getValue());
                    } else
                        createCell(row, i, style, value);

                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {

                }
            }

        }

        if (footer != null && footer.size() > 0) {

            Iterator<T> itf = footer.iterator();
            while (itf.hasNext()) {
                index++;
                row = sheet.createRow(index);
                T t = itf.next();

                for (short i = 0; i < fields.length; i++) {

                    String fieldName = fields[i];
                    String getMethodName = "get"
                            + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (i != 0 && value instanceof Number) {
                            if (value instanceof Integer || value instanceof Long)
                                createCell(row, i, boldNumber, value);
                            else
                                createCell(row, i, boldNumber, value);
                        } else if (value instanceof ExcelItem.Amount) {
                            createCell(row, i, getAmountStyle(workbook, dataFormat, ((ExcelItem.Amount) value).getCurrencyCode(), true), ((ExcelItem.Amount) value).getValue());
                        } else
                            createCell(row, i, boldText, value);

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i, true);
        }

        String outputFilePath = getOutputFilePath(outputFilePathPrefix);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFilePath;
    }

    public CellStyle getAmountStyle(XSSFWorkbook workbook, DataFormat dataFormat, String currencyCode, boolean bold) {
        CellStyle amountStyle;

        if (bold) amountStyle = getTextStyleBold(workbook);
        else amountStyle = getStyle(workbook);

        if (StringUtil.notEmpty(currencyCode))
            amountStyle.setDataFormat(dataFormat.getFormat("#,##0.00 \"" + currencyCode + "\""));
        else
            amountStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
        amountStyle.setAlignment(HorizontalAlignment.RIGHT);

        return amountStyle;
    }

    private int createHeader(XSSFSheet sheet, CellStyle headerStyle, int rowIndex, List<List<ExcelItem>> headerConfig, int[][] headerPosition) {
        Row row;
        for (int i = 0; i < headerConfig.size(); i++) {
            int colIndex = 0;
            row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            for (ExcelItem hitem : headerConfig.get(i)) {

                colIndex = getColIndex(rowIndex, colIndex, headerPosition);
                createCell(row, colIndex, headerStyle, hitem.getTitle());

                Integer colspan = hitem.getColspan();
                Integer rowspan = hitem.getRowspan();
                CellRangeAddress cellRangeAddress = null;
                if (colspan != null && rowspan != null) {
                    cellRangeAddress = new CellRangeAddress(rowIndex, rowIndex + rowspan - 1, colIndex, colIndex + colspan - 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    setIndexs(rowIndex, colIndex, rowspan, colspan, headerPosition);
                } else if (colspan != null) {
                    cellRangeAddress = new CellRangeAddress(rowIndex, rowIndex, colIndex, colIndex + colspan - 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    setIndexs(rowIndex, colIndex, 1, colspan, headerPosition);
                } else if (rowspan != null) {
                    cellRangeAddress = new CellRangeAddress(rowIndex, rowIndex + rowspan - 1, colIndex, colIndex);
                    sheet.addMergedRegion(cellRangeAddress);
                    setIndexs(rowIndex, colIndex, rowspan, 1, headerPosition);
                } else {
                    setIndexs(rowIndex, colIndex, 1, 1, headerPosition);
                }
                if (cellRangeAddress != null) {
                    setBorderRegion(cellRangeAddress, sheet);
                }
            }
            rowIndex++;
        }
        return rowIndex;
    }


/*    public String ostatokExport(String title, Date toDate, List<ReportGroupProperty> groups, ReportSaleService service, boolean isAll, CurrencyProperty currency) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
//        numberStyle.setDataFormat(dataFormat.getFormat("#,###.0####"));

        CellStyle boldNumber = getTextStyleBold(workbook);
//        boldNumber.setDataFormat(dataFormat.getFormat("#,###.0####"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " + Time.toString(toDate, Time.pattern));

        rowIndex++;

        List<List<ExcelItem>> headerConfig = new ArrayList<>();

        List<ExcelItem> one = new ArrayList<>();
        one.add(new ExcelItem(StringConfig.getValue("caption.no"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("product.groupName"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("product.name"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
//        one.add(new ExcelItem(StringConfig.getValue("sales.weight"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.rate"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        headerConfig.add(one);

        Integer hRowSize = headerConfig.size();

        int[][] headerPosition = new int[rowIndex + hRowSize][6];

        rowIndex = createHeader(sheet, headerStyle, rowIndex, headerConfig, headerPosition);

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal qty = BigDecimal.ZERO;
        int counter = 0;
        for (ReportGroupProperty group : groups) {
            qty = qty.add(group.getClosingBalance());
            amount = amount.add(group.getClosingAmount());

            List<ReportProductProperty> productList = service.productOstatok(group.getId(), toDate, isAll, currency.getId());

            for (int i = 0; i < productList.size(); i++) {
                row = sheet.createRow(rowIndex++);
                ReportProductProperty product = productList.get(i);
                int colindex = 0;
                createCell(row, colindex++, null, ++counter);
                createCell(row, colindex++, null, group.getName());
                createCell(row, colindex++, null, product.getName());
                createCell(row, colindex++, null, product.getClosingBalance());
//                createCell(row, colindex++, null, product.getClosingAltBalance());
                createCell(row, colindex++, numberStyle, product.getRate());
                createCell(row, colindex++, numberStyle, product.getClosingAmount());

            }

        }

        row = sheet.createRow(rowIndex++);

        createCell(row, 2, null, StringConfig.getValue("label.total"));
        createCell(row, 3, numberStyle, qty);
        createCell(row, 5, numberStyle, amount);

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i, true);
        }

        String outputFilePath = getOutputFilePath(title);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFilePath;
    }*/


    public String exportAllProducts(String title, String sheetName, Collection<T> dataset, String[] fields, String[] headers) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        CellStyle styleHeader = getStyleHeader(workbook);
        CellStyle style = getStyleIndex(workbook);
        CellStyle numStyle = getStyle(workbook);
        int rowNum = 0;
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(styleHeader);
        cell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = format.format(new Date());
        rowNum++;
        Iterator<T> iter = dataset.iterator();
        while (iter.hasNext()) {
            T t = iter.next();
            row = sheet.createRow(rowNum);
            int currIndex = 0;
            for (short i = 0; i < fields.length; i++) {
                String field = fields[i];
                String getMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);

                try {
                    cell = createCell(row, currIndex++, numStyle, rowNum);
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    if (value instanceof String && field.equals("name")) {
                        cell = row.createCell(currIndex);
                        cell.setCellValue(value.toString());
                        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, currIndex, currIndex + 1));
                        currIndex += 2;
                    } else if (value instanceof Integer) {
                        createCell(row, currIndex++, numStyle, value.toString());
                    } else {
                        createCell(row, currIndex++, style, value.toString());
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            rowNum++;
        }

        String outputFilePath = getOutputFilePath(title);
        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(outputFilePath);
            workbook.write(outFile);
            outFile.flush();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFilePath;
    }

    public void setBorderRegion(CellRangeAddress region, XSSFSheet sheet) {
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
    }

    private void setIndexs(Integer row, Integer col, Integer rowspan, Integer colspan, int[][] indexs) {
        for (int i = row; i < row + rowspan; i++) {
            for (int j = col; j < col + colspan; j++) {
                indexs[i][j] = 1;
            }
        }
    }

    private static Integer getColIndex(Integer row, Integer col, int[][] indexs) {
        if (indexs[row][col] == 0) {
            return col;
        } else {
            for (int i = col; i < indexs[row].length; i++) {
                if (indexs[row][i] == 0) {
                    return i;
                }
            }
        }
        return 0;
    }


    private void createHeader(Row row, long index, String[] headers, CellStyle style) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }
    }

    private Cell createCell(Row row, int colIndex, CellStyle style, Object txt) {
        Cell cell = row.createCell(colIndex);

        boolean dicemal = false;
        if (txt instanceof String) {
            RichTextString text = new XSSFRichTextString((String) txt);
            cell.setCellValue(text);
        } else if (txt instanceof Long) {
            cell.setCellValue(((Long) txt).doubleValue());
            dicemal = true;
        } else if (txt instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) txt).doubleValue());
            dicemal = true;
        } else if (txt instanceof Integer) {
            cell.setCellValue(((Integer) txt).doubleValue());
            dicemal = true;
        } else if (txt instanceof Float) {
            cell.setCellValue(((Float) txt).doubleValue());
            dicemal = true;
        }
        if (style != null) {
            cell.setCellStyle(style);
        }

        return cell;
    }
}
