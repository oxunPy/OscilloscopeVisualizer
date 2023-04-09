package com.example.program.app.property;

import com.example.program.app.entity.OsciDeviceEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class DeviceProperty extends BaseProperty{
    private StringProperty pcName = new SimpleStringProperty();

    private StringProperty pcOwner = new SimpleStringProperty();

    private StringProperty hdd = new SimpleStringProperty();

    private StringProperty cpu = new SimpleStringProperty();

    private StringProperty motherboard = new SimpleStringProperty();


    public static DeviceProperty newInstance(OsciDeviceEntity entity, boolean withUpdate){
        if(entity == null) return null;

        DeviceProperty deviceProperty = new DeviceProperty();
        deviceProperty.populateBase(entity);
        deviceProperty.setPcName(entity.getPcName());
        deviceProperty.setCpu(entity.getCpu());
        deviceProperty.setHdd(entity.getHdd());
        deviceProperty.setPcOwner(entity.getPcOwner());
        deviceProperty.setMotherboard(entity.getMotherboard());

        if(withUpdate){
            deviceProperty.setUpdatedDate(new Date());
        }
        return deviceProperty;
    }

    public OsciDeviceEntity toEntity(boolean withUpdate){
        return toEntity(new OsciDeviceEntity(), withUpdate);
    }

    public OsciDeviceEntity toEntity(OsciDeviceEntity entity, boolean withUpdate){
        baseEntity(entity);
        entity.setCpu(getCpu());
        entity.setHdd(getHdd());
        entity.setMotherboard(getMotherboard());
        entity.setPcOwner(getPcOwner());
        entity.setPcName(getPcName());
        return entity;
    }

    public String getPcName() {
        return pcName.get();
    }

    public StringProperty pcNameProperty() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName.set(pcName);
    }

    public String getPcOwner() {
        return pcOwner.get();
    }

    public StringProperty pcOwnerProperty() {
        return pcOwner;
    }

    public void setPcOwner(String pcOwner) {
        this.pcOwner.set(pcOwner);
    }

    public String getHdd() {
        return hdd.get();
    }

    public StringProperty hddProperty() {
        return hdd;
    }

    public void setHdd(String hdd) {
        this.hdd.set(hdd);
    }

    public String getCpu() {
        return cpu.get();
    }

    public StringProperty cpuProperty() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu.set(cpu);
    }

    public String getMotherboard() {
        return motherboard.get();
    }

    public StringProperty motherboardProperty() {
        return motherboard;
    }

    public void setMotherboard(String motherboard) {
        this.motherboard.set(motherboard);
    }
}
