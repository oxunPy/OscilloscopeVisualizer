package com.example.program.app.property;

import com.example.program.app.entity.OsciDataEntity;
import com.example.program.app.entity.OsciToolEntity;
import javafx.beans.property.*;

import java.util.Date;

public class OsciDataProperty extends BaseProperty {
    private LongProperty osciToolId = new SimpleLongProperty();

    private LongProperty osciFileId = new SimpleLongProperty();

    private ObjectProperty<Date> date = new SimpleObjectProperty<>();

    private StringProperty dataName = new SimpleStringProperty();

    private StringProperty info = new SimpleStringProperty();


    public OsciDataProperty newInstance(OsciDataEntity entity, boolean withUpdate){
        if(entity == null) return null;

        OsciDataProperty property = new OsciDataProperty();
        property.setDate(entity.getDate());
        property.setOsciFileId(entity.getOsciFileId());
        property.setOsciToolId(entity.getOsciToolId());
        property.setDataName(entity.getDataName());
        property.setInfo(entity.getInfo());

        if(withUpdate){
            property.setUpdatedDate(new Date());
        }

        return property;
    }

    public OsciDataEntity toEntity(boolean withUpdate){
        return toEntity(new OsciDataEntity(), withUpdate);
    }

    public OsciDataEntity toEntity(OsciDataEntity entity, boolean withUpdate){
        if(entity == null) return null;

        populateBase(entity);
        entity.setDate(getDate());
        entity.setOsciFileId(getOsciFileId());
        return entity;
    }



    public long getOsciToolId() {
        return osciToolId.get();
    }

    public LongProperty osciToolIdProperty() {
        return osciToolId;
    }

    public void setOsciToolId(long osciToolId) {
        this.osciToolId.set(osciToolId);
    }

    public long getOsciFileId() {
        return osciFileId.get();
    }

    public LongProperty osciFileIdProperty() {
        return osciFileId;
    }

    public void setOsciFileId(long osciFileId) {
        this.osciFileId.set(osciFileId);
    }

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

    public String getInfo() {
        return info.get();
    }

    public StringProperty infoProperty() {
        return info;
    }

    public void setInfo(String info) {
        this.info.set(info);
    }
}
