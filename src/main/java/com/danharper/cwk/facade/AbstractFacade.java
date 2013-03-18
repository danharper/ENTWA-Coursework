package com.danharper.cwk.facade;

import com.danharper.cwk.domain.Idea;
import com.danharper.cwk.domain.Person;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author danharper
 */
public abstract class AbstractFacade<T>
{

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public T create(T entity)
    {
        getEntityManager().persist(entity);
        return entity;
    }

    public T update(T entity)
    {
        return getEntityManager().merge(entity);
    }

    public void remove(T entity)
    {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    public T find(Object id)
    {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll()
    {
        CriteriaQuery criteria = getEntityManager().getCriteriaBuilder().createQuery();
        return getEntityManager().createQuery(
                criteria.select(criteria.from(entityClass))).getResultList();
    }

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

    protected abstract Predicate[] getSearchPredicates(Root<T> root, T entity);
}
