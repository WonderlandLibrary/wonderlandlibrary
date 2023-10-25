package org.apache.logging.log4j.core.filter;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginElement;

public abstract class AbstractFilterable extends AbstractLifeCycle implements Filterable {
  private volatile Filter filter;
  
  @PluginElement("Properties")
  private final Property[] propertyArray;
  
  public static abstract class Builder<B extends Builder<B>> {
    @PluginElement("Filter")
    private Filter filter;
    
    @PluginElement("Properties")
    private Property[] propertyArray;
    
    public B asBuilder() {
      return (B)this;
    }
    
    public Filter getFilter() {
      return this.filter;
    }
    
    public Property[] getPropertyArray() {
      return this.propertyArray;
    }
    
    public B setFilter(Filter filter) {
      this.filter = filter;
      return asBuilder();
    }
    
    public B setPropertyArray(Property[] properties) {
      this.propertyArray = properties;
      return asBuilder();
    }
    
    @Deprecated
    public B withFilter(Filter filter) {
      return setFilter(filter);
    }
  }
  
  protected AbstractFilterable() {
    this((Filter)null, Property.EMPTY_ARRAY);
  }
  
  protected AbstractFilterable(Filter filter) {
    this(filter, Property.EMPTY_ARRAY);
  }
  
  protected AbstractFilterable(Filter filter, Property[] propertyArray) {
    this.filter = filter;
    this.propertyArray = (propertyArray == null) ? Property.EMPTY_ARRAY : propertyArray;
  }
  
  public synchronized void addFilter(Filter filter) {
    if (filter == null)
      return; 
    if (this.filter == null) {
      this.filter = filter;
    } else if (this.filter instanceof CompositeFilter) {
      this.filter = ((CompositeFilter)this.filter).addFilter(filter);
    } else {
      Filter[] filters = { this.filter, filter };
      this.filter = CompositeFilter.createFilters(filters);
    } 
  }
  
  public Filter getFilter() {
    return this.filter;
  }
  
  public boolean hasFilter() {
    return (this.filter != null);
  }
  
  public boolean isFiltered(LogEvent event) {
    return (this.filter != null && this.filter.filter(event) == Filter.Result.DENY);
  }
  
  public synchronized void removeFilter(Filter filter) {
    if (this.filter == null || filter == null)
      return; 
    if (this.filter == filter || this.filter.equals(filter)) {
      this.filter = null;
    } else if (this.filter instanceof CompositeFilter) {
      CompositeFilter composite = (CompositeFilter)this.filter;
      composite = composite.removeFilter(filter);
      if (composite.size() > 1) {
        this.filter = composite;
      } else if (composite.size() == 1) {
        Iterator<Filter> iter = composite.iterator();
        this.filter = iter.next();
      } else {
        this.filter = null;
      } 
    } 
  }
  
  public void start() {
    setStarting();
    if (this.filter != null)
      this.filter.start(); 
    setStarted();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    return stop(timeout, timeUnit, true);
  }
  
  protected boolean stop(long timeout, TimeUnit timeUnit, boolean changeLifeCycleState) {
    if (changeLifeCycleState)
      setStopping(); 
    boolean stopped = true;
    if (this.filter != null)
      if (this.filter instanceof LifeCycle2) {
        stopped = ((LifeCycle2)this.filter).stop(timeout, timeUnit);
      } else {
        this.filter.stop();
        stopped = true;
      }  
    if (changeLifeCycleState)
      setStopped(); 
    return stopped;
  }
  
  public Property[] getPropertyArray() {
    return this.propertyArray;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\AbstractFilterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */