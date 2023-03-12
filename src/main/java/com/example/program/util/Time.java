package com.example.program.util;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;

/**
 * Handling, formatting and clacing dates, mainly assisting in converting dates from the new java api from LocalDate
 * to Timestamp for insertion into the database
 *
 * @author Xurshidbek
 */
public class Time {

    public static final String pattern = "dd-MM-yyyy";
    public static final String time_pattern = "HH:mm";

    public static StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(pattern);

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    /**
     * Converts a LocalDate to Timestamp with zeroed times, ie midnight.
     */
    public static Timestamp toTimestamp(LocalDate date) {
        return Timestamp.valueOf(date.atStartOfDay());
    }

    /**
     * Convert Local Date Time to Timestamp
     */
    public static Timestamp toTimestamp(LocalDateTime date) {
        return Timestamp.valueOf(date);
    }

    /**
     * Convert String to Timestamp
     */
    public static Timestamp toTimestamp(String date) {
        return Timestamp.valueOf(LocalDateTime.parse(date, formatter("yyyy-MM-dd HH:mm.ss")));
    }

    /**
     * Converts String to Timestamp indicating the format
     */
    public static Timestamp toTimestamp(String date, String model) {
        return Timestamp.valueOf(LocalDateTime.parse(date, formatter(model)));
    }

    /**
     * Converts Timestamp to Local Date Time
     */
    public static LocalDateTime toDateTime(Timestamp time) {
        return time.toLocalDateTime();
    }

    /**
     * Converts Timestamp to Local Date
     */
    public static LocalDate toDate(Timestamp time) {
        return time.toLocalDateTime().toLocalDate();
    }

    /**
     * Converts Date to Local Date
     */
    public static LocalDate toDate(Date date) {
        return toDate(new Timestamp(date.getTime()));
    }

    /**
     * Converts String to LocalDate
     */
    public static LocalDate toDate(String date) {
        return LocalDate.parse(date, formatter("yyyy-MM-dd"));
    }

    /**
     * Converts String to LocalDate indicating the format
     */
    public static LocalDate toDate(String date, String model) {
        return LocalDate.parse(date, formatter(model));
    }

    /**
     * Transform Date into String
     */
    public static String toString(Date date, boolean onlyDate) {
        if (date == null)
            return "";
        if (onlyDate)
            return new SimpleDateFormat("dd.MM.yyyy").format(date);
        else
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
    }

    /**
     * Transform Date into String
     */
    public static String toString(Date date, String pattern) {
        return date == null ? "" : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Transform Timestamp into String
     */
    public static String toString(Timestamp date) {
        return date == null ? "" : date.toLocalDateTime().format(formatter("dd/MM/yyyy"));
    }

    /**
     * Transform Timestamp into String format indicated
     */
    public static String toString(Timestamp date, String model) {
        return date == null ? "" : date.toLocalDateTime().format(formatter(model));
    }

    /**
     * Converts LocalDateTime to String in dd/MM/yyyy format
     */
    public static String toString(LocalDate date) {
        return date == null ? "" : date.format(formatter("dd/MM/yyyy"));
    }

    /**
     * Converts LocalDateTime to String indicating toString format
     */
    public static String toString(LocalDate date, String model) {
        return date == null ? "" : date.format(formatter(model));
    }

    /**
     * Converts LocalDateTime to String indicating toString format
     */
    public static String toString(LocalDateTime date, String model) {
        return date == null ? "" : date.format(formatter(model));
    }

    /**
     * Informs the date of the number month and returns its abbreviated name in string Ex: 1->JAN, 2->FEV, 3->MAR ... 12->DEC
     */
    public static String month(String date) {
        return Month.of(Integer.parseInt(date)).getDisplayName(TextStyle.SHORT, StringConfig.getAppLocale()).toUpperCase();
    }

    public static String monthFull(Date date) {
        return Month.of(date.getMonth() + 1).getDisplayName(TextStyle.FULL, StringConfig.getAppLocale()).toUpperCase();
    }

    /**
     * Returns current date in timestamp format
     */
    public static Timestamp current() {
        return toTimestamp(LocalDate.now());
    }

    /**
     * Generate a formatter for formatting toDate in string in the format indicated
     */
    private static DateTimeFormatter formatter(String model) {
        return DateTimeFormatter.ofPattern(model);
    }

    /**
     * Block days from previous DatePickers to the informed leaving in red dates not selectable
     */
    public static void blockPreviousDate(LocalDate date, DatePicker calendar) {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {

                        super.updateItem(item, empty);

                        if (item.isBefore(date.plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc8c3;");
                        }
                    }
                };
            }
        };

        calendar.setDayCellFactory(dayCellFactory);
        calendar.setValue(date.plusDays(1));
    }
}
