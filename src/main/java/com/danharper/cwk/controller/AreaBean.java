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

import com.danharper.cwk.domain.Area;
import com.danharper.cwk.service.AreaService;
import javax.ejb.EJB;

@Named
@Stateful
@ConversationScoped
public class AreaBean implements Serializable
{

    public AreaBean()
    {
    }

    private static final long serialVersionUID = 1L;
    @Inject
    private Conversation conversation;
    @EJB
    private AreaService areaService;
    @Resource
    private SessionContext sessionContext;
    private Long id;
    private Area area;
    private Area example = new Area();

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Area getArea()
    {
        return this.area;
    }

    public Area getExample()
    {
        return this.example;
    }

    public void setExample(Area example)
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
            this.area = this.example;
        }
        else
        {
            this.area = findById(getId());
        }
    }

    public Area findById(Long id)
    {
        return this.areaService.find(id);
    }

    public String update()
    {
        this.conversation.end();

        try
        {
            if (this.id == null)
            {
                this.areaService.create(this.area);
                return "search?faces-redirect=true";
            }
            else
            {
                this.areaService.update(this.area);
                return "view?faces-redirect=true&id=" + this.area.getId();
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
            this.areaService.remove(findById(getId()));
            return "search?faces-redirect=true";
        }
        catch (Exception e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    /*
     * Support searching Area entities with pagination
     */
    private int page;
    private long count;
    private List<Area> pageItems;

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
        this.count = areaService.count();
        this.pageItems = areaService.findRange(this.page, getPageSize());
    }

    public List<Area> getPageItems()
    {
        return this.pageItems;
    }

    public long getCount()
    {
        return this.count;
    }

    /*
     * For correctly listing area entities (e.g. HtmlSelectOneMenu)
     */
    public List<Area> getAll()
    {
        return areaService.findAll();
    }

    public Converter getConverter()
    {

        final AreaBean ejbProxy = this.sessionContext.getBusinessObject(AreaBean.class);

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

                return String.valueOf(((Area) value).getId());
            }

        };
    }

    /*
     * Support adding children to bidirectional, one-to-many tables
     */
    private Area add = new Area();

    public Area getAdd()
    {
        return this.add;
    }

    public Area getAdded()
    {
        Area added = this.add;
        this.add = new Area();
        return added;
    }

}