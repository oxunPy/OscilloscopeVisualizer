package com.example.program.common.service;

import com.example.program.app.entity.SettingEntity;
import com.example.program.app.entity.OsciUserEntity;
import com.example.program.common.dao.BaseDaoI;
import com.example.program.common.dao.impl.BaseSimpleDaoImpl;
import com.example.program.util.persistence.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class BaseService {

    protected BaseDaoI<OsciUserEntity> userDao = new BaseSimpleDaoImpl<OsciUserEntity>() {
        @Override
        public Session getCurrentSession() {
            return BaseService.this.getCurrentSession();
        }
    };

    protected BaseDaoI<SettingEntity> settingDao = new BaseSimpleDaoImpl<SettingEntity>(){
        @Override
        public Session getCurrentSession() {
            return BaseService.this.getCurrentSession();
        }
    };
    private Session currentSession;

    private Transaction currentTransaction;


    private SessionFactory getSessionFactory(){
        return HibernateUtil.getSessionFactory();
    }

    protected Session openCurrentSession(){
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    protected Session openCurrentSessionWithTransaction(){
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    protected void closeCurrentSession(){
        currentSession.close();
    }

    protected  void closeCurrentSessionWithTransaction(){
        currentTransaction.commit();
        currentSession.close();
    }

    private Session getCurrentSession(){
        return currentSession;
    }
}
