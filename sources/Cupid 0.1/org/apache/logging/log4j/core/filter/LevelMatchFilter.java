package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "LevelMatchFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({"allocation"})
public final class LevelMatchFilter extends AbstractFilter {
  public static final String ATTR_MATCH = "match";
  
  private final Level level;
  
  private LevelMatchFilter(Level level, Filter.Result onMatch, Filter.Result onMismatch) {
    super(onMatch, onMismatch);
    this.level = level;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    return filter(level);
  }
  
  public Filter.Result filter(LogEvent event) {
    return filter(event.getLevel());
  }
  
  private Filter.Result filter(Level level) {
    return (level == this.level) ? this.onMatch : this.onMismatch;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return filter(level);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return filter(level);
  }
  
  public String toString() {
    return this.level.toString();
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public static class Builder extends AbstractFilter.AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<LevelMatchFilter> {
    @PluginBuilderAttribute
    private Level level = Level.ERROR;
    
    public Builder setLevel(Level level) {
      this.level = level;
      return this;
    }
    
    public LevelMatchFilter build() {
      return new LevelMatchFilter(this.level, getOnMatch(), getOnMismatch());
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\LevelMatchFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */