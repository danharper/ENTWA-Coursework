package com.danharper.cwk.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for working with Java Server Faces views.
 */
public final class ViewUtils
{

    /**
     * View helper method for accessing a collection of objects as a list
     * which can be iterated over in JSF template tags
     * @param <T> Entity being accessed
     * @param collection The collected to convert to an array list
     * @return 
     */
    public static <T> List<T> asList(Collection<T> collection)
    {

        if (collection == null)
        {
            return null;
        }

        return new ArrayList<T>(collection);
    }

    private ViewUtils()
    {
    }

}
