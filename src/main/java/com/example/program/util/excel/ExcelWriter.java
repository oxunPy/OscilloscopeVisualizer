package com.example.program.util.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uz.akfa.dealer.desktop.app.entity.DealerClient;
import uz.akfa.dealer.desktop.app.entity.Invoice;
import uz.akfa.dealer.desktop.app.property.*;
import uz.akfa.dealer.desktop.app.service.InvoiceService;
import uz.akfa.dealer.desktop.app.service.ReportSaleService;
import uz.akfa.dealer.desktop.common.util.DateUtils;
import uz.akfa.dealer.desktop.common.util.StringConfig;
import uz.akfa.dealer.desktop.common.util.StringUtil;
import uz.akfa.dealer.desktop.common.util.Time;

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

    public String invoiceExport(String title, List<InvoiceProperty> invoices, InvoiceService service) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.0000"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.0000"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        rowIndex++;

        for (InvoiceProperty invoice : invoices) {
            row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, Time.toString(invoice.getDate(), Time.pattern));

            row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, StringConfig.formatValue("caption.nomer", invoice.getId()));

            if (invoice.getClient() != null) {

                row = sheet.createRow(rowIndex++);
                createCell(row, 1, boldText, invoice.getClient().getPrintableName());
            }

            List<InvoiceItemGroupedProperty> invoiceItems = InvoiceItemGroupedProperty.convert(service.getInvoiceItems(invoice));

            row = sheet.createRow(rowIndex++);
            if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                createHeader(row, rowIndex, new String[]{
                        StringConfig.getValue("caption.no"),
                        StringConfig.getValue("product.name"),
                        StringConfig.getValue("stock.actual"),
                        StringConfig.getValue("stock.difference"),
                        StringConfig.getValue("sales.rate"),
                        StringConfig.getValue("label.total")
                }, headerStyle);
            } else {

                createHeader(row, rowIndex, new String[]{
                        StringConfig.getValue("caption.no"),
                        StringConfig.getValue("product.name"),
                        StringConfig.getValue("label.qty"),
//                        StringConfig.getValue("sales.weight"),
                        StringConfig.getValue("sales.rate"),
                        StringConfig.getValue("label.amount")
                }, headerStyle);
            }

            int nomer = 1;
            BigDecimal itog = BigDecimal.ZERO;
            for (InvoiceItemGroupedProperty invoiceItem : invoiceItems) {
                row = sheet.createRow(rowIndex++);
                if (!invoiceItem.isGroup()) {
                    createCell(row, 0, null, nomer++);
                    if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                        itog = itog.add(invoiceItem.getTotalDifference());
                    } else {
                        itog = itog.add(invoiceItem.getTotal());
                    }

                    createCell(row, 1, null, invoiceItem.getName());
                    if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                        createCell(row, 2, null, invoiceItem.getActualQuantity());
                        createCell(row, 3, null, invoiceItem.getDifference());
                        createCell(row, 4, numberStyle, invoiceItem.getRate());
                        createCell(row, 5, numberStyle, invoiceItem.getTotalDifference());
                    } else {
                        if (invoice.getType() == Invoice.Type.ITEM_GROUP_CHANGE) {

                            createCell(row, 2, null, invoiceItem.getQuantity().multiply(BigDecimal.valueOf(invoiceItem.getActionType())));
                            createCell(row, 4, numberStyle, invoiceItem.getTotal().multiply(BigDecimal.valueOf(invoiceItem.getActionType())));
                        } else {
                            createCell(row, 2, null, invoiceItem.getQuantity());
                            createCell(row, 4, numberStyle, invoiceItem.getTotal());
                        }
//                        createCell(row, 3, null, invoiceItem.getAlternativeQuantity());
                        createCell(row, 3, numberStyle, invoiceItem.getRate());
                    }
                } else {
                    nomer = 1;
                    createCell(row, 1, boldText, invoiceItem.getName());
                    if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                        createCell(row, 2, null, invoiceItem.getActualQuantity());
                        createCell(row, 3, null, invoiceItem.getDifference());
                        createCell(row, 4, boldNumber, invoiceItem.getRate());
                        createCell(row, 5, boldNumber, invoiceItem.getTotalDifference());
                    } else {
                        createCell(row, 2, null, invoiceItem.getQuantity());
//                        createCell(row, 3, null, invoiceItem.getAlternativeQuantity());
                        createCell(row, 3, boldNumber, invoiceItem.getRate());
                        createCell(row, 4, boldNumber, invoiceItem.getTotal());
                    }
                }

            }
            row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, StringConfig.getValue("label.total"));
            createCell(row, 5, boldNumber, itog);

            rowIndex++;
        }

        for (int i = 0; i < 6; i++) {
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
    }

    public String invoiceExport(String title, List<InvoiceProperty> invoices, InvoiceService service, File file) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.0000"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.0000"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        rowIndex++;

        for (InvoiceProperty invoice : invoices) {
            row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, Time.toString(invoice.getDate(), Time.pattern));

            row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, StringConfig.formatValue("caption.nomer", invoice.getId()));

            if (invoice.getClient() != null) {

                row = sheet.createRow(rowIndex++);
                createCell(row, 1, boldText, invoice.getClient().getPrintableName());
            }

            List<InvoiceItemGroupedProperty> invoiceItems = InvoiceItemGroupedProperty.convert(service.getInvoiceItems(invoice));

            row = sheet.createRow(rowIndex++);
            if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                createHeader(row, rowIndex, new String[]{
                        StringConfig.getValue("caption.no"),
                        StringConfig.getValue("product.name"),
                        StringConfig.getValue("stock.actual"),
                        StringConfig.getValue("stock.difference"),
                        StringConfig.getValue("sales.rate"),
                        StringConfig.getValue("label.total")
                }, headerStyle);
            } else {

                createHeader(row, rowIndex, new String[]{
                        StringConfig.getValue("caption.no"),
                        StringConfig.getValue("product.name"),
                        StringConfig.getValue("label.qty"),
//                        StringConfig.getValue("sales.weight"),
                        StringConfig.getValue("sales.rate"),
                        StringConfig.getValue("label.amount")
                }, headerStyle);
            }

            int nomer = 1;
            BigDecimal itog = BigDecimal.ZERO;
            for (InvoiceItemGroupedProperty invoiceItem : invoiceItems) {
                row = sheet.createRow(rowIndex++);
                if (!invoiceItem.isGroup()) {
                    createCell(row, 0, null, nomer++);
                    if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                        itog = itog.add(invoiceItem.getTotalDifference());
                    } else {
                        itog = itog.add(invoiceItem.getTotal());
                    }

                    createCell(row, 1, null, invoiceItem.getName());
                    if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                        createCell(row, 2, null, invoiceItem.getActualQuantity());
                        createCell(row, 3, null, invoiceItem.getDifference());
                        createCell(row, 4, numberStyle, invoiceItem.getRate());
                        createCell(row, 5, numberStyle, invoiceItem.getTotalDifference());
                    } else {
                        createCell(row, 2, null, invoiceItem.getQuantity());
//                        createCell(row, 3, null, invoiceItem.getAlternativeQuantity());
                        createCell(row, 3, numberStyle, invoiceItem.getRate());
                        createCell(row, 4, numberStyle, invoiceItem.getTotal());
                    }
                } else {
                    nomer = 1;
                    createCell(row, 1, boldText, invoiceItem.getName());
                    if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                        createCell(row, 2, null, invoiceItem.getActualQuantity());
                        createCell(row, 3, null, invoiceItem.getDifference());
                        createCell(row, 4, boldNumber, invoiceItem.getRate());
                        createCell(row, 5, boldNumber, invoiceItem.getTotalDifference());
                    } else {
                        createCell(row, 2, null, invoiceItem.getQuantity());
//                        createCell(row, 3, null, invoiceItem.getAlternativeQuantity());
                        createCell(row, 3, boldNumber, invoiceItem.getRate());
                        createCell(row, 4, boldNumber, invoiceItem.getTotal());
                    }
                }

            }
            row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, StringConfig.getValue("label.total"));
            if (invoice.getType() == Invoice.Type.ACTUAL_BALANCE) {
                createCell(row, 5, boldNumber, itog);
            } else {
                createCell(row, 4, boldNumber, itog);
            }

            rowIndex++;
        }

        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i, true);
        }

//        String outputFilePath = getOutputFilePath(title);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
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

        return file.getPath();
    }

    public String tovarOborotExport(String title, Date fromDate, Date toDate, List<ReportGroupProperty> groups, ReportSaleService service, CurrencyProperty currency) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " + Time.toString(fromDate, Time.pattern) + " / " + Time.toString(toDate, Time.pattern));

        rowIndex++;

        List<List<ExcelItem>> headerConfig = new ArrayList<>();

        List<ExcelItem> one = new ArrayList<>();
        one.add(new ExcelItem(StringConfig.getValue("caption.no"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("product.groupName"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("product.name"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("period.openingbalance"), 1, 2));
        one.add(new ExcelItem(StringConfig.getValue("menu.sales"), 1, 2));
        one.add(new ExcelItem(StringConfig.getValue("menu.purchase"), 1, 2));
        one.add(new ExcelItem(StringConfig.getValue("period.closingbalance"), 1, 2));
        headerConfig.add(one);

        List<ExcelItem> two = new ArrayList<>();
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
//        two.add(new ExcelItem(StringConfig.getValue("sales.weight"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
//        two.add(new ExcelItem(StringConfig.getValue("sales.weight"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
//        two.add(new ExcelItem(StringConfig.getValue("sales.weight"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
//        two.add(new ExcelItem(StringConfig.getValue("sales.weight"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        headerConfig.add(two);

        Integer hRowSize = headerConfig.size();

        int[][] headerPosition = new int[rowIndex + hRowSize][11];

        rowIndex = createHeader(sheet, headerStyle, rowIndex, headerConfig, headerPosition);


        for (ReportGroupProperty group : groups) {

            List<ReportProductProperty> productList = service.productTurnover(group.getId(), fromDate, toDate, currency.getId());

            for (int i = 0; i < productList.size(); i++) {
                row = sheet.createRow(rowIndex++);
                ReportProductProperty product = productList.get(i);
                int colindex = 0;
                createCell(row, colindex++, null, i + 1);
                createCell(row, colindex++, null, group.getName());
                createCell(row, colindex++, null, product.getName());
                createCell(row, colindex++, null, product.getOpeningBalance());
//                createCell(row, colindex++, null, product.getOpeningAltBalance());
                createCell(row, colindex++, numberStyle, product.getOpeningAmount());
                createCell(row, colindex++, null, product.getPrixod());
//                createCell(row, colindex++, null, product.getPrixodAlt());
                createCell(row, colindex++, numberStyle, product.getPrixodAmount());
                createCell(row, colindex++, null, product.getRasxod());
//                createCell(row, colindex++, null, product.getRasxodAlt());
                createCell(row, colindex++, numberStyle, product.getRasxodAmount());
                createCell(row, colindex++, null, product.getClosingBalance());
//                createCell(row, colindex++, null, product.getClosingAltBalance());
                createCell(row, colindex++, numberStyle, product.getClosingAmount());

            }

//            rowIndex++;
        }

        for (int i = 0; i < 10; i++) {
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
    }

    public String mainReportExport(String title, Date fromDate, Date toDate, List<ReportProductProperty> items) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle boldRightText = getTextStyleBoldRight(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.0###"));

        CellStyle natNumberStyle = workbook.createCellStyle();
        natNumberStyle.setDataFormat(dataFormat.getFormat("#,###"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.0###"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " + Time.toString(fromDate, Time.pattern) + " / " + Time.toString(toDate, Time.pattern));

        rowIndex++;

        List<List<ExcelItem>> headerConfig = new ArrayList<>();

        List<ExcelItem> one = new ArrayList<>();
        one.add(new ExcelItem(StringConfig.getValue("product.group"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("product.name"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("label.fromdate"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("label.todate"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("label.price"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("dealerClient.openingBalance"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("menu.purchase"), 1, 2));
        one.add(new ExcelItem(StringConfig.getValue("menu.sales"), 1, 2));
        one.add(new ExcelItem(StringConfig.getValue("menu.return.base"), 1, 2));
        one.add(new ExcelItem(StringConfig.getValue("menu.return.client"), 1, 2));
        one.add(new ExcelItem(StringConfig.getValue("dealerClient.closingBalance"), 2, 1));
        one.add(new ExcelItem(StringConfig.getValue("sales.profit"), 2, 1));
        headerConfig.add(one);

        List<ExcelItem> two = new ArrayList<>();
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
        two.add(new ExcelItem(StringConfig.getValue("label.amount"), null, null));

        headerConfig.add(two);


        Integer hRowSize = headerConfig.size();

        int[][] headerPosition = new int[rowIndex + hRowSize][16];

        rowIndex = createHeader(sheet, headerStyle, rowIndex, headerConfig, headerPosition);

        BigDecimal prixodAmount = BigDecimal.ZERO;
        BigDecimal rasxodAmount = BigDecimal.ZERO;
        BigDecimal returnBaseAmount = BigDecimal.ZERO;
        BigDecimal returnClientAmount = BigDecimal.ZERO;
        BigDecimal profitAmount = BigDecimal.ZERO;

        for (int i = 0; i < items.size(); i++) {
            row = sheet.createRow(rowIndex++);
            ReportProductProperty product = items.get(i);
            int colindex = 0;
            createCell(row, colindex++, null, product.getGroup());
            createCell(row, colindex++, null, product.getName());
            createCell(row, colindex++, null, Time.toString(product.getRatedate(), true));
            createCell(row, colindex++, null, Time.toString(product.getRatetodate(), true));
            createCell(row, colindex++, numberStyle, product.getRate().stripTrailingZeros());

            createCell(row, colindex++, natNumberStyle, getBalanceWithoutZero(product.getOpeningBalance().stripTrailingZeros()));
            createCell(row, colindex++, natNumberStyle, getBalanceWithoutZero(product.getPrixod().stripTrailingZeros()));
            createCell(row, colindex++, numberStyle, getBalanceWithoutZero(product.getPrixodAmount().stripTrailingZeros()));
            createCell(row, colindex++, natNumberStyle, getBalanceWithoutZero(product.getRasxod().stripTrailingZeros()));
            createCell(row, colindex++, numberStyle, getBalanceWithoutZero(product.getRasxodAmount().stripTrailingZeros()));
            createCell(row, colindex++, natNumberStyle, getBalanceWithoutZero(product.getReturnBase().stripTrailingZeros()));
            createCell(row, colindex++, numberStyle, getBalanceWithoutZero(product.getReturnBaseAmount().stripTrailingZeros()));
            createCell(row, colindex++, natNumberStyle, getBalanceWithoutZero(product.getReturnClient().stripTrailingZeros()));
            createCell(row, colindex++, numberStyle, getBalanceWithoutZero(product.getReturnClientAmount().stripTrailingZeros()));
            createCell(row, colindex++, natNumberStyle, getBalanceWithoutZero(product.getClosingBalance().stripTrailingZeros()));
            createCell(row, colindex++, numberStyle, getBalanceWithoutZero(product.getProfit().stripTrailingZeros()));

            if (product.getPrixodAmount() != null)
                prixodAmount = prixodAmount.add(product.getPrixodAmount());
            if (product.getRasxodAmount() != null)
                rasxodAmount = rasxodAmount.add(product.getRasxodAmount());
            if (product.getReturnBaseAmount() != null)
                returnBaseAmount = returnBaseAmount.add(product.getReturnBaseAmount());
            if (product.getReturnClientAmount() != null)
                returnClientAmount = returnClientAmount.add(product.getReturnClientAmount());
            if (product.getProfit() != null)
                profitAmount = profitAmount.add(product.getProfit());
        }

        row = sheet.createRow(rowIndex++);
        //footer
        createCell(row, 0, boldRightText, StringConfig.getValue("label.total"));

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 6);
        sheet.addMergedRegion(cellRangeAddress);
        setBorderRegion(cellRangeAddress, sheet);

        createCell(row, 7, boldNumber, prixodAmount);
        createCell(row, 9, boldNumber, rasxodAmount);
        createCell(row, 11, boldNumber, returnBaseAmount);
        createCell(row, 13, boldNumber, returnClientAmount);
        createCell(row, 15, boldNumber, profitAmount);

        row = sheet.createRow(rowIndex++);

        createCell(row, 0, boldRightText, StringConfig.getValue("label.total.sales"));

        CellRangeAddress cellSalesRangeAddress = new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 6);
        sheet.addMergedRegion(cellSalesRangeAddress);
        setBorderRegion(cellSalesRangeAddress, sheet);

        createCell(row, 7, boldNumber, rasxodAmount.subtract(returnClientAmount));

        row = sheet.createRow(rowIndex++);

        createCell(row, 0, boldRightText, StringConfig.getValue("label.total.purchase"));

        CellRangeAddress cellPurchaseRangeAddress = new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 6);
        sheet.addMergedRegion(cellPurchaseRangeAddress);
        setBorderRegion(cellPurchaseRangeAddress, sheet);

        createCell(row, 7, boldNumber, prixodAmount.subtract(returnBaseAmount));


        for (int i = 0; i < 12; i++) {
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
    }

    private BigDecimal getBalanceWithoutZero(BigDecimal value) {
        if (value != null && value.compareTo(BigDecimal.ZERO) != 0)
            return value;
        return null;
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


    public String ostatokExport(String title, Date toDate, List<ReportGroupProperty> groups, ReportSaleService service, boolean isAll, CurrencyProperty currency) {
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
    }

    public String salesByClient(String title, ReportClientProperty client, Date fromdate, Date toDate, List<ReportGroupProperty> groups, ReportSaleService service, CurrencyProperty currency) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.period") + ": " + Time.toString(fromdate, Time.pattern) + " | " + Time.toString(toDate, Time.pattern));
        row = sheet.createRow(rowIndex++);
        Cell clientCell = row.createCell(1);
        clientCell.setCellValue(StringConfig.getValue("label.client") + ": " + client.getName());

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

        int counter = 1;
        for (ReportGroupProperty group : groups) {
            /*row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, group.getName());
            createCell(row, 2, boldText, group.getQty());
//            createCell(row, 3, boldText, group.getAltQty());
            createCell(row, 4, boldNumber, group.getTotal());
*/


            List<ReportProductProperty> productList = service.salesProductByClient(client.getId(), group.getId(), fromdate, toDate, currency.getId());

            for (int i = 0; i < productList.size(); i++) {
                row = sheet.createRow(rowIndex++);
                ReportProductProperty product = productList.get(i);
                int colindex = 0;
                createCell(row, colindex++, null, counter++);
                createCell(row, colindex++, null, group.getName());
                createCell(row, colindex++, null, product.getName());
                createCell(row, colindex++, null, product.getQty());
//                createCell(row, colindex++, null, product.getAltQty());
                createCell(row, colindex++, numberStyle, product.getRate());
                createCell(row, colindex++, numberStyle, product.getTotal());

            }

//            rowIndex++;
        }

        for (int i = 0; i < 6; i++) {
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
    }


    public String rasxodPoClientamGroup(String title, String sheetName, Date fromDate, Date toDate,
                                        List<ReportGroupProperty> groups,
                                        String groupTotal,
                                        String paidCashTotal,
                                        String paidBankTotal,
                                        String returnAmountTotal,
                                        String debtTotal,
                                        String pawnTotal,
                                        String expenseTotal,
                                        String cashBoxTotal) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));
        CellStyle boldNumberRight = getTextStyleBoldRight(workbook);
        boldNumberRight.setDataFormat(dataFormat.getFormat("#,##0.00"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " + Time.toString(fromDate, Time.pattern) + " / " + Time.toString(toDate, Time.pattern));

        rowIndex++;

        row = sheet.createRow(rowIndex++);
        createCell(row, 0, headerStyle, StringConfig.getValue("caption.no"));
        createCell(row, 1, headerStyle, StringConfig.getValue("product.groupName"));
        createCell(row, 2, headerStyle, StringConfig.getValue("label.qty"));
        createCell(row, 3, headerStyle, StringConfig.getValue("label.total"));

        int count = 0;
        for (ReportGroupProperty group : groups) {

            row = sheet.createRow(rowIndex++);
            int colindex = 0;
            createCell(row, colindex++, null, ++count);
            createCell(row, colindex++, null, group.getName());
            createCell(row, colindex++, numberStyle, group.getQty());
            createCell(row, colindex++, numberStyle, group.getTotal());

        }
        row = sheet.createRow(rowIndex++);
        createCell(row, 1, boldText, StringConfig.getValue("label.total"));
        createCell(row, 3, boldNumberRight, groupTotal);

        rowIndex++;
        row = sheet.createRow(rowIndex++);
        createCell(row, 1, boldText, StringConfig.getValue("reportSale.paidCashForGoodsSold"));
        createCell(row, 3, boldNumberRight, paidCashTotal);
        row = sheet.createRow(rowIndex++);
        createCell(row, 1, boldText, StringConfig.getValue("reportSale.paidBankForGoodsSold"));
        createCell(row, 3, boldNumberRight, paidBankTotal);
        row = sheet.createRow(rowIndex++);
        createCell(row, 1, boldText, StringConfig.getValue("reportSale.amountReturnedGoods"));
        createCell(row, 3, boldNumberRight, returnAmountTotal);
        row = sheet.createRow(rowIndex++);
        createCell(row, 1, boldText, StringConfig.getValue("reportSale.debt"));
        createCell(row, 3, boldNumberRight, debtTotal);
        row = sheet.createRow(rowIndex++);
        createCell(row, 1, boldText, StringConfig.getValue("reportSale.pawn"));
        createCell(row, 3, boldNumberRight, pawnTotal);

        if (expenseTotal != null) {
            row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, StringConfig.getValue("reportSale.expense"));
            createCell(row, 3, boldNumberRight, expenseTotal);
        }
        row = sheet.createRow(rowIndex++);
        createCell(row, 1, boldText, StringConfig.getValue("reportSale.cashbox"));
        createCell(row, 3, boldNumberRight, cashBoxTotal);


        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i, true);
        }

        String outputFilePath = getOutputFilePath(sheetName);
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

    public String rasxodPoClientamProduct(String title, String sheetName, Date fromDate, Date toDate, List<ReportGroupProperty> groups, ReportSaleService service, Integer currencyId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " + Time.toString(fromDate, Time.pattern) + " / " + Time.toString(toDate, Time.pattern));

        rowIndex++;

        List<List<ExcelItem>> headerConfig = new ArrayList<>();

        List<ExcelItem> one = new ArrayList<>();
        one.add(new ExcelItem(StringConfig.getValue("caption.no"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("product.groupName"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("product.name"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("sales.profit"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.rate"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.total"), null, null));
        headerConfig.add(one);

        Integer hRowSize = headerConfig.size();

        int[][] headerPosition = new int[rowIndex + hRowSize][14];

        rowIndex = createHeader(sheet, headerStyle, rowIndex, headerConfig, headerPosition);
        BigDecimal amount = BigDecimal.ZERO;
        for (ReportGroupProperty group : groups) {
            /*row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, group.getName());
            createCell(row, 2, boldNumber, group.getQty());
            createCell(row, 3, boldNumber, group.getProfit());
            createCell(row, 5, boldNumber, group.getTotal()); */


            List<ReportProductProperty> productList = service.listProduct(DealerClient.AccountType.DEBITOR, group.getId(), fromDate, toDate, currencyId);

            for (int i = 0; i < productList.size(); i++) {
                row = sheet.createRow(rowIndex++);
                ReportProductProperty product = productList.get(i);
                int colindex = 0;
                createCell(row, colindex++, null, i + 1);
                createCell(row, colindex++, null, group.getName());
                createCell(row, colindex++, null, product.getName());
                createCell(row, colindex++, numberStyle, product.getQty());
                createCell(row, colindex++, numberStyle, product.getProfit());
                createCell(row, colindex++, numberStyle, product.getRate());
                createCell(row, colindex++, numberStyle, product.getTotal());

                amount = amount.add(product.getTotal());

            }

//            rowIndex++;
        }
        row = sheet.createRow(rowIndex++);
        createCell(row, 2, boldText, StringConfig.getValue("label.total"));
        createCell(row, 6, boldNumber, amount);

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i, true);
        }

        String outputFilePath = getOutputFilePath(sheetName);
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

    public String prixodByProduct(String title, String sheetName, Date fromDate, Date toDate, List<ReportGroupProperty> groups, ReportSaleService service, Integer currencyId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " + Time.toString(fromDate, Time.pattern) + " / " + Time.toString(toDate, Time.pattern));

        rowIndex++;

        List<List<ExcelItem>> headerConfig = new ArrayList<>();

        List<ExcelItem> one = new ArrayList<>();
        one.add(new ExcelItem(StringConfig.getValue("caption.no"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("product.groupName"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("product.name"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.qty"), null, null));
//        one.add(new ExcelItem(StringConfig.getValue("sales.profit"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.rate"), null, null));
        one.add(new ExcelItem(StringConfig.getValue("label.total"), null, null));
        headerConfig.add(one);

        Integer hRowSize = headerConfig.size();

        int[][] headerPosition = new int[rowIndex + hRowSize][14];

        rowIndex = createHeader(sheet, headerStyle, rowIndex, headerConfig, headerPosition);

        BigDecimal amount = BigDecimal.ZERO;
        for (ReportGroupProperty group : groups) {
           /* row = sheet.createRow(rowIndex++);
            createCell(row, 1, boldText, group.getName());
            createCell(row, 2, boldNumber, group.getQty());
//            createCell(row, 3, boldNumber, group.getProfit());
            createCell(row, 4, boldNumber, group.getTotal());*/

            List<ReportProductProperty> productList = service.listProduct(DealerClient.AccountType.CREDITOR, group.getId(), fromDate, toDate, currencyId);

            for (int i = 0; i < productList.size(); i++) {
                row = sheet.createRow(rowIndex++);
                ReportProductProperty product = productList.get(i);
                int colindex = 0;
                createCell(row, colindex++, null, i + 1);
                createCell(row, colindex++, null, group.getName());
                createCell(row, colindex++, null, product.getName());
                createCell(row, colindex++, numberStyle, product.getQty());
//                createCell(row, colindex++, numberStyle, product.getProfit());
                createCell(row, colindex++, numberStyle, product.getRate());
                createCell(row, colindex++, numberStyle, product.getTotal());

                amount = amount.add(product.getTotal());

            }

//            rowIndex++;
        }
        row = sheet.createRow(rowIndex++);
        createCell(row, 2, boldText, StringConfig.getValue("label.total"));
        createCell(row, 5, boldNumber, amount);

        for (int i = 0; i < one.size(); i++) {
            sheet.autoSizeColumn(i, true);
        }

        String outputFilePath = getOutputFilePath(sheetName);
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


    public String salesByDate(String title, LocalDate fromdate, LocalDate todate, List<Map> groups, ReportSaleService service) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " +
                Time.toString(DateUtils.fromLocale(fromdate), Time.pattern) + " / " +
                Time.toString(DateUtils.fromLocale(todate), Time.pattern));

        rowIndex++;

        List<List<ExcelItem>> headerConfig = new ArrayList<>();

        List<ExcelItem> one = new ArrayList<>();
        List<ExcelItem> two = new ArrayList<>();
        List<ExcelItem> fields = new LinkedList<>();

        ExcelItem idField = new ExcelItem("id", StringConfig.getValue("caption.no"), 2, 1);
        one.add(idField);
        fields.add(idField);
        ExcelItem nameField = new ExcelItem("name", StringConfig.getValue("product.name"), 2, 1);
        one.add(nameField);
        fields.add(nameField);

        int count = 0;
        for (LocalDate date = fromdate; date.isBefore(todate) || date.isEqual(todate); date = date.plusDays(1)) {
            count++;
            one.add(new ExcelItem(Time.toString(DateUtils.fromLocale(date), Time.pattern), 1, 3));

            ExcelItem qtyField = new ExcelItem("qty_" + count, StringConfig.getValue("label.qty"), null, null);
            two.add(qtyField);
            fields.add(qtyField);
            ExcelItem altQtyField = new ExcelItem("alt_qty_" + count, StringConfig.getValue("sales.weight"), null, null);
            two.add(altQtyField);
            fields.add(altQtyField);
            ExcelItem amountField = new ExcelItem("amount_" + count, StringConfig.getValue("label.amount"), null, null);
            two.add(amountField);
            fields.add(amountField);
        }

        headerConfig.add(one);
        headerConfig.add(two);

        Integer hRowSize = headerConfig.size();

        int[][] headerPosition = new int[rowIndex + hRowSize][fields.size()];

        rowIndex = createHeader(sheet, headerStyle, rowIndex, headerConfig, headerPosition);

        for (Map group : groups) {

            row = sheet.createRow(rowIndex++);

            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getField() != null && fields.get(i).getField().equals("id"))
                    continue;
                if (group.get(fields.get(i).getField()) instanceof Number)
                    createCell(row, i, boldNumber, group.get(fields.get(i).getField()));
                else
                    createCell(row, i, boldText, group.get(fields.get(i).getField()));
            }


            List<Map> productList = service.salesByDateProduct(fromdate, todate, Invoice.Type.RASXOD_KLIENT, (Integer) group.get("id"));

            for (int i = 0; i < productList.size(); i++) {
                row = sheet.createRow(rowIndex++);
                Map product = productList.get(i);
                for (int k = 0; k < fields.size(); k++) {
                    if (fields.get(k).getField().equals("id")) {
                        createCell(row, k, null, i + 1);
                    } else {
                        if (product.get(fields.get(k).getField()) instanceof Number) {
                            if (product.get(fields.get(k).getField()) instanceof BigDecimal && BigDecimal.ZERO.compareTo((BigDecimal) product.get(fields.get(k).getField())) != 0)
                                createCell(row, k, numberStyle, product.get(fields.get(k).getField()));
                        } else
                            createCell(row, k, null, product.get(fields.get(k).getField()));

                    }
                }

            }

            rowIndex++;
        }

        for (int i = 0; i < fields.size(); i++) {
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
    }

    public String salesByMonth(String title, LocalDate fromdate, LocalDate todate, List<Map> groups, ReportSaleService service) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title);
        DataFormat dataFormat = workbook.createDataFormat();

        sheet.setDefaultColumnWidth((short) 15);
        CellStyle boldText = getTextStyleBold(workbook);
        CellStyle headerStyle = getStyleHeader2(workbook);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

        CellStyle boldNumber = getTextStyleBold(workbook);
        boldNumber.setDataFormat(dataFormat.getFormat("#,##0.00"));

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(rowIndex++);
        Cell period = row.createCell(1);
        period.setCellValue(StringConfig.getValue("label.date") + ": " +
                Time.toString(DateUtils.fromLocale(fromdate), Time.pattern) + " / " +
                Time.toString(DateUtils.fromLocale(todate), Time.pattern));

        rowIndex++;

        List<List<ExcelItem>> headerConfig = new ArrayList<>();

        List<ExcelItem> one = new ArrayList<>();
        List<ExcelItem> two = new ArrayList<>();
        List<ExcelItem> fields = new LinkedList<>();

        ExcelItem idField = new ExcelItem("id", StringConfig.getValue("caption.no"), 2, 1);
        one.add(idField);
        fields.add(idField);
        ExcelItem nameField = new ExcelItem("name", StringConfig.getValue("product.name"), 2, 1);
        one.add(nameField);
        fields.add(nameField);

        int count = 0;
        Date date = DateUtils.fromLocale(fromdate);
        Calendar start = Calendar.getInstance();
        start.setTime(new Date(date.getYear(), date.getMonth(), 1));

        Calendar end = Calendar.getInstance();
        end.setTime(DateUtils.fromLocale(todate));

        for (Calendar month = start; month.before(end) || month.equals(end); month.add(Calendar.MONTH, 1)) {
            count++;
            one.add(new ExcelItem(Time.monthFull(month.getTime()), 1, 3));

            ExcelItem qtyField = new ExcelItem("qty_" + count, StringConfig.getValue("label.qty"), null, null);
            two.add(qtyField);
            fields.add(qtyField);
            ExcelItem altQtyField = new ExcelItem("alt_qty_" + count, StringConfig.getValue("sales.weight"), null, null);
            two.add(altQtyField);
            fields.add(altQtyField);
            ExcelItem amountField = new ExcelItem("amount_" + count, StringConfig.getValue("label.amount"), null, null);
            two.add(amountField);
            fields.add(amountField);
        }

        headerConfig.add(one);
        headerConfig.add(two);

        Integer hRowSize = headerConfig.size();

        int[][] headerPosition = new int[rowIndex + hRowSize][fields.size()];

        rowIndex = createHeader(sheet, headerStyle, rowIndex, headerConfig, headerPosition);

        for (Map group : groups) {

            row = sheet.createRow(rowIndex++);

            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getField() != null && fields.get(i).getField().equals("id"))
                    continue;
                if (group.get(fields.get(i).getField()) instanceof Number)
                    createCell(row, i, boldNumber, group.get(fields.get(i).getField()));
                else
                    createCell(row, i, boldText, group.get(fields.get(i).getField()));
            }


            List<Map> productList = service.salesByMonthProduct(fromdate, todate, Invoice.Type.RASXOD_KLIENT, (Integer) group.get("id"));

            for (int i = 0; i < productList.size(); i++) {
                row = sheet.createRow(rowIndex++);
                Map product = productList.get(i);
                for (int k = 0; k < fields.size(); k++) {
                    if (fields.get(k).getField().equals("id")) {
                        createCell(row, k, null, i + 1);
                    } else {
                        if (product.get(fields.get(k).getField()) instanceof Number) {
                            if (product.get(fields.get(k).getField()) instanceof BigDecimal && BigDecimal.ZERO.compareTo((BigDecimal) product.get(fields.get(k).getField())) != 0)
                                createCell(row, k, numberStyle, product.get(fields.get(k).getField()));
                        } else
                            createCell(row, k, null, product.get(fields.get(k).getField()));

                    }
                }

            }

            rowIndex++;
        }

        for (int i = 0; i < fields.size(); i++) {
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
    }

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
