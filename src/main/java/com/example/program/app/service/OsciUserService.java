package com.example.program.app.service;

import com.example.program.app.entity.OsciUserEntity;
import com.example.program.app.entity.OsciUserEntity.UserType;
import com.example.program.app.property.UserProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;
import com.example.program.util.exception.UnsafeUpdateException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OsciUserService extends BaseService {
    private final LogUtil log = LogUtil.getLog(this.getClass());

    public Long insert(OsciUserEntity osciUserEntity) {
        openCurrentSessionWithTransaction();
        userDao.saveOrUpdate(osciUserEntity);
        closeCurrentSessionWithTransaction();
        return osciUserEntity.getId();
    }

    public OsciUserEntity getByLogin(String login) {
        openCurrentSession();

        OsciUserEntity entity = userDao.get("from OsciUserEntity where login=?", new Object[]{login});

        closeCurrentSession();
        return entity;
    }

    public OsciUserEntity getAdminUser() {
        List<OsciUserEntity> entities = list(UserType.ADMIN);
        if (entities.size() == 0) return null;
        if (entities.size() == 1) return entities.get(0);
        throw new UnsafeUpdateException("Only one adminUser must be active.", this.getClass());
    }

    public Boolean getAuthSet() {
        openCurrentSession();

        Boolean result = false;
        try {
            String sql = "SELECT authset FROM osci_user";
            result = MapUtils.getBoolean(userDao.getSQL(sql, new HashMap<>()), "authset", false);
        } catch (Exception ex) {
            log.print(StringConfig.getValue("err.db.get") + "\n" + ex);
        }
        closeCurrentSession();
        return result;
    }

    public List<OsciUserEntity> list(UserType type) {
        openCurrentSession();

        List<OsciUserEntity> entities = new ArrayList<>();
        try {
            entities = userDao.find("from OsciUserEntity where status=? and userType = ? ORDER BY id", new Object[]{EntityStatus.ACTIVE, type});
        } catch (Exception ex) {
            log.print(StringConfig.getValue("err.db.list") + "\n" + ex);
        }
        closeCurrentSession();
        return entities;
    }

    public UserProperty userProperty(Long id) {
        openCurrentSession();
        try{
            String hql = "from OsciUserEntity where 1 = 1 and id = ? and status <> ? ";
            OsciUserEntity osciUser = userDao.findFirst(hql, new Object[]{id, EntityStatus.DELETED});
            return UserProperty.newInstance(osciUser, true, false);
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.get") + " \n" + ex);
            closeCurrentSession();
        }
        closeCurrentSession();
        return null;
    }

    public Integer setAdminAuth(String login, String password) {
        openCurrentSessionWithTransaction();
        Integer result = 0;
        try {
            String sql = "UPDATE osci_user \n" +
                    "SET login = ?, pass = ?, authset = ? \n" +
                    "WHERE 1 = 1";
            result = userDao.executeSql(sql, new Object[]{login, password, Boolean.TRUE});
        } catch (Exception ex) {

        }
        closeCurrentSessionWithTransaction();
        return result;
    }

    public Boolean editAppUser(UserProperty userProperty) {
        openCurrentSessionWithTransaction();
        try {
            OsciUserEntity entity = userDao.findFirst("from OsciUserEntity where id = ?", new Object[]{userProperty.getId()});
            if (entity != null) {
                if (!StringUtils.isEmpty(userProperty.getLogin())) entity.setLogin(userProperty.getLogin());
                if (!StringUtils.isEmpty(userProperty.getPass())) entity.setPass(userProperty.getPass());
                if (!StringUtils.isEmpty(userProperty.getFirstName())) entity.setFirstName(userProperty.getFirstName());
                if (!StringUtils.isEmpty(userProperty.getLastName())) entity.setLastName(userProperty.getLastName());
                if (!StringUtils.isEmpty(userProperty.getMiddleName()))
                    entity.setMiddleName(userProperty.getMiddleName());
                if (!StringUtils.isEmpty(userProperty.getInfo())) entity.setInfo(userProperty.getInfo());
                userDao.save(entity);
                return true;
            }
        } catch (Exception ex) {
            log.print(StringConfig.getValue("err.db.get") + " \n" + ex.getMessage());
            closeCurrentSessionWithTransaction();
            throw new UnsafeUpdateException(StringConfig.getValue("err.db.edit"), getClass());
        }
        closeCurrentSessionWithTransaction();
        return false;
    }
}
