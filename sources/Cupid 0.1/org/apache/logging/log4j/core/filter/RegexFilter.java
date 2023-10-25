package org.apache.logging.log4j.core.filter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "RegexFilter", category = "Core", elementType = "filter", printObject = true)
public final class RegexFilter extends AbstractFilter {
  private static final int DEFAULT_PATTERN_FLAGS = 0;
  
  private final Pattern pattern;
  
  private final boolean useRawMessage;
  
  private RegexFilter(boolean raw, Pattern pattern, Filter.Result onMatch, Filter.Result onMismatch) {
    super(onMatch, onMismatch);
    this.pattern = pattern;
    this.useRawMessage = raw;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return filter(msg);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    if (msg == null)
      return this.onMismatch; 
    return filter(msg.toString());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    if (msg == null)
      return this.onMismatch; 
    String text = this.useRawMessage ? msg.getFormat() : msg.getFormattedMessage();
    return filter(text);
  }
  
  public Filter.Result filter(LogEvent event) {
    String text = this.useRawMessage ? event.getMessage().getFormat() : event.getMessage().getFormattedMessage();
    return filter(text);
  }
  
  private Filter.Result filter(String msg) {
    if (msg == null)
      return this.onMismatch; 
    Matcher m = this.pattern.matcher(msg);
    return m.matches() ? this.onMatch : this.onMismatch;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("useRaw=").append(this.useRawMessage);
    sb.append(", pattern=").append(this.pattern.toString());
    return sb.toString();
  }
  
  @PluginFactory
  public static RegexFilter createFilter(@PluginAttribute("regex") String regex, @PluginElement("PatternFlags") String[] patternFlags, @PluginAttribute("useRawMsg") Boolean useRawMsg, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) throws IllegalArgumentException, IllegalAccessException {
    if (regex == null) {
      LOGGER.error("A regular expression must be provided for RegexFilter");
      return null;
    } 
    return new RegexFilter(useRawMsg.booleanValue(), Pattern.compile(regex, toPatternFlags(patternFlags)), match, mismatch);
  }
  
  private static int toPatternFlags(String[] patternFlags) throws IllegalArgumentException, IllegalAccessException {
    if (patternFlags == null || patternFlags.length == 0)
      return 0; 
    Field[] fields = Pattern.class.getDeclaredFields();
    Comparator<Field> comparator = (f1, f2) -> f1.getName().compareTo(f2.getName());
    Arrays.sort(fields, comparator);
    String[] fieldNames = new String[fields.length];
    for (int i = 0; i < fields.length; i++)
      fieldNames[i] = fields[i].getName(); 
    int flags = 0;
    for (String test : patternFlags) {
      int index = Arrays.binarySearch((Object[])fieldNames, test);
      if (index >= 0) {
        Field field = fields[index];
        flags |= field.getInt(Pattern.class);
      } 
    } 
    return flags;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\RegexFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */