package com.example.program.app.service;

import com.example.program.app.entity.OsciSettingEntity;
import com.example.program.app.property.SettingProperty;
import com.example.program.common.service.BaseService;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;

public class OsciSettingService extends BaseService {

    private final LogUtil log = LogUtil.getLog(this.getClass());


    public SettingProperty getApplicationSetting(){
        openCurrentSession();
        SettingProperty property = null;
        try{
            String hql = "from SettingEntity where id > ?";
            OsciSettingEntity entity = settingDao.findFirst(hql, new Object[]{0});
            property = SettingProperty.newInstance(entity, false);
        }catch(Exception ex){
            log.print(StringConfig.getValue("err.db.setting") + ex.getMessage());
        }
        closeCurrentSession();
        return property;
    }


    public Integer insert(SettingProperty settingProperty){
        openCurrentSessionWithTransaction();
        OsciSettingEntity entity = settingProperty.toEntity(new OsciSettingEntity(), false);
        settingDao.saveOrUpdate(entity);
        closeCurrentSessionWithTransaction();
        return entity.getId();
    }
}
