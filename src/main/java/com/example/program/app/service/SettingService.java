package com.example.program.app.service;

import com.example.program.app.entity.SettingEntity;
import com.example.program.app.property.SettingProperty;
import com.example.program.common.service.BaseService;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;

public class SettingService extends BaseService {

    private final LogUtil log = LogUtil.getLog(this.getClass());


    public SettingProperty getApplicationSetting(){
        openCurrentSession();
        SettingProperty property = null;
        try{
            String hql = "from SettingEntity where id > ?";
            SettingEntity entity = settingDao.findFirst(hql, new Object[]{});
            property = SettingProperty.newInstance(entity, false);
        }catch(Exception ex){
            log.print(StringConfig.getValue("err.db.setting") + ex.getMessage());
        }
        closeCurrentSession();
        return property;
    }


    public Integer insert(SettingProperty settingProperty){
        return 0;
    }
}
