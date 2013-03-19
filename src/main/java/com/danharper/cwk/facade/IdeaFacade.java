package com.danharper.cwk.facade;

import com.danharper.cwk.domain.Idea;
import com.danharper.cwk.domain.Person;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Database access for Ideas
 * @author danharper
 */
@Stateless
public class IdeaFacade extends AbstractFacade<Idea>
{

    public IdeaFacade()
    {
        super(Idea.class);
    }
    
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

    @Override
    protected Predicate[] getSearchPredicates(Root<Idea> root, Idea example)
    {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String title = example.getTitle();
        if (title != null && !"".equals(title)) {
            predicatesList.add(builder.like(root.<String>get("title"), '%' + title + '%'));
        }

        String details = example.getDetails();
        if (details != null && !"".equals(details)) {
            predicatesList.add(builder.like(root.<String>get("details"), '%' + details + '%'));
        }

        Person person = example.getPerson();
        if (person != null) {
            predicatesList.add(builder.equal(root.get("person"), person));
        }

        int stateType = example.getStateType();
        if (stateType != 0) {
            predicatesList.add(builder.equal(root.get("stateType"), stateType));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}
