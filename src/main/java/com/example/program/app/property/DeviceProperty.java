package com.example.program.app.property;

import com.example.program.app.entity.DeviceEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class DeviceProperty extends BaseProperty{
    private StringProperty pcName = new SimpleStringProperty();

    private StringProperty licenseKey = new SimpleStringProperty();

    public static DeviceProperty newInstance(DeviceEntity entity, boolean withUpdate){
        if(entity == null) return null;

        DeviceProperty deviceProperty = new DeviceProperty();
        deviceProperty.populateBase(entity);
        deviceProperty.setPcName(entity.getPcName());
        deviceProperty.setLicenseKey(entity.getLicenseKey());

        if(withUpdate){
            deviceProperty.setUpdatedDate(new Date());
        }
        return deviceProperty;
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

    public String getLicenseKey() {
        return licenseKey.get();
    }

    public StringProperty licenseKeyProperty() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey.set(licenseKey);
    }
}
