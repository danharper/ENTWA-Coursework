package com.danharper.cwk.service;

import com.danharper.cwk.facade.AbstractFacade;
import java.util.List;

public abstract class AbstractService<T>
{
    
    private Class<T> entityClass;

    public AbstractService(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }
    
    protected abstract AbstractFacade<T> getFacade();
    
    public T find(Long id)
    {
        return getFacade().find(id);
    }
    
    public List<T> findAll()
    {
        return getFacade().findAll();
    }
    
    public T create(T entity)
    {
        return getFacade().create(entity);
    }
    
    public T update(T entity)
    {
        return getFacade().update(entity);
    }
    
    public void remove(T entity)
    {
        getFacade().remove(entity);
    }
    
    /**
     * 
     * @param entity for the search params
     * @return 
     */
    public long count(T entity)
    {
        return getFacade().count(entity);
    }
    
    /**
     * 
     * @param currentPage
     * @param pageSize
     * @param entity for the search params
     * @return 
     */
    public List<T> findRange(int currentPage, int pageSize, T entity)
    {
        return getFacade().findRange(currentPage, pageSize, entity);
    }
    
}