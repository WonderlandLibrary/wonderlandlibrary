package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.message.Message;

public abstract class AbstractFilter extends AbstractLifeCycle implements Filter {
  protected final Filter.Result onMatch;
  
  protected final Filter.Result onMismatch;
  
  public static abstract class AbstractFilterBuilder<B extends AbstractFilterBuilder<B>> {
    public static final String ATTR_ON_MISMATCH = "onMismatch";
    
    public static final String ATTR_ON_MATCH = "onMatch";
    
    @PluginBuilderAttribute("onMatch")
    private Filter.Result onMatch = Filter.Result.NEUTRAL;
    
    @PluginBuilderAttribute("onMismatch")
    private Filter.Result onMismatch = Filter.Result.DENY;
    
    public Filter.Result getOnMatch() {
      return this.onMatch;
    }
    
    public Filter.Result getOnMismatch() {
      return this.onMismatch;
    }
    
    public B setOnMatch(Filter.Result onMatch) {
      this.onMatch = onMatch;
      return asBuilder();
    }
    
    public B setOnMismatch(Filter.Result onMismatch) {
      this.onMismatch = onMismatch;
      return asBuilder();
    }
    
    public B asBuilder() {
      return (B)this;
    }
  }
  
  protected AbstractFilter() {
    this(null, null);
  }
  
  protected AbstractFilter(Filter.Result onMatch, Filter.Result onMismatch) {
    this.onMatch = (onMatch == null) ? Filter.Result.NEUTRAL : onMatch;
    this.onMismatch = (onMismatch == null) ? Filter.Result.DENY : onMismatch;
  }
  
  protected boolean equalsImpl(Object obj) {
    if (this == obj)
      return true; 
    if (!super.equalsImpl(obj))
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    AbstractFilter other = (AbstractFilter)obj;
    if (this.onMatch != other.onMatch)
      return false; 
    if (this.onMismatch != other.onMismatch)
      return false; 
    return true;
  }
  
  public Filter.Result filter(LogEvent event) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
    return filter(logger, level, marker, msg, new Object[] { p0 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
  }
  
  public final Filter.Result getOnMatch() {
    return this.onMatch;
  }
  
  public final Filter.Result getOnMismatch() {
    return this.onMismatch;
  }
  
  protected int hashCodeImpl() {
    int prime = 31;
    int result = super.hashCodeImpl();
    result = 31 * result + ((this.onMatch == null) ? 0 : this.onMatch.hashCode());
    result = 31 * result + ((this.onMismatch == null) ? 0 : this.onMismatch.hashCode());
    return result;
  }
  
  public String toString() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\AbstractFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */