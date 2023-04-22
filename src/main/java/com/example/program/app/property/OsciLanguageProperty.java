package com.example.program.app.property;

import com.example.program.app.entity.OsciLanguageEntity;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class OsciLanguageProperty extends BaseProperty{
    private StringProperty name = new SimpleStringProperty();

    private StringProperty code = new SimpleStringProperty();

    private LongProperty flagFileId = new SimpleLongProperty();

    public String getName() {
        return name.get();
    }

    public static OsciLanguageProperty newInstance(OsciLanguageEntity entity, boolean withUpdate){
        if(entity == null) return null;
        OsciLanguageProperty property = new OsciLanguageProperty();
        property.populateBase(entity);

        property.setCode(entity.getCode());
        property.setName(entity.getName());

        if(withUpdate){
            property.setUpdatedDate(new Date());
        }
        return property;
    }

    public OsciLanguageEntity toEntity(boolean withUpdate){
        return toEntity(new OsciLanguageEntity(), withUpdate);
    }

    public OsciLanguageEntity toEntity(OsciLanguageEntity entity, boolean withUpdate){
        if(entity == null) entity = new OsciLanguageEntity();
        baseEntity(entity);
        entity.setName(getName());
        entity.setCode(getCode());

        if(withUpdate){
            entity.setUpdated(new Date());
        }
        return entity;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCode() {
        return code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public long getFlagFileId() {
        return flagFileId.get();
    }

    public LongProperty flagFileIdProperty() {
        return flagFileId;
    }

    public void setFlagFileId(Long flagFileId) {
        this.flagFileId.set(flagFileId == null ? 0 : flagFileId);
    }
}
