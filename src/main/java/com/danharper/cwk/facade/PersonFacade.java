package com.danharper.cwk.facade;

import com.danharper.cwk.entity.Person;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Database access for Person/Companies
 * @author danharper
 */
@Stateless
public class PersonFacade extends AbstractFacade<Person>
{

    public PersonFacade()
    {
        super(Person.class);
    }

    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

    @Override
    protected Predicate[] getSearchPredicates(Root<Person> root, Person example)
    {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String email = example.getEmail();
        if (email != null && !"".equals(email))
        {
            predicatesList.add(builder.like(root.<String>get("email"), '%' + email + '%'));
        }
        
        String password = example.getPassword();
        if (password != null && !"".equals(password))
        {
            predicatesList.add(builder.like(root.<String>get("password"), '%' + password + '%'));
        }
        
        String contactName = example.getContactName();
        if (contactName != null && !"".equals(contactName))
        {
            predicatesList.add(builder.like(root.<String>get("contactName"), '%' + contactName + '%'));
        }
        
        String companyName = example.getCompanyName();
        if (companyName != null && !"".equals(companyName))
        {
            predicatesList.add(builder.like(root.<String>get("companyName"), '%' + companyName + '%'));
        }
        
        String profile = example.getProfile();
        if (profile != null && !"".equals(profile))
        {
            predicatesList.add(builder.like(root.<String>get("profile"), '%' + profile + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

}
