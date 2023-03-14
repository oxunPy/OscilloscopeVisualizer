package com.example.program.common.dao.impl;

import com.example.program.common.dao.BaseDaoI;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static com.example.program.util.Parser.setParams;

public abstract class BaseSimpleDaoImpl<T> implements BaseDaoI<T> {

    public abstract Session getCurrentSession();


    public Serializable save(T o) {
        //this.openCurrentSessionwithTransaction();

        Serializable result = this.getCurrentSession().save(o);

        //this.closeCurrentSessionwithTransaction();
        return result;
    }

    public void delete(T o) {
        //this.openCurrentSessionwithTransaction();

        this.getCurrentSession().delete(o);

        //this.closeCurrentSessionwithTransaction();
    }

    public void update(T o) {
        //this.openCurrentSessionwithTransaction();

        this.getCurrentSession().update(o);

        //this.closeCurrentSessionwithTransaction();
    }

    public void saveOrUpdate(T o) {
        //this.openCurrentSessionwithTransaction();

        this.getCurrentSession().saveOrUpdate(o);

        //this.closeCurrentSessionwithTransaction();
    }

    public List<T> find(String hql) {
        //this.openCurrentSession();

        List<T> result = this.getCurrentSession().createQuery(hql).list();

        //this.closeCurrentSession();
        return result;
    }

    public List<T> find(String hql, Object[] param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        List<T> result = q.list();

        //this.closeCurrentSession();
        return result;
    }

    public List<T> find(String hql, List<Object> param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        List<T> result = q.list();

        //this.closeCurrentSession();
        return result;
    }

    public List<T> find(String hql, Object[] param, Integer page, Integer rows) {
        //this.openCurrentSession();

        if (page == null || page < 1) {
            page = 1;
        }
        if (rows == null || rows < 1) {
            rows = 10;
        }
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        List<T> result = q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();

        //this.closeCurrentSession();
        return result;
    }

    public List<T> find(String hql, List<Object> param, Integer page, Integer rows) {
        //this.openCurrentSession();

        if (page == null || page < 1) {
            page = 1;
        }
        if (rows == null || rows < 1) {
            rows = 10;
        }
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        List<T> result = q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();

        //this.closeCurrentSession();
        return result;
    }

    public T findFirst(String hql, List<Object> param) {

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        List<T> result = q.setMaxResults(1).list();

        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public T findFirst(String hql, Object[] param) {

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        List<T> result = q.setMaxResults(1).list();

        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public List findSQL(String sql) {
        //this.openCurrentSession();

        List result = this.getCurrentSession().createSQLQuery(sql).list();

        //this.closeCurrentSession();
        return result;
    }

    public List findSQL(String sql, Class T) {
        //this.openCurrentSession();

        List result = this.getCurrentSession().createSQLQuery(sql).addEntity(T).list();

        //this.closeCurrentSession();
        return result;
    }

    public List findSQL(String sql, Object[] param) {
//        this.openCurrentSession();

        Query q = this.getCurrentSession().createSQLQuery(sql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        List result = q.list();

//        this.closeCurrentSession();
        return result;
    }

    public List<T> findSQL(String sql, Object[] param, Class T) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createSQLQuery(sql).addEntity(T);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        List<T> result = q.list();

        //this.closeCurrentSession();
        return result;
    }

    public List<T> findSQL(String sql, List<Object> param, Class T) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createSQLQuery(sql).addEntity(T);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        List result = q.list();

        //this.closeCurrentSession();
        return result;
    }

    public List findSQL(String sql, Object[] param, Integer page, Integer rows) {
        //this.openCurrentSession();

        if (page == null || page < 1) {
            page = 1;
        }
        if (rows == null || rows < 1) {
            rows = 10;
        }
        Query q = this.getCurrentSession().createSQLQuery(sql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        List result = q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();

        //this.closeCurrentSession();
        return result;
    }

    public List<T> findSQL(String sql, Object[] param, Integer page, Integer rows, Class T) {
        //this.openCurrentSession();

        if (page == null || page < 1) {
            page = 1;
        }
        if (rows == null || rows < 1) {
            rows = 10;
        }
        Query q = this.getCurrentSession().createSQLQuery(sql).addEntity(T);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        List<T> result = q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();

        //this.closeCurrentSession();
        return result;
    }

    @Override
    public List<T> findSQL(String sql, Map<String, Object> values) {
//        this.openCurrentSession();
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        setParams(values, query);

//        this.closeCurrentSession();
        return query.list();
    }

    public List<T> findSQL(String sql, List<Object> param, Integer page, Integer rows, Class T) {
        //this.openCurrentSession();

        if (page == null || page < 1) {
            page = 1;
        }
        if (rows == null || rows < 1) {
            rows = 10;
        }
        Query q = this.getCurrentSession().createSQLQuery(sql).addEntity(T);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        List<T> result = q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();

        //this.closeCurrentSession();
        return result;
    }

    public Long countSQL(String sql) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createSQLQuery(sql);
        Long result;
        if (q.uniqueResult() instanceof BigInteger)
            result = ((BigInteger) q.uniqueResult()).longValue();
        else
            result = (Long) q.uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Long countSQL(String sql, Object[] param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createSQLQuery(sql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        Long result;
        if (q.uniqueResult() instanceof BigInteger)
            result = ((BigInteger) q.uniqueResult()).longValue();
        else
            result = (Long) q.uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Long countSQL(String sql, List<Object> param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createSQLQuery(sql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        Long result;
        if (q.uniqueResult() instanceof BigInteger)
            result = ((BigInteger) q.uniqueResult()).longValue();
        else
            result = (Long) q.uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public T get(Class<T> c, Serializable id) {
        //this.openCurrentSession();

        T result = (T) this.getCurrentSession().get(c, id);

        //this.closeCurrentSession();
        return result;
    }

    public T get(String hql, Object[] param) {
        return this.findFirst(hql, param);
    }

    public T get(String hql, List<Object> param) {
        return this.findFirst(hql, param);
    }

    @Override
    public Map getSQL(String sql, Map<String, Object> values) {
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        setParams(values, query);

        List l = query.setMaxResults(1).list();
        if (l != null && l.size() > 0) {
            return (Map) l.get(0);
        } else {
            return null;
        }
    }

    public Long count(String hql) {
        //this.openCurrentSession();

        Long result = (Long) this.getCurrentSession().createQuery(hql).uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Long count(String hql, Object[] param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        Long result = (Long) q.uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Long count(String hql, List<Object> param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        Long result = (Long) q.uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Double amount(String hql) {
        //this.openCurrentSession();

        Double result = (Double) this.getCurrentSession().createQuery(hql).uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Double amount(String hql, Object[] param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        Double result = (Double) q.uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Double amount(String hql, List<Object> param) {
        //this.openCurrentSession();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        Double result = (Double) q.uniqueResult();

        //this.closeCurrentSession();
        return result;
    }

    public Integer executeHql(String hql) {
        //this.openCurrentSessionwithTransaction();

        Integer result = this.getCurrentSession().createQuery(hql).executeUpdate();

        //this.closeCurrentSessionwithTransaction();
        return result;
    }

    public Integer executeSql(String sql, Object[] param){
//        this.openCurrentSessionwithTransaction();

        Query q = this.getCurrentSession().createSQLQuery(sql);

        if(param != null && param.length > 0){
            for(int i = 0; i < param.length; i++){
                q.setParameter(i, param[i]);
            }
        }
        Integer result = q.executeUpdate();
        return result;
    }

    public Integer executeHql(String hql, Object[] param) {
        //this.openCurrentSessionwithTransaction();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        Integer result = q.executeUpdate();

        //this.closeCurrentSessionwithTransaction();
        return result;
    }

    @Override
    public Integer executeSql(String hql) {

        Integer result = this.getCurrentSession().createSQLQuery(hql).executeUpdate();

        return result;
    }

    public Integer executeHql(String hql, List<Object> param) {
        //this.openCurrentSessionwithTransaction();

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        Integer result = q.executeUpdate();

        //this.closeCurrentSessionwithTransaction();
        return result;
    }
}
