package com.example.program.app.service;

import com.example.program.app.entity.OsciUserEntity;
import com.example.program.app.property.UserProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;
import com.example.program.util.exception.UnsafeUpdateException;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserService extends BaseService {
    private final LogUtil log = LogUtil.getLog(this.getClass());

    public Integer insert(OsciUserEntity osciUserEntity){
        openCurrentSessionWithTransaction();
        userDao.saveOrUpdate(osciUserEntity);
        closeCurrentSessionWithTransaction();
        return osciUserEntity.getId();
    }

    public OsciUserEntity getByLogin(String login){
        openCurrentSession();

        OsciUserEntity entity = userDao.get("from OsciUserEntity where login=?", new Object[]{login});

        closeCurrentSession();
        return entity;
    }

    public OsciUserEntity getUser(){
        List<OsciUserEntity> entities = list();
        if(entities.size() == 0) return null;
        if(entities.size() == 1) return entities.get(0);
        throw new UnsafeUpdateException("Only one adminUser must be active.", this.getClass());
    }

    public Boolean getAuthSet(){
        openCurrentSession();

        Boolean result = false;
        try{
            String sql = "SELECT authset FROM osci_user";
            result = MapUtils.getBoolean(userDao.getSQL(sql, new HashMap<>()), "authset", false);
        } catch(Exception ex){
            log.print(StringConfig.getValue("err.db.get") + "\n" + ex);
        }
        closeCurrentSession();
        return result;
    }

    public List<OsciUserEntity> list(){
        openCurrentSession();

        List<OsciUserEntity> entities = new ArrayList<>();
        try{
            entities = userDao.find("from OsciUserEntity where status=? ORDER BY id", new Object[]{EntityStatus.ACTIVE});
        } catch(Exception ex){
            log.print(StringConfig.getValue("err.db.list") + "\n" + ex);
        }
        closeCurrentSession();
        return entities;
    }

    public UserProperty userProperty(Integer id){
        return null;
    }

    public Integer setAdminAuth(String login, String password){
        openCurrentSessionWithTransaction();
        Integer result = 0;
        try{
            String sql = "UPDATE osci_user \n" +
                         "SET login = ?, pass = ?, authset = ? \n" +
                         "WHERE 1 = 1";
            result = userDao.executeSql(sql, new Object[]{login, password, Boolean.TRUE});
        } catch(Exception ex){

        }
        closeCurrentSessionWithTransaction();
        return result;
    }
}
