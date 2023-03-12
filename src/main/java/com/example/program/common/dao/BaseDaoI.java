package com.example.program.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDaoI<T> {

    public Serializable save(T o);
    public void delete(T o);
    public void update(T o);
    public void saveOrUpdate(T o);

    public List<T> find(String hql);
    public List<T> find(String hql, Object[] param);
    public List<T> find(String hql, List<Object> param);

    /**
     * @param page which pag to display
     * @param rows how many records in each page
     */
    public List<T> find(String hql, Object[] param, Integer page, Integer rows);
    public List<T> find(String hql, List<Object> param, Integer page, Integer rows);
    public T findFirst(String hql, List<Object> param);
    public T findFirst(String hql, Object[] param);

    public List findSQL(String sql);
    public List findSQL(String sql, Class T);
    public List findSQL(String sql, Object[] param);
    public List<T> findSQL(String sql, Object[] param, Class T);
    public List<T> findSQL(String sql, List<Object> param, Class T);
    public List findSQL(String sql, Map<String, Object> values);
    public List findSQL(String sql, Object[] param, Integer page, Integer rows);
    public List<T> findSQL(String sql, Object[] param, Integer page, Integer rows, Class T);
    public List<T> findSQL(String sql, List<Object> param, Integer page, Integer rows, Class T);

    public Long countSQL(String sql);
    public Long countSQL(String sql, Object[] param);
    public Long countSQL(String sql, List<Object> param);

    public T get(Class<T> c, Serializable id);
    public T get(String hql, Object[] param);
    public T get(String hql, List<Object> param);
    public Map getSQL(String sql, Map<String, Object> values);

    public Long count(String hql);
    public Long count(String hql, Object[] param);
    public Long count(String hql, List<Object> param);

    public Double amount(String hql);
    public Double amount(String hql, Object[] param);
    public Double amount(String hql, List<Object> param);

    /**
     * @return number of results
     */
    public Integer executeHql(String hql);
    public Integer executeSql(String hql);
    public Integer executeHql(String hql, Object[] param);
    public Integer executeHql(String hql, List<Object> param);
}
