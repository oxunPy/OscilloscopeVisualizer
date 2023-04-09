package com.example.program.app.service;

import com.example.program.app.entity.OsciDeviceEntity;
import com.example.program.app.property.DeviceProperty;
import com.example.program.common.service.BaseService;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;

public class OsciDeviceService extends BaseService {

    private LogUtil log = LogUtil.getLog(this.getClass());

    public DeviceProperty getDevice(){
        openCurrentSession();
        try{
            String hql = "from DeviceEntity where 1=1";
            OsciDeviceEntity entity = deviceDao.get(hql, new Object[]{});
            return DeviceProperty.newInstance(entity, false);
        } catch(Exception ex){
            log.print(StringConfig.getValue("err.db.get"));
        }
        closeCurrentSession();
        return null;
    }


    public Integer insert(DeviceProperty property){
        openCurrentSessionWithTransaction();
        Integer result = 0;
        try{
            OsciDeviceEntity entity = property.toEntity(false);
            deviceDao.saveOrUpdate(entity);
            result = entity.getId();
        }catch (Exception ex){
            log.print(StringConfig.getValue("err.db.insert"));
        }
        closeCurrentSessionWithTransaction();
        return result;
    }
}
