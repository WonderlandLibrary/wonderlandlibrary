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

@Plugin(name = "StringMatchFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({"allocation"})
public final class StringMatchFilter extends AbstractFilter {
  public static final String ATTR_MATCH = "match";
  
  private final String text;
  
  private StringMatchFilter(String text, Filter.Result onMatch, Filter.Result onMismatch) {
    super(onMatch, onMismatch);
    this.text = text;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return filter(logger.getMessageFactory().newMessage(msg, params).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    return filter(logger.getMessageFactory().newMessage(msg).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    return filter(msg.getFormattedMessage());
  }
  
  public Filter.Result filter(LogEvent event) {
    return filter(event.getMessage().getFormattedMessage());
  }
  
  private Filter.Result filter(String msg) {
    return msg.contains(this.text) ? this.onMatch : this.onMismatch;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 }).getFormattedMessage());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 }).getFormattedMessage());
  }
  
  public String toString() {
    return this.text;
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public static class Builder extends AbstractFilter.AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<StringMatchFilter> {
    @PluginBuilderAttribute
    private String text = "";
    
    public Builder setMatchString(String text) {
      this.text = text;
      return this;
    }
    
    public StringMatchFilter build() {
      return new StringMatchFilter(this.text, getOnMatch(), getOnMismatch());
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\StringMatchFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */