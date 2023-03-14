package com.example.program.app.service;

import com.example.program.app.entity.OsciUserEntity;
import com.example.program.app.property.UserProperty;
import com.example.program.common.service.BaseService;
import com.example.program.util.Encryption;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;

import java.io.Serializable;

public class LoginService extends BaseService {

    private LogUtil log = LogUtil.getLog(this.getClass());

    public UserProperty getLoggedUser(String login) {
        openCurrentSession();
        try {
            String hql = "from OsciUserEntity where login=?";
            OsciUserEntity entity = userDao.get(hql, new Object[]{login});
            return UserProperty.newInstance(entity, true, true);
        } catch(Exception ex) {
            log.print(StringConfig.getValue("err.db.login.password") + "\n" + ex);
        }
        closeCurrentSession();
        return null;
    }

    public boolean authenticatePassword(String login, String password) {
        openCurrentSession();

        boolean result = false;
        String encryptedPassword = Encryption.convert(password);
        try {
            OsciUserEntity entity = userDao.get("from OsciUserEntity where login=? and pass=?", new Object[]{login, encryptedPassword});
            result = entity != null;
        } catch (Exception ex) {
            log.print(StringConfig.getValue("err.db.login.password") + "\n" + ex);
        }

        closeCurrentSession();
        return result;
    }

    public UserProperty userProperty(Serializable id) {
        openCurrentSession();

        UserProperty userProperty = UserProperty.newInstance(userDao.get(OsciUserEntity.class, id), true, true);

        closeCurrentSession();
        return userProperty;
    }

}
