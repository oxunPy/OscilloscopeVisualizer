package com.example.program.app.property;

import com.example.program.app.entity.OsciFileEntity;
import com.example.program.app.entity.OsciToolEntity;
import javafx.beans.property.*;

import java.util.Date;

public class OsciToolProperty extends BaseProperty{
    private StringProperty name = new SimpleStringProperty();

    private StringProperty model = new SimpleStringProperty();

    private StringProperty info = new SimpleStringProperty();

    private LongProperty imageId = new SimpleLongProperty();

    private ObjectProperty<OsciFileProperty> toolImage = new SimpleObjectProperty<>(new OsciFileProperty(OsciFileEntity.FileType.IMG));


    public static OsciToolProperty newInstance(OsciToolEntity entity, boolean withImageFile){
        if(entity == null) return null;

        OsciToolProperty property = new OsciToolProperty();
        property.populateBase(entity);

        property.setName(entity.getName());
        property.setInfo(entity.getInfo());
        property.setModel(entity.getModel());
        property.setImageId(entity.getImageFileId());

        if(withImageFile){
            property.setToolImage(new OsciFileProperty(OsciFileEntity.FileType.IMG));
        }
        return property;
    }

    public OsciToolEntity toEntity(boolean withUpdate){
        return toEntity(new OsciToolEntity(), withUpdate);
    }

    public OsciToolEntity toEntity(OsciToolEntity entity, boolean withUpdate){
        baseEntity(entity);
        entity.setName(getName());
        entity.setInfo(getInfo());
        entity.setImageFileId(getImageId());
        entity.setModel(getModel());

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

    public String getInfo() {
        return info.get();
    }

    public StringProperty infoProperty() {
        return info;
    }

    public void setInfo(String info) {
        this.info.set(info);
    }

    public OsciFileProperty getToolImage() {
        return toolImage.get();
    }

    public ObjectProperty<OsciFileProperty> toolImageProperty() {
        return toolImage;
    }

    public void setToolImage(OsciFileProperty toolImage) {
        this.toolImage.set(toolImage);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public long getImageId() {
        return imageId.get();
    }

    public LongProperty imageIdProperty() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId.set(imageId == null ? 0 : imageId);
    }
}
