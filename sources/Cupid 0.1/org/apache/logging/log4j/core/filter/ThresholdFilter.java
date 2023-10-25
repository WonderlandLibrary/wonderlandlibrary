package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "ThresholdFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({"allocation"})
public final class ThresholdFilter extends AbstractFilter {
  private final Level level;
  
  private ThresholdFilter(Level level, Filter.Result onMatch, Filter.Result onMismatch) {
    super(onMatch, onMismatch);
    this.level = level;
  }
  
  public Filter.Result filter(Logger logger, Level testLevel, Marker marker, String msg, Object... params) {
    return filter(testLevel);
  }
  
  public Filter.Result filter(Logger logger, Level testLevel, Marker marker, Object msg, Throwable t) {
    return filter(testLevel);
  }
  
  public Filter.Result filter(Logger logger, Level testLevel, Marker marker, Message msg, Throwable t) {
    return filter(testLevel);
  }
  
  public Filter.Result filter(LogEvent event) {
    return filter(event.getLevel());
  }
  
  private Filter.Result filter(Level testLevel) {
    return testLevel.isMoreSpecificThan(this.level) ? this.onMatch : this.onMismatch;
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
  
  public Level getLevel() {
    return this.level;
  }
  
  public String toString() {
    return this.level.toString();
  }
  
  @PluginFactory
  public static ThresholdFilter createFilter(@PluginAttribute("level") Level level, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
    Level actualLevel = (level == null) ? Level.ERROR : level;
    Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
    Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
    return new ThresholdFilter(actualLevel, onMatch, onMismatch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\ThresholdFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */