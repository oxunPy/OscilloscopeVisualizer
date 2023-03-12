package com.example.program.app.service;

import com.example.program.app.entity.UserEntity;
import com.example.program.app.property.UserProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;
import com.example.program.util.exception.UnsafeUpdateException;

import java.util.ArrayList;
import java.util.List;

public class UserService extends BaseService {
    private final LogUtil log = LogUtil.getLog(this.getClass());

    public Integer insert(UserEntity userEntity){
        return 0;
    }

    public UserEntity getByLogin(String login){
        openCurrentSession();

        UserEntity entity = userDao.get("from UserEntity where login=?", new Object[]{login});

        closeCurrentSession();
        return entity;
    }

    public UserEntity getUser(){
        List<UserEntity> entities = list();
        if(entities.size() == 0) return null;
        if(entities.size() == 1) return entities.get(0);
        throw new UnsafeUpdateException("Only one dealer must be active.", this.getClass());
    }

    public List<UserEntity> list(){
        openCurrentSession();

        List<UserEntity> entities = new ArrayList<>();
        try{
            entities = userDao.find("form UserEntity where status=? order by id", new Object[]{EntityStatus.ACTIVE});
        } catch(Exception ex){
            log.print(StringConfig.getValue("err.db.list") + "\n" + ex);
        }
        closeCurrentSession();
        return entities;
    }

    public UserProperty userProperty(Integer id){
        return null;
    }
}
