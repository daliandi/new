package com.md.basePlatform.repository.impl;

/**
 * BaseRepositoryImpl - 通用Repository基类
 * 
 * 【功能】：提供通用CRUD操作（findAll/findById/save/update/delete/search）
 * 【技术】：泛型<T>+反射机制+模板方法模式
 * 【用法】：子类继承，重写getMapperClass()返回具体Mapper接口
 * 【设计】：开闭原则，新增实体只需继承此类即可
 */

import com.md.basePlatform.repository.BaseRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

    @Autowired
    protected SqlSession sqlSession;

    protected abstract Class<?> getMapperClass();

    @SuppressWarnings("unchecked")
    protected Object getMapper() {
        return sqlSession.getMapper(getMapperClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return (List<T>) executeMethod("findAll");
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findById(Integer id) {
        return (T) executeMethod("findById", id);
    }

    @Override
    public void save(T entity) {
        executeMethod("save", entity);
    }

    @Override
    public void update(T entity) {
        executeMethod("update", entity);
    }

    @Override
    public void delete(Integer id) {
        executeMethod("delete", id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> search(String keyword) {
        return (List<T>) executeMethod("search", keyword);
    }

    private Object executeMethod(String methodName, Object... args) {
        try {
            Object mapper = getMapper();
            java.lang.reflect.Method method = getMapperClass().getMethod(methodName, getParameterTypes(args));
            return method.invoke(mapper, args);
        } catch (Exception e) {
            throw new RuntimeException("Error executing method: " + methodName, e);
        }
    }

    private Class<?>[] getParameterTypes(Object... args) {
        if (args == null || args.length == 0) {
            return new Class<?>[0];
        }
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }
}