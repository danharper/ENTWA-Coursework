package com.danharper.cwk.controller;

import com.danharper.cwk.domain.Area;
import java.io.Serializable;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import com.danharper.cwk.service.AbstractService;
import com.danharper.cwk.service.AreaService;
import javax.ejb.EJB;

@Named
@Stateful
@ConversationScoped
public class AreaBean extends AbstractBean<Area> implements Serializable
{

    public AreaBean()
    {
        super(Area.class);
    }

    @EJB
    private AreaService areaService;
    
    public Area getArea()
    {
        return this.entity;
    }
    
    @Override
    protected AbstractService<Area> getService()
    {
        return this.areaService;
    }

    @Override
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

}