package com.example.program.app.property;

import com.example.program.app.entity.OsciSettingEntity;
import javafx.beans.property.*;

import java.util.Date;

public class SettingProperty extends BaseProperty{
    private StringProperty appName = new SimpleStringProperty();

    private StringProperty version = new SimpleStringProperty();

    private StringProperty techSupport = new SimpleStringProperty();

    private StringProperty authorName = new SimpleStringProperty();

    private StringProperty authorContact = new SimpleStringProperty();

    private StringProperty languageCode = new SimpleStringProperty();

    private LongProperty languageId = new SimpleLongProperty();

    public static SettingProperty newInstance(OsciSettingEntity entity, boolean withUpdate){
        if(entity == null) return null;

        SettingProperty settingProperty = new SettingProperty();
        settingProperty.populateBase(entity);

        settingProperty.setAppName(entity.getAppName());
        settingProperty.setVersion(entity.getVersion());
        settingProperty.setAuthorContact(entity.getAuthorContact());
        settingProperty.setAuthorName(entity.getAuthorName());
        settingProperty.setTechSupport(entity.getTechSupport());

        if(withUpdate){
            settingProperty.setUpdatedDate(new Date());
        }
        return settingProperty;
    }

    public OsciSettingEntity toEntity(boolean withUpdate){
        return toEntity(new OsciSettingEntity(), withUpdate);
    }

    public OsciSettingEntity toEntity(OsciSettingEntity entity, boolean withUpdate){
        baseEntity(entity);

        entity.setVersion(getVersion());
        entity.setAppName(getAppName());
        entity.setAuthorName(getAuthorName());
        entity.setAuthorContact(getAuthorContact());
        entity.setTechSupport(getTechSupport());

        if(withUpdate){
            entity.setUpdated(new Date());
        }
        return entity;
    }

    

    public String getAppName() {
        return appName.get();
    }

    public StringProperty appNameProperty() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName.set(appName);
    }

    public String getVersion() {
        return version.get();
    }

    public StringProperty versionProperty() {
        return version;
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public String getTechSupport() {
        return techSupport.get();
    }

    public StringProperty techSupportProperty() {
        return techSupport;
    }

    public void setTechSupport(String techSupport) {
        this.techSupport.set(techSupport);
    }

    public String getAuthorName() {
        return authorName.get();
    }

    public StringProperty authorNameProperty() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName.set(authorName);
    }

    public String getAuthorContact() {
        return authorContact.get();
    }

    public StringProperty authorContactProperty() {
        return authorContact;
    }

    public void setAuthorContact(String authorContact) {
        this.authorContact.set(authorContact);
    }

    public String getLanguageCode() {
        return languageCode.get();
    }

    public StringProperty languageCodeProperty() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode.set(languageCode);
    }

    public Long getLanguageId() {
        return languageId.get();
    }

    public LongProperty languageIdProperty() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId.set(languageId == null ? 0 : languageId);
    }
}
