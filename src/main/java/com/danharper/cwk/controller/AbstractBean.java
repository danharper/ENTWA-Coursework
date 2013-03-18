package com.danharper.cwk.controller;

import com.danharper.cwk.service.BaseService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;


public abstract class AbstractBean<T> implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Class<T> entity;

	protected AbstractBean(Class<T> entityClass)
	{
		this.entity = entityClass;
	}

	@Inject
	private Conversation conversation;

	@ManagedProperty(value="#{param.id}")
	private Long id;

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public T getEntity()
	{
        try {
            return this.entity.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
        
        public abstract BaseService getService();

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
			//this.entity = this.example;
		}
		else
		{
			//this.entity = getService().findById(getId());
		}
	}
        
        public String update()
        {
            this.conversation.end();
            try
            {
                if (this.id == null)
                {
                    getService().persist(this.entity);
                    return "search?faces-redirect=true";
                }
                else
                {
                    getService().merge(this.entity);
                    //return "view?faces-redirect=true&id=" + this.entity.getId();
                    return "";
                }
            }
            catch (Exception e)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
                return null;
            }
        }

}