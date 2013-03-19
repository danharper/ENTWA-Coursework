package com.danharper.cwk.facade;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Core functionality for majority of basic DAO tasks
 * @author danharper
 * @param <T> Entity class being represented
 */
public abstract class AbstractFacade<T>
{

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    /**
     * Retrieve the Entity Manager used for accessing persistence of entities
     * @return 
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Persist a new instance of T (the given entity class)
     * @param entity The entity to persist
     * @return The entity provided
     */
    public T create(T entity)
    {
        getEntityManager().persist(entity);
        return entity;
    }

    /**
     * Update the given entity in the persistence layer
     * @param entity The entity to update
     * @return The entity provided
     */
    public T update(T entity)
    {
        return getEntityManager().merge(entity);
    }

    /**
     * Remove (un-persist) the given entity
     * @param entity The entity to remove
     */
    public void remove(T entity)
    {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    /**
     * Retrieve the entity matching the given ID from the persistence layer
     * @param id The ID to find in the database
     * @return The found entity
     */
    public T find(Object id)
    {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Retrieve all entities from the persistence layer
     * @return List of all entities
     */
    public List<T> findAll()
    {
        CriteriaQuery criteria = getEntityManager().getCriteriaBuilder().createQuery();
        return getEntityManager().createQuery(
                criteria.select(criteria.from(entityClass))).getResultList();
    }

    /**
     * Retrieve the total number of entities persisted
     * @param entity
     * @return Number of entities
     */
    public long count(T entity)
    {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<T> root = countCriteria.from(entityClass);
        countCriteria = countCriteria.select(builder.count(root)).where(
                getSearchPredicates(root, entity));
        return getEntityManager().createQuery(countCriteria)
                .getSingleResult();
    }

    /**
     * Retrieve all entities, paginated
     * @param currentPage The current page being accessed
     * @param pageSize The number of entities per page
     * @param entity An entity containing data to search on (empty for everything)
     * @return List of entities
     */
    public List<T> findRange(int currentPage, int pageSize, T entity)
    {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        
        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
      Root<T> root = criteria.from(entityClass);
      
      TypedQuery<T> query = getEntityManager().createQuery(criteria
            .select(root).where(getSearchPredicates(root, entity)));
      query.setFirstResult(currentPage * pageSize).setMaxResults(
            pageSize);
      return query.getResultList();
    }

    /**
     * Access search parameters on given entity
     * @param root The root search request
     * @param entity Entity to access search parameters on
     * @return An array of search predicates
     */
    protected abstract Predicate[] getSearchPredicates(Root<T> root, T entity);
}
