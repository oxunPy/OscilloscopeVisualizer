package com.example.program.util.excel;

import java.math.BigDecimal;

/**
 * Created by Xurshidbek on 09.06.2016.
 */
public class ExcelItem {

    private String field;
    private String title;
    private Integer rowspan;
    private Integer colspan;

    public ExcelItem() {

    }

    public ExcelItem(String field, String title, Integer rowspan, Integer colspan) {
        this.field = field;
        this.title = title;
        this.rowspan = rowspan;
        this.colspan = colspan;
    }

    public ExcelItem(String title, Integer rowspan, Integer colspan) {
        this.title = title;
        this.rowspan = rowspan;
        this.colspan = colspan;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }


    public static class DealerClient {
        Integer number;
        String name;
        String printableName;
        String phone;
        BigDecimal openingBalance;
        String info;
        BigDecimal debit;
        BigDecimal credit;
        BigDecimal buyAmount;
        BigDecimal payAmount;
        BigDecimal debt;

        public DealerClient() {

        }

        public DealerClient(Integer number, String name, String phone, BigDecimal openingBalance, String info) {
            this.name = name;
            this.phone = phone;
            this.openingBalance = openingBalance;
            this.info = info;
            this.number = number;
        }

        public DealerClient(Integer number, String name, String printableName, String phone, BigDecimal openingBalance, String info) {
            this.name = name;
            this.phone = phone;
            this.printableName = printableName;
            this.openingBalance = openingBalance;
            this.info = info;
            this.number = number;
        }

        public DealerClient(Integer number, String name, String phone, String info, BigDecimal debit, BigDecimal credit) {
            this.number = number;
            this.name = name;
            this.phone = phone;
            this.info = info;
            this.debit = debit;
            this.credit = credit;
        }

        public DealerClient(Integer number, String name, BigDecimal payAmount, BigDecimal buyAmount, BigDecimal debt) {
            this.number = number;
            this.name = name;
            this.payAmount = payAmount;
            this.buyAmount = buyAmount;
            this.debt = debt;
        }


        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public BigDecimal getOpeningBalance() {
            return openingBalance;
        }

        public String getPrintableName() {
            return printableName;
        }

        public String getInfo() {
            return info;
        }

        public BigDecimal getDebit() {
            return debit;
        }

        public BigDecimal getCredit() {
            return credit;
        }

        public Integer getNumber() {
            return number;
        }

        public BigDecimal getBuyAmount() {
            return buyAmount;
        }

        public BigDecimal getPayAmount() {
            return payAmount;
        }

        public BigDecimal getDebt() {
            return debt;
        }
    }

    public static class Product {
        Integer number;
        String name;
        String groupName;
        String date;
        BigDecimal qty;
        BigDecimal newRate;
        BigDecimal oldRate;
        BigDecimal amount;

        public Product() {

        }

        public Product(Integer number, String name, String groupName, String date, BigDecimal qty, BigDecimal oldRate, BigDecimal newRate, BigDecimal amount) {
            this.number = number;
            this.name = name;
            this.groupName = groupName;
            this.date = date;
            this.qty = qty;
            this.newRate = newRate;
            this.oldRate = oldRate;
            this.amount = amount;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public BigDecimal getQty() {
            return qty;
        }

        public void setQty(BigDecimal qty) {
            this.qty = qty;
        }

        public BigDecimal getNewRate() {
            return newRate;
        }

        public void setNewRate(BigDecimal newRate) {
            this.newRate = newRate;
        }

        public BigDecimal getOldRate() {
            return oldRate;
        }

        public void setOldRate(BigDecimal oldRate) {
            this.oldRate = oldRate;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public static class ClientHistory {
        String vchDate;
        String vchTime;
        String particular;
        String invoiceType;
        Integer voucherNumber;
        String info;
        Amount debit;
        Amount credit;

        public ClientHistory() {

        }

        public ClientHistory(String vchDate, String vchTime, String particular, String invoiceType, Integer voucherNumber, String info, Amount debit, Amount credit) {
            this.vchDate = vchDate;
            this.vchTime = vchTime;
            this.particular = particular;
            this.invoiceType = invoiceType;
            this.voucherNumber = voucherNumber;
            this.info = info;
            this.debit = debit;
            this.credit = credit;
        }

        public String getVchDate() {
            return vchDate;
        }

        public void setVchDate(String vchDate) {
            this.vchDate = vchDate;
        }

        public String getVchTime() {
            return vchTime;
        }

        public void setVchTime(String vchTime) {
            this.vchTime = vchTime;
        }

        public String getParticular() {
            return particular;
        }

        public void setParticular(String particular) {
            this.particular = particular;
        }

        public String getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceType(String invoiceType) {
            this.invoiceType = invoiceType;
        }

        public Integer getVoucherNumber() {
            return voucherNumber;
        }

        public void setVoucherNumber(Integer voucherNumber) {
            this.voucherNumber = voucherNumber;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public Amount getDebit() {
            return debit;
        }

        public void setDebit(Amount debit) {
            this.debit = debit;
        }

        public Amount getCredit() {
            return credit;
        }

        public void setCredit(Amount credit) {
            this.credit = credit;
        }
    }

    public static class PaymentReceipt{
        private Integer id;
        private String client;
        private String account;
        private String info;
        private String date;
        private Amount amount;

        public PaymentReceipt(Integer id, String client, String account, String info, String date, Amount amount) {
            this.id = id;
            this.client = client;
            this.account = account;
            this.info = info;
            this.date = date;
            this.amount = amount;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Amount getAmount() {
            return amount;
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }
    }

    public static class Amount {
        private BigDecimal value;
        private String currencyCode;

        public Amount(BigDecimal value, String currencyCode) {
            this.value = value;
            this.currencyCode = currencyCode;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }
    }

    public static class ProductProperty{

        private int id = 1;
        private String productName = " ";
        private String barcode = " ";
        private String groupName = " ";
        private String baseUnit = " ";
        private String alternateUnit = " ";
        private String baseUnitVal = " ";
        private String alternateUnitVal = " ";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getBaseUnit() {
            return baseUnit;
        }

        public void setBaseUnit(String baseUnit) {
            this.baseUnit = baseUnit;
        }

        public String getAlternateUnit() {
            return alternateUnit;
        }

        public void setAlternateUnit(String alternateUnit) {
            this.alternateUnit = alternateUnit;
        }

        public String getBaseUnitVal() {
            return baseUnitVal;
        }

        public void setBaseUnitVal(String baseUnitVal) {
            this.baseUnitVal = baseUnitVal;
        }

        public String getAlternateUnitVal() {
            return alternateUnitVal;
        }

        public void setAlternateUnitVal(String alternateUnitVal) {
            this.alternateUnitVal = alternateUnitVal;
        }

    }
}
