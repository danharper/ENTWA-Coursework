package com.danharper.cwk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.danharper.cwk.domain.Person;
import com.danharper.cwk.service.PersonService;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;

@Named
@Stateful
@ConversationScoped
public class PersonBean implements Serializable
{

    public PersonBean()
    {
    }

    private static final long serialVersionUID = 1L;
    @Inject
    private Conversation conversation;
    @EJB
    private PersonService personService;
    @Resource
    private SessionContext sessionContext;
    @ManagedProperty(value = "#{param.id}")
    private Long id;
    private Person person;
    private Person example = new Person();

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Person getPerson()
    {
        return this.person;
    }

    public Person getExample()
    {
        return this.example;
    }

    public void setExample(Person example)
    {
        this.example = example;
    }

    public String create()
    {

        this.conversation.begin();
        return "create?faces-redirect=true";
    }

    public void retrieve()
    {

        if (FacesContext.getCurrentInstance().isPostback())
        {
            return;
        }

        if (this.conversation.isTransient())
        {
            this.conversation.begin();
        }

        if (this.id == null)
        {
            this.person = this.example;
        }
        else
        {
            this.person = findById(getId());
        }
    }

    public Person findById(Long id)
    {

        return this.personService.find(id);
    }

    /*
     * Support updating and deleting Person entities
     */
    public String update()
    {
        this.conversation.end();

        try
        {
            if (this.id == null)
            {
                this.personService.create(this.person);
                return "search?faces-redirect=true";
            }
            else
            {
                this.personService.update(this.person);
                return "view?faces-redirect=true&id=" + this.person.getId();
            }
        }
        catch (Exception e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete()
    {
        this.conversation.end();

        try
        {
            this.personService.remove(findById(getId()));
            return "search?faces-redirect=true";
        }
        catch (Exception e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    /*
     * Support searching Person entities with pagination
     */
    private int page;
    private long count;
    private List<Person> pageItems;

    public int getPage()
    {
        return this.page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getPageSize()
    {
        return 10;
    }

    public void search()
    {
        this.page = 0;
    }

    public void paginate()
    {
        this.count = personService.count(this.example);
        this.pageItems = personService.findRange(this.page, getPageSize(), this.example);
    }

    public List<Person> getPageItems()
    {
        return this.pageItems;
    }

    public long getCount()
    {
        return this.count;
    }

    /*
     * Support listing and POSTing back Person entities (e.g. from inside an
     * HtmlSelectOneMenu)
     */
    public List<Person> getAll()
    {
        return personService.findAll();
    }

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

    /*
     * Bi-directional, one-to-many tables
     */
    private Person add = new Person();

    public Person getAdd()
    {
        return this.add;
    }

    public Person getAdded()
    {
        Person added = this.add;
        this.add = new Person();
        return added;
    }

}