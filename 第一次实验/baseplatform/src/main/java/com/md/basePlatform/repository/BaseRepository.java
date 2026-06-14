package com.md.basePlatform.repository;

/**
 * BaseRepository - 通用数据访问接口
 * 
 * 【功能】：提供通用CRUD操作规范
 * 【泛型】：<T> 表示实体类型
 * 【方法】：findAll/findById/save/update/delete/search
 * 【设计】：模板方法模式，子类继承实现具体Mapper
 */

import java.util.List;

public interface BaseRepository<T> {
    List<T> findAll();
    T findById(Integer id);
    void save(T entity);
    void update(T entity);
    void delete(Integer id);
    List<T> search(String keyword);
}