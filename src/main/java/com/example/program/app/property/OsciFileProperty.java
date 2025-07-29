package com.example.program.app.property;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class OsciFileProperty {
    private StringProperty filename = new SimpleStringProperty();

    private StringProperty originalName = new SimpleStringProperty();

    public OsciFileProperty(){}


    public String getFilename() {
        return filename.get();
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename.set(filename);
    }

    public String getOriginalName() {
        return originalName.get();
    }

    public StringProperty originalNameProperty() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName.set(originalName);
    }
}
