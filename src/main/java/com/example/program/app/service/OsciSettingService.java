package com.example.program.app.service;

import com.example.program.app.entity.OsciLanguageEntity;
import com.example.program.app.entity.OsciSettingEntity;
import com.example.program.app.property.SettingProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;
import org.apache.commons.collections.MapUtils;
import org.h2.engine.Setting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public SettingProperty getApplicationSetting(boolean withLangCode){
        if(!withLangCode) return getApplicationSetting();

        openCurrentSession();
        String sql = "select os.*, ol.id as languageId, ol.code as languageCode\n" +
                     "from osci_setting os\n" +
                     "left join osci_language ol on os.default_language_id = ol.id\n" +
                     "where os.status <> 'DELETED' and ol.status <> 'DELETED'";
        Map map = settingDao.getSQL(sql, new HashMap<>());
        SettingProperty setting = new SettingProperty();
        setting.setId(MapUtils.getInteger(map, "id"));
        setting.setStatus(EntityStatus.valueOf(MapUtils.getString(map, "status")));
        setting.setAppName(MapUtils.getString(map, "appname"));
        setting.setAuthorContact(MapUtils.getString(map, "authorcontact"));
        setting.setAuthorName(MapUtils.getString(map, "authorname"));
        setting.setLanguageId(MapUtils.getInteger(map, "default_language_id"));
        setting.setTechSupport(MapUtils.getString(map, "techsupport"));
        setting.setVersion(MapUtils.getString(map, "version"));
        setting.setLanguageCode(MapUtils.getString(map, "languagecode"));
        closeCurrentSession();
        return setting;
    }


    public boolean hasLanguageByCode(String code){
        String hql = "from OsciLanguageEntity where code = ?";
        Long count = languageDao.count(hql, new Object[]{"ru"});
        return count > 0;
    }

    public Integer insertDefaultLanguage(String code){
        openCurrentSessionWithTransaction();
        OsciLanguageEntity entity = new OsciLanguageEntity();
        entity.setCode("ru");
        entity.setCreated(new Date());
        entity.setName("Русский язык");
        languageDao.save(entity);
        closeCurrentSessionWithTransaction();
        return entity.getId();
    }

    public Integer insert(SettingProperty settingProperty){
        openCurrentSessionWithTransaction();
        OsciSettingEntity entity = settingProperty.toEntity(new OsciSettingEntity(), false);
        settingDao.saveOrUpdate(entity);
        closeCurrentSessionWithTransaction();
        return entity.getId();
    }
}
