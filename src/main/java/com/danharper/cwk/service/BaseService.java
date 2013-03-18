package com.danharper.cwk.service;

public abstract class BaseService<T>
{
    public abstract T findById(Long id);
    public abstract void persist(T entity);
    public abstract void merge(T entity);
}