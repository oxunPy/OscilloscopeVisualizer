package com.example.program.app.property;

import com.example.program.app.entity.UserEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class UserProperty extends BaseProperty{
    private StringProperty firstName = new SimpleStringProperty();

    private StringProperty middleName = new SimpleStringProperty();

    private StringProperty lastName = new SimpleStringProperty();

    private StringProperty printableName = new SimpleStringProperty();

    private StringProperty login = new SimpleStringProperty();

    private StringProperty pass = new SimpleStringProperty();

    private StringProperty info = new SimpleStringProperty();

    public static UserProperty newInstance(UserEntity userEntity, boolean withPassword, boolean withCreatedAndUpdated){
        if(userEntity == null) return null;
        UserProperty userProperty = new UserProperty();
        userProperty.populateBase(userEntity);

        userProperty.setFirstName(userEntity.getFirstName());
        userProperty.setLastName(userEntity.getLastName());
        userProperty.setMiddleName(userEntity.getMiddleName());
        userProperty.setInfo(userEntity.getInfo());
        userProperty.setPrintableName(userEntity.getPrintableName());

        if(withPassword){
            userProperty.setPass(userEntity.getPass());
        }

        if(withCreatedAndUpdated){
            userProperty.setCreatedDate(userEntity.getCreated());
            userProperty.setUpdatedDate(userEntity.getUpdated());
        }
        return userProperty;
    }

    public UserEntity toEntity(boolean updatePassword, boolean updateCreateAndUpdate){
        return toEntity(new UserEntity(), updatePassword, updateCreateAndUpdate);
    }

    public UserEntity toEntity(UserEntity user, boolean updatePassword, boolean withUpdate){
        baseEntity(user);

        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setMiddleName(getMiddleName());
        user.setPrintableName(getPrintableName());
        user.setLogin(getLogin());
        user.setPass(getPass());
        user.setInfo(getInfo());

        if(updatePassword){
            user.setPass(getPass());
        }

        if(withUpdate){
            user.setUpdated(new Date());
        }
        return user;
    }


    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public StringProperty middleNameProperty() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPrintableName() {
        return printableName.get();
    }

    public StringProperty printableNameProperty() {
        return printableName;
    }

    public void setPrintableName(String printableName) {
        this.printableName.set(printableName);
    }

    public String getLogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPass() {
        return pass.get();
    }

    public StringProperty passProperty() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass.set(pass);
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
