package com.danharper.cwk.service;

import com.danharper.cwk.domain.Area;
import com.danharper.cwk.facade.AbstractFacade;
import com.danharper.cwk.facade.AreaFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Simplified access to Area entities and business logic
 * @author danharper
 */
@Stateless
public class AreaService extends AbstractService<Area> {
    
    public AreaService()
    {
        super(Area.class);
    }
    
    @EJB
    private AreaFacade areas;
    
    protected AbstractFacade<Area> getFacade()
    {
        return this.areas;
    }
    
}
