package com.danharper.cwk.service;

import com.danharper.cwk.domain.Person;
import com.danharper.cwk.facade.PersonFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author danharper
 */
@Stateless
public class PersonService {
    
    @EJB
    private PersonFacade people;
    
    public Person find(Long id)
    {
        return people.find(id);
    }
    
    public List<Person> findAll()
    {
        return people.findAll();
    }
    
    public Person create(Person person)
    {
        return people.create(person);
    }
    
    public Person update(Person person)
    {
        return people.update(person);
    }
    
    public void remove(Person person)
    {
        people.remove(person);
    }
    
    public long count()
    {
        return people.count();
    }
    
    public List<Person> findRange(int currentPage, int pageSize)
    {
        return people.findRange(currentPage, pageSize);
    }
    
}
