package com.danharper.cwk.service;

import com.danharper.cwk.domain.Person;
import com.danharper.cwk.facade.AbstractFacade;
import com.danharper.cwk.facade.PersonFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author danharper
 */
@Stateless
public class PersonService extends AbstractService<Person> {
    
    public PersonService()
    {
        super(Person.class);
    }
    
    @EJB
    private PersonFacade people;
    
    protected AbstractFacade<Person> getFacade()
    {
        return this.people;
    }
    
}
