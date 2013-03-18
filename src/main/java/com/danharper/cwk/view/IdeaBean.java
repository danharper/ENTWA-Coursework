package com.danharper.cwk.view;

import java.io.Serializable;
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

import com.danharper.cwk.domain.Idea;
import com.danharper.cwk.service.IdeaService;
import javax.ejb.EJB;

@Named
@Stateful
@ConversationScoped
public class IdeaBean implements Serializable
{

    public IdeaBean()
    {
    }

    private static final long serialVersionUID = 1L;
    @Inject
    private Conversation conversation;
    @EJB
    private IdeaService ideaService;
    @Resource
    private SessionContext sessionContext;
    private Long id;
    private Idea idea;
    private Idea example = new Idea();

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Idea getIdea()
    {
        return this.idea;
    }
    
    
    public Idea getExample()
    {
        return this.example;
    }

    public void setExample(Idea example)
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
            this.idea = this.example;
        }
        else
        {
            this.idea = findById(getId());
        }
    }

    public Idea findById(Long id)
    {
        return this.ideaService.find(id);
    }

    public String update()
    {
        this.conversation.end();

        try
        {
            if (this.id == null)
            {
                this.ideaService.create(this.idea);
                return "search?faces-redirect=true";
            }
            else
            {
                this.ideaService.update(this.idea);
                return "view?faces-redirect=true&id=" + this.idea.getId();
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
            this.ideaService.remove(findById(getId()));
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
    private int page;
    private long count;
    private List<Idea> pageItems;

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
        return 2;
    }

    public void search()
    {
        this.page = 0;
    }

    public void paginate()
    {
        this.count = ideaService.count();
        this.pageItems = ideaService.findRange(this.page, getPageSize());
    }

    public List<Idea> getPageItems()
    {
        return this.pageItems;
    }

    public long getCount()
    {
        return this.count;
    }

    /*
     * For correctly listing idea entities (e.g. HtmlSelectOneMenu)
     */
    public List<Idea> getAll()
    {
        return ideaService.findAll();
    }

    public Converter getConverter()
    {

        final IdeaBean ejbProxy = this.sessionContext.getBusinessObject(IdeaBean.class);

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

                return String.valueOf(((Idea) value).getId());
            }

        };
    }

    /*
     * Bi-directional, one-to-many tables
     */
    private Idea add = new Idea();

    public Idea getAdd()
    {
        return this.add;
    }

    public Idea getAdded()
    {
        Idea added = this.add;
        this.add = new Idea();
        return added;
    }

}