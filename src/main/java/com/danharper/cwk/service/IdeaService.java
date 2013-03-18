package com.danharper.cwk.service;

import com.danharper.cwk.domain.Idea;
import com.danharper.cwk.facade.IdeaFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author danharper
 */
@Stateless
public class IdeaService {
    
    @EJB
    private IdeaFacade ideas;
    
    public Idea find(Long id)
    {
        return ideas.find(id);
    }
    
    public List<Idea> findAll()
    {
        return ideas.findAll();
    }
    
    public Idea create(Idea idea)
    {
        return ideas.create(idea);
    }
    
    public Idea update(Idea idea)
    {
        return ideas.update(idea);
    }
    
    public void remove(Idea idea)
    {
        ideas.remove(idea);
    }
    
    public long count()
    {
        return ideas.count();
    }
    
    public List<Idea> findRange(int currentPage, int pageSize)
    {
        return ideas.findRange(currentPage, pageSize);
    }
    
}
