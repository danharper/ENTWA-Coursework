package com.danharper.cwk.service;

import com.danharper.cwk.domain.Area;
import com.danharper.cwk.facade.AreaFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author danharper
 */
@Stateless
public class AreaService {
    
    @EJB
    private AreaFacade areas;
    
    public Area find(Long id)
    {
        return areas.find(id);
    }
    
    public List<Area> findAll()
    {
        return areas.findAll();
    }
    
    public Area create(Area area)
    {
        return areas.create(area);
    }
    
    public Area update(Area area)
    {
        return areas.update(area);
    }
    
    public void remove(Area area)
    {
        areas.remove(area);
    }
    
    public long count()
    {
        return areas.count();
    }
    
    public List<Area> findRange(int currentPage, int pageSize)
    {
        return areas.findRange(currentPage, pageSize);
    }
    
}
