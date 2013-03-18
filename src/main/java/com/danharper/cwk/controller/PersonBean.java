package com.danharper.cwk.controller;

import com.danharper.cwk.domain.Person;
import java.io.Serializable;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import com.danharper.cwk.service.AbstractService;
import com.danharper.cwk.service.PersonService;
import com.danharper.cwk.view.AbstractBean;
import javax.ejb.EJB;

@Named
@Stateful
@ConversationScoped
public class PersonBean extends AbstractBean<Person> implements Serializable
{

    public PersonBean()
    {
        super(Person.class);
    }

    @EJB
    private PersonService personService;
    
    public Person getPerson()
    {
        return this.entity;
    }
    
    @Override
    protected AbstractService<Person> getService()
    {
        return this.personService;
    }

    @Override
    public Converter getConverter()
    {
        final PersonBean ejbProxy = this.sessionContext.getBusinessObject(PersonBean.class);

        return new Converter()
        {
            @Override
            public Object getAsObject(FacesContext context,
                    UIComponent component, String value)
            {

                return ejbProxy.findById(Long.valueOf(value));
            }

            @Override
            public String getAsString(FacesContext context,
                    UIComponent component, Object value)
            {

                if (value == null)
                {
                    return "";
                }

                return String.valueOf(((Person) value).getId());
            }

        };
    }

}