package org.apache.logging.log4j.core.pattern;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "highlight", category = "Converter")
@ConverterKeys({"highlight"})
@PerformanceSensitive({"allocation"})
public final class HighlightConverter extends LogEventPatternConverter implements AnsiConverter {
  private static final Map<String, String> DEFAULT_STYLES = new HashMap<>();
  
  private static final Map<String, String> LOGBACK_STYLES = new HashMap<>();
  
  private static final String STYLE_KEY = "STYLE";
  
  private static final String STYLE_KEY_DEFAULT = "DEFAULT";
  
  private static final String STYLE_KEY_LOGBACK = "LOGBACK";
  
  private static final Map<String, Map<String, String>> STYLES = new HashMap<>();
  
  private final Map<String, String> levelStyles;
  
  private final List<PatternFormatter> patternFormatters;
  
  private final boolean noAnsi;
  
  private final String defaultStyle;
  
  static {
    DEFAULT_STYLES.put(Level.FATAL.name(), AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
    DEFAULT_STYLES.put(Level.ERROR.name(), AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
    DEFAULT_STYLES.put(Level.WARN.name(), AnsiEscape.createSequence(new String[] { "YELLOW" }));
    DEFAULT_STYLES.put(Level.INFO.name(), AnsiEscape.createSequence(new String[] { "GREEN" }));
    DEFAULT_STYLES.put(Level.DEBUG.name(), AnsiEscape.createSequence(new String[] { "CYAN" }));
    DEFAULT_STYLES.put(Level.TRACE.name(), AnsiEscape.createSequence(new String[] { "BLACK" }));
    LOGBACK_STYLES.put(Level.FATAL.name(), AnsiEscape.createSequence(new String[] { "BLINK", "BRIGHT", "RED" }));
    LOGBACK_STYLES.put(Level.ERROR.name(), AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
    LOGBACK_STYLES.put(Level.WARN.name(), AnsiEscape.createSequence(new String[] { "RED" }));
    LOGBACK_STYLES.put(Level.INFO.name(), AnsiEscape.createSequence(new String[] { "BLUE" }));
    LOGBACK_STYLES.put(Level.DEBUG.name(), AnsiEscape.createSequence((String[])null));
    LOGBACK_STYLES.put(Level.TRACE.name(), AnsiEscape.createSequence((String[])null));
    STYLES.put("DEFAULT", DEFAULT_STYLES);
    STYLES.put("LOGBACK", LOGBACK_STYLES);
  }
  
  private static Map<String, String> createLevelStyleMap(String[] options) {
    if (options.length < 2)
      return DEFAULT_STYLES; 
    String string = options[1].replaceAll("disableAnsi=(true|false)", "").replaceAll("noConsoleNoAnsi=(true|false)", "");
    Map<String, String> styles = AnsiEscape.createMap(string, new String[] { "STYLE" });
    Map<String, String> levelStyles = new HashMap<>(DEFAULT_STYLES);
    for (Map.Entry<String, String> entry : styles.entrySet()) {
      String key = ((String)entry.getKey()).toUpperCase(Locale.ENGLISH);
      String value = entry.getValue();
      if ("STYLE".equalsIgnoreCase(key)) {
        Map<String, String> enumMap = STYLES.get(value.toUpperCase(Locale.ENGLISH));
        if (enumMap == null) {
          LOGGER.error("Unknown level style: " + value + ". Use one of " + 
              Arrays.toString(STYLES.keySet().toArray()));
          continue;
        } 
        levelStyles.putAll(enumMap);
        continue;
      } 
      Level level = Level.toLevel(key, null);
      if (level == null) {
        LOGGER.warn("Setting style for yet unknown level name {}", key);
        levelStyles.put(key, value);
        continue;
      } 
      levelStyles.put(level.name(), value);
    } 
    return levelStyles;
  }
  
  public static HighlightConverter newInstance(Configuration config, String[] options) {
    if (options.length < 1) {
      LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
      return null;
    } 
    if (options[0] == null) {
      LOGGER.error("No pattern supplied on style");
      return null;
    } 
    PatternParser parser = PatternLayout.createPatternParser(config);
    List<PatternFormatter> formatters = parser.parse(options[0]);
    boolean disableAnsi = Arrays.toString((Object[])options).contains("disableAnsi=true");
    boolean noConsoleNoAnsi = Arrays.toString((Object[])options).contains("noConsoleNoAnsi=true");
    boolean hideAnsi = (disableAnsi || (noConsoleNoAnsi && System.console() == null));
    return new HighlightConverter(formatters, createLevelStyleMap(options), hideAnsi);
  }
  
  private HighlightConverter(List<PatternFormatter> patternFormatters, Map<String, String> levelStyles, boolean noAnsi) {
    super("style", "style");
    this.patternFormatters = patternFormatters;
    this.levelStyles = levelStyles;
    this.defaultStyle = AnsiEscape.getDefaultStyle();
    this.noAnsi = noAnsi;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    int start = 0;
    int end = 0;
    String levelStyle = this.levelStyles.get(event.getLevel().name());
    if (!this.noAnsi) {
      start = toAppendTo.length();
      if (levelStyle != null)
        toAppendTo.append(levelStyle); 
      end = toAppendTo.length();
    } 
    for (int i = 0, size = this.patternFormatters.size(); i < size; i++)
      ((PatternFormatter)this.patternFormatters.get(i)).format(event, toAppendTo); 
    boolean empty = (toAppendTo.length() == end);
    if (!this.noAnsi)
      if (empty) {
        toAppendTo.setLength(start);
      } else if (levelStyle != null) {
        toAppendTo.append(this.defaultStyle);
      }  
  }
  
  String getLevelStyle(Level level) {
    return this.levelStyles.get(level.name());
  }
  
  public boolean handlesThrowable() {
    for (PatternFormatter formatter : this.patternFormatters) {
      if (formatter.handlesThrowable())
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\HighlightConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */