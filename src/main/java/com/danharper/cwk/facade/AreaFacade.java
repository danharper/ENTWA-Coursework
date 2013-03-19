package com.danharper.cwk.facade;

import com.danharper.cwk.entity.Area;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Database access for Idea Areas
 * @author danharper
 */
@Stateless
public class AreaFacade extends AbstractFacade<Area>
{

    public AreaFacade()
    {
        super(Area.class);
    }

    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

    @Override
    protected Predicate[] getSearchPredicates(Root<Area> root, Area example)
    {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String title = example.getTitle();
        if (title != null && !"".equals(title))
        {
            predicatesList.add(builder.like(root.<String>get("title"), '%' + title + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

}
