package com.example.program.app.property;

import com.example.program.app.entity.OsciToolEntity;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class OsciToolProperty extends BaseProperty{
    private StringProperty name = new SimpleStringProperty();

    private StringProperty characteristics = new SimpleStringProperty();

    private StringProperty info = new SimpleStringProperty();

    private LongProperty imageFileId = new SimpleLongProperty();


    public static OsciToolProperty newInstance(OsciToolEntity entity, boolean withUpdate){
        if(entity == null) return null;

        OsciToolProperty property = new OsciToolProperty();
        property.populateBase(entity);

        property.setName(entity.getName());
        property.setCharacteristics(entity.getCharacteristics());
        property.setImageFileId(entity.getImageFileId());
        property.setInfo(entity.getInfo());

        if(withUpdate){
            property.setUpdatedDate(new Date());
        }
        return property;
    }

    public OsciToolEntity toEntity(boolean withUpdate){
        return toEntity(new OsciToolEntity(), withUpdate);
    }

    public OsciToolEntity toEntity(OsciToolEntity entity, boolean withUpdate){
        baseEntity(entity);
        entity.setName(getName());
        entity.setCharacteristics(getCharacteristics());
        entity.setImageFileId(getImageFileId());
        entity.setInfo(entity.getInfo());

        if(withUpdate){
            entity.setUpdated(new Date());
        }
        return entity;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCharacteristics() {
        return characteristics.get();
    }

    public StringProperty characteristicsProperty() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics.set(characteristics);
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

    public long getImageFileId() {
        return imageFileId.get();
    }

    public LongProperty imageFileIdProperty() {
        return imageFileId;
    }

    public void setImageFileId(Long imageFileId) {
        this.imageFileId.set(imageFileId == null ? 0 : imageFileId);
    }
}
