package com.example.program.app.property;

import com.example.program.app.entity.OsciDataEntity;
import com.example.program.app.entity.OsciToolEntity;
import com.example.program.common.status.EntityStatus;
import javafx.beans.property.*;

import java.util.Date;

public class OsciDataProperty extends BaseProperty {
    private LongProperty osciToolId = new SimpleLongProperty();

    private LongProperty osciFileId = new SimpleLongProperty();

    private ObjectProperty<Date> date = new SimpleObjectProperty<>();

    private StringProperty dataName = new SimpleStringProperty();

    private StringProperty info = new SimpleStringProperty();

    private ObjectProperty<OsciFileProperty> dataFile = new SimpleObjectProperty<>(new OsciFileProperty());

    public OsciDataProperty(boolean withFile){
        if(withFile){
            setDataFile(new OsciFileProperty());
        }
    }

    public OsciDataProperty(){}

    public static OsciDataProperty newInstance(OsciDataEntity entity, boolean withUpdate){
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
        entity.setInfo(getInfo());
        entity.setDataName(getDataName());
        entity.setOsciToolId(getOsciToolId());
        entity.setOsciFileId(getOsciFileId());

        if(withUpdate){
            entity.setStatus(EntityStatus.UPDATED);
            entity.setUpdated(new Date());
        }
        return entity;
    }



    public long getOsciToolId() {
        return osciToolId.get();
    }

    public LongProperty osciToolIdProperty() {
        return osciToolId;
    }

    public void setOsciToolId(Long osciToolId) {
        this.osciToolId.set(osciToolId == null ? 0 : osciToolId);
    }

    public long getOsciFileId() {
        return osciFileId.get();
    }

    public LongProperty osciFileIdProperty() {
        return osciFileId;
    }

    public void setOsciFileId(Long osciFileId) {
        this.osciFileId.set(osciFileId == null ? 0 : osciFileId);
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
