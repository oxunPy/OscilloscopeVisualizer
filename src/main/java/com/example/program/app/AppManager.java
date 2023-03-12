package com.example.program.app;

import com.example.program.app.property.DeviceProperty;
import com.example.program.app.property.SettingProperty;
import com.example.program.app.property.UserProperty;

public class AppManager {

    private static AppManager instance;

    public static AppManager getInstance(){
        if(instance == null){
            instance = new AppManager();
        }
        return instance;
    }

    private UserProperty user;

    private SettingProperty setting;

    private DeviceProperty device;

    public static class Builder{
        private UserProperty user;
        private SettingProperty setting;
        private DeviceProperty device;

        public Builder user(UserProperty user){
            this.user = user;
            return this;
        }

        public Builder setting(SettingProperty setting){
            this.setting = setting;
            return this;
        }

        public Builder device(DeviceProperty device){
            this.device = device;
            return this;
        }

        public AppManager build(){
            AppManager appManager = AppManager.getInstance();
            appManager.user = user;
            appManager.setting = setting;
            appManager.device = device;
            return appManager;
        }
    }

    public UserProperty getLoggedUser(){
        return user;
    }

    public void setLoggedUser(UserProperty user){
        this.user = user;
    }

    public SettingProperty getSetting(){
        return setting;
    }

    public DeviceProperty getDevice(){return device;}

    public void setDevice(DeviceProperty device){this.device = device;}

    public void setApplicationSetting(SettingProperty setting){
        this.setting = setting;
    }
}
