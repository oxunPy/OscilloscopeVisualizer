package com.example.program.common.service;

import com.example.program.app.entity.*;
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

    protected BaseDaoI<OsciSettingEntity> settingDao = new BaseSimpleDaoImpl<OsciSettingEntity>(){
        @Override
        public Session getCurrentSession() {
            return BaseService.this.getCurrentSession();
        }
    };

    protected BaseDaoI<OsciDeviceEntity> deviceDao = new BaseSimpleDaoImpl<OsciDeviceEntity>(){
        @Override
        public Session getCurrentSession(){
            return BaseService.this.getCurrentSession();
        }
    };

    protected BaseDaoI<OsciLanguageEntity> languageDao = new BaseSimpleDaoImpl<OsciLanguageEntity>(){
        @Override
        public Session getCurrentSession() {
            return BaseService.this.getCurrentSession();
        }
    };

    protected BaseDaoI<OsciToolEntity> toolDao = new BaseSimpleDaoImpl<OsciToolEntity>() {
        @Override
        public Session getCurrentSession() {
            return BaseService.this.getCurrentSession();
        }
    };

    protected BaseDaoI<OsciDataEntity> osciDataDao = new BaseSimpleDaoImpl<OsciDataEntity>() {
        @Override
        public Session getCurrentSession() {
            return BaseService.this.getCurrentSession();
        }
    };

    protected BaseDaoI<OsciFileEntity> osciFileDao = new BaseSimpleDaoImpl<OsciFileEntity>() {
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
