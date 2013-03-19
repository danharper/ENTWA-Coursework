package com.danharper.cwk.service;

import com.danharper.cwk.domain.Idea;
import com.danharper.cwk.facade.AbstractFacade;
import com.danharper.cwk.facade.IdeaFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Simplified access to Idea entities and business logic
 * @author danharper
 */
@Stateless
public class IdeaService extends AbstractService<Idea> {
    
    public IdeaService()
    {
        super(Idea.class);
    }
    
    @EJB
    private IdeaFacade ideas;
    
    protected AbstractFacade<Idea> getFacade()
    {
        return this.ideas;
    }
    
}
