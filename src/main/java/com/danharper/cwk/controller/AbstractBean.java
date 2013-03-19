package com.danharper.cwk.controller;

import com.danharper.cwk.service.AbstractService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.enterprise.context.Conversation;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

/**
 * Provides basic core functionality for the majority of controller tasks
 * @author danharper
 * @param <T> The main entity which the bean represents
 */
public abstract class AbstractBean<T> implements Serializable
{

    protected Class<T> entityClass;
    protected T entity;
    protected T example;

    public AbstractBean(Class<T> entityClass)
    {
        this.entityClass = entityClass;

        try
        {
            this.example = entityClass.newInstance();
            this.add = entityClass.newInstance();
        }
        catch (InstantiationException ex)
        {
            Logger.getLogger(AbstractBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getLogger(AbstractBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static final long serialVersionUID = 1L;
    @Inject
    protected Conversation conversation;
    @Resource
    protected SessionContext sessionContext;
    private Long id;

    /**
     * Retrieve the service layer for the entity class
     * @return 
     */
    protected abstract AbstractService<T> getService();

    /**
     * Retrieve the ID of the entity being accessed
     * @return 
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * Set the ID of the entity being accessed
     * @param id 
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Retrieve an example entity to use for handling search parameters
     * @return The example entity for the current entity class
     */
    public T getExample()
    {
        return this.example;
    }

    /**
     * Set the example entity used for handling search parameters
     * @param example Example entity for the current entity class
     */
    public void setExample(T example)
    {
        this.example = example;
    }

    /**
     * Navigate to the view for creating an entity
     * @return 
     */
    public String create()
    {

        this.conversation.begin();
        return "create?faces-redirect=true";
    }

    /**
     * Set the current entity being accessed
     * 
     * If an entity is currently being accessed (ID is set), retrieve the
     * entity from the service layer.
     * 
     * Otherwise, retrieve an empty example entity for the entity class.
     */
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
            this.entity = this.example;
        }
        else
        {
            this.entity = findById(getId());
        }
    }

    /**
     * Helper method for retrieving the entity associated with the given ID
     * @param id The ID to retrieve the entity for
     * @return Retrieve entity
     */
    public T findById(Long id)
    {
        return getService().find(id);
    }

    /**
     * Either create or update the current entity depending on if the entity
     * has been persisted already or not.
     * Then navigate to either the list or view page
     * @return The view JSF should navigate to
     */
    public String update()
    {
        this.conversation.end();

        try
        {
            if (this.id == null)
            {
                getService().create(this.entity);
                return "search?faces-redirect=true";
            }
            else
            {
                getService().update(this.entity);
                return "view?faces-redirect=true&id=" + getId();
            }
        }
        catch (Exception e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    /**
     * Remove the current entity and navigate back to the search/list view
     * @return The view JSF should navigate to
     */
    public String delete()
    {
        this.conversation.end();

        try
        {
            getService().remove(findById(getId()));
            return "search?faces-redirect=true";
        }
        catch (Exception e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    /*
     * Support searching Idea entities with pagination
     */
    protected int page;
    protected long count;
    protected List<T> pageItems;

    /**
     * Retrieve the page number currently being accessed when paginating
     * @return 
     */
    public int getPage()
    {
        return this.page;
    }

    /**
     * Set the page number currently being accessed when paginating
     * @param page Page number
     */
    public void setPage(int page)
    {
        this.page = page;
    }

    /**
     * Retrieve the total number of entities to display per paginated page
     * @return Number of entities per page
     */
    public int getPageSize()
    {
        return 5;
    }

    /**
     * Reset the current paginated page number back to 0 when searching
     */
    public void search()
    {
        this.page = 0;
    }

    /**
     * Set the current page count when paginating views
     */
    public void paginate()
    {
        this.count = getService().count(this.example);
        this.pageItems = getService().findRange(this.page, getPageSize(), this.example);
    }

    /**
     * Retrieve entities to display for the current paginated page
     * @return List of entities to display
     */
    public List<T> getPageItems()
    {
        return this.pageItems;
    }

    /**
     * Retrieve the total number of entities available
     * @return Total number of entities
     */
    public long getCount()
    {
        return this.count;
    }


    /**
     * Retrieve all entities correctly when listing entities (eg. for select menus)
     * @return List of all entities
     */
    public List<T> getAll()
    {
        return getService().findAll();
    }

    /**
     * Retrieve the converter for providing sessions with the current bean
     * @return The converter for the Bean
     */
    public abstract Converter getConverter();

    /*
     * Bi-directional, one-to-many tables
     */
    protected T add;

    public T getAdd()
    {
        return this.add;
    }

    public T getAdded()
    {
        T added = this.add;
        
        try
        {
            this.add = entityClass.newInstance();
        }
        catch (InstantiationException ex)
        {
            Logger.getLogger(AbstractBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getLogger(AbstractBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return added;
    }

}