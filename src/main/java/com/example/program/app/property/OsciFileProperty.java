package com.example.program.app.property;

import com.example.program.app.entity.OsciFileEntity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class OsciFileProperty extends BaseProperty{
    private StringProperty filename = new SimpleStringProperty();

    private StringProperty originalName = new SimpleStringProperty();

    private ObjectProperty<OsciFileEntity.FileType> fileType = new SimpleObjectProperty<>();

    public OsciFileProperty(OsciFileEntity.FileType fileType){
        setFileType(fileType);
    }

    public OsciFileProperty(){}

    public static OsciFileProperty newInstance(OsciFileEntity entity, boolean withUpdate){
        if(entity == null) return null;

        OsciFileProperty property = new OsciFileProperty();
        property.populateBase(entity);

        property.setFilename(entity.getFilename());
        property.setOriginalName(entity.getOriginalName());
        property.setFileType(entity.getFileType());

        if(withUpdate){
            property.setUpdatedDate(new Date());
        }
        return property;
    }

    public OsciFileEntity toEntity(boolean withUpdate){
        return toEntity(new OsciFileEntity(), withUpdate);
    }

    public OsciFileEntity toEntity(OsciFileEntity entity, boolean withUpdate){
        if(entity == null) entity = new OsciFileEntity();
        baseEntity(entity);

        entity.setFilename(getFilename());
        entity.setOriginalName(getOriginalName());
        entity.setFileType(getFileType());

        if(withUpdate){
            entity.setUpdated(new Date());
        }
        return entity;
    }


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

    public OsciFileEntity.FileType getFileType() {
        return fileType.get();
    }

    public ObjectProperty<OsciFileEntity.FileType> fileTypeProperty() {
        return fileType;
    }

    public void setFileType(OsciFileEntity.FileType fileType) {
        this.fileType.set(fileType);
    }
}
