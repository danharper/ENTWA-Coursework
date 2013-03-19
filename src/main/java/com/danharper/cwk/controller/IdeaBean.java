package com.danharper.cwk.controller;

import com.danharper.cwk.domain.Idea;
import com.danharper.cwk.service.AbstractService;
import com.danharper.cwk.service.IdeaService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

@Named
@Stateful
@ConversationScoped
public class IdeaBean extends AbstractBean<Idea> implements Serializable
{

    public IdeaBean()
    {
        super(Idea.class);
    }

    @EJB
    private IdeaService ideaService;
    
    public Idea getIdea()
    {
        return this.entity;
    }
    
    @Override
    protected AbstractService<Idea> getService()
    {
        return this.ideaService;
    }

    @Override
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

}