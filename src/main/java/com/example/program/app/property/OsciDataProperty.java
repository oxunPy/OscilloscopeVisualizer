package com.example.program.app.property;

import javafx.beans.property.*;

import java.util.Date;

public class OsciDataProperty {
    private ObjectProperty<Date> date = new SimpleObjectProperty<>();

    private StringProperty dataName = new SimpleStringProperty();

    private StringProperty toolName = new SimpleStringProperty();

    private StringProperty info = new SimpleStringProperty();

    private ObjectProperty<OsciFileProperty> dataFile = new SimpleObjectProperty<>(new OsciFileProperty());

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public String getDataName() {
        return dataName.get();
    }

    public StringProperty dataNameProperty() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName.set(dataName);
    }

    public String getToolName() {
        return toolName.get();
    }

    public StringProperty toolNameProperty() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName.set(toolName);
    }

    public String getInfo() {
        return info.get();
    }

    public StringProperty infoProperty() {
        return info;
    }

    public void setInfo(String info) {
        this.info.set(info);
    }

    public OsciFileProperty getDataFile() {
        return dataFile.get();
    }

    public ObjectProperty<OsciFileProperty> dataFileProperty() {
        return dataFile;
    }

    public void setDataFile(OsciFileProperty dataFile) {
        this.dataFile.set(dataFile);
    }
}
