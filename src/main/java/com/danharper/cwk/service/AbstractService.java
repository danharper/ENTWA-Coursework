package com.danharper.cwk.service;

import com.danharper.cwk.facade.AbstractFacade;
import java.util.List;

/**
 * Provides core functionality for communicating with data access
 * facades for persistence tasks
 * @author danharper
 * @param <T> Entity class being represented
 */
public abstract class AbstractService<T>
{
    
    private Class<T> entityClass;

    public AbstractService(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }
    
    /**
     * Retrieve the facade layer for the DAO of the current entity class
     * @return 
     */
    protected abstract AbstractFacade<T> getFacade();
    
    /**
     * Retrieve the entity matching the given ID
     * @param id The ID to find in the database
     * @return The found entity
     */
    public T find(Long id)
    {
        return getFacade().find(id);
    }
    
    /**
     * Retrieve all entities
     * @return List of all entities
     */
    public List<T> findAll()
    {
        return getFacade().findAll();
    }
    
    /**
     * Save a new instance of T (the given entity class)
     * @param entity The entity to persist
     * @return The entity provided
     */
    public T create(T entity)
    {
        return getFacade().create(entity);
    }
    
    /**
     * Update the given entity
     * @param entity The entity to update
     * @return The entity provided
     */
    public T update(T entity)
    {
        return getFacade().update(entity);
    }
    
    /**
     * Remove the given entity
     * @param entity The entity to remove
     */
    public void remove(T entity)
    {
        getFacade().remove(entity);
    }
    
    /**
     * Retrieve the total number of entities
     * @param entity for the search parameters
     * @return Number of entities
     */
    public long count(T entity)
    {
        return getFacade().count(entity);
    }
    
    /**
     * Retrieve all entities, paginated
     * @param currentPage The current page being accessed
     * @param pageSize The number of entities per page
     * @param entity for the search parameters
     * @return List of entities
     */
    public List<T> findRange(int currentPage, int pageSize, T entity)
    {
        return getFacade().findRange(currentPage, pageSize, entity);
    }
    
}