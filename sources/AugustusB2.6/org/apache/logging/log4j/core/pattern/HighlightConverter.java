// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.Iterator;
import org.apache.logging.log4j.Level;
import java.util.Arrays;
import java.util.Locale;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "highlight", category = "Converter")
@ConverterKeys({ "highlight" })
@PerformanceSensitive({ "allocation" })
public final class HighlightConverter extends LogEventPatternConverter implements AnsiConverter
{
    private static final Map<String, String> DEFAULT_STYLES;
    private static final Map<String, String> LOGBACK_STYLES;
    private static final String STYLE_KEY = "STYLE";
    private static final String STYLE_KEY_DEFAULT = "DEFAULT";
    private static final String STYLE_KEY_LOGBACK = "LOGBACK";
    private static final Map<String, Map<String, String>> STYLES;
    private final Map<String, String> levelStyles;
    private final List<PatternFormatter> patternFormatters;
    private final boolean noAnsi;
    private final String defaultStyle;
    
    private static Map<String, String> createLevelStyleMap(final String[] options) {
        if (options.length < 2) {
            return HighlightConverter.DEFAULT_STYLES;
        }
        final String string = options[1].replaceAll("disableAnsi=(true|false)", "").replaceAll("noConsoleNoAnsi=(true|false)", "");
        final Map<String, String> styles = AnsiEscape.createMap(string, new String[] { "STYLE" });
        final Map<String, String> levelStyles = new HashMap<String, String>(HighlightConverter.DEFAULT_STYLES);
        for (final Map.Entry<String, String> entry : styles.entrySet()) {
            final String key = entry.getKey().toUpperCase(Locale.ENGLISH);
            final String value = entry.getValue();
            if ("STYLE".equalsIgnoreCase(key)) {
                final Map<String, String> enumMap = HighlightConverter.STYLES.get(value.toUpperCase(Locale.ENGLISH));
                if (enumMap == null) {
                    HighlightConverter.LOGGER.error("Unknown level style: " + value + ". Use one of " + Arrays.toString(HighlightConverter.STYLES.keySet().toArray()));
                }
                else {
                    levelStyles.putAll(enumMap);
                }
            }
            else {
                final Level level = Level.toLevel(key, null);
                if (level == null) {
                    HighlightConverter.LOGGER.warn("Setting style for yet unknown level name {}", key);
                    levelStyles.put(key, value);
                }
                else {
                    levelStyles.put(level.name(), value);
                }
            }
        }
        return levelStyles;
    }
    
    public static HighlightConverter newInstance(final Configuration config, final String[] options) {
        if (options.length < 1) {
            HighlightConverter.LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
            return null;
        }
        if (options[0] == null) {
            HighlightConverter.LOGGER.error("No pattern supplied on style");
            return null;
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        final boolean disableAnsi = Arrays.toString(options).contains("disableAnsi=true");
        final boolean noConsoleNoAnsi = Arrays.toString(options).contains("noConsoleNoAnsi=true");
        final boolean hideAnsi = disableAnsi || (noConsoleNoAnsi && System.console() == null);
        return new HighlightConverter(formatters, createLevelStyleMap(options), hideAnsi);
    }
    
    private HighlightConverter(final List<PatternFormatter> patternFormatters, final Map<String, String> levelStyles, final boolean noAnsi) {
        super("style", "style");
        this.patternFormatters = patternFormatters;
        this.levelStyles = levelStyles;
        this.defaultStyle = AnsiEscape.getDefaultStyle();
        this.noAnsi = noAnsi;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        int start = 0;
        int end = 0;
        final String levelStyle = this.levelStyles.get(event.getLevel().name());
        if (!this.noAnsi) {
            start = toAppendTo.length();
            if (levelStyle != null) {
                toAppendTo.append(levelStyle);
            }
            end = toAppendTo.length();
        }
        for (int i = 0, size = this.patternFormatters.size(); i < size; ++i) {
            this.patternFormatters.get(i).format(event, toAppendTo);
        }
        final boolean empty = toAppendTo.length() == end;
        if (!this.noAnsi) {
            if (empty) {
                toAppendTo.setLength(start);
            }
            else if (levelStyle != null) {
                toAppendTo.append(this.defaultStyle);
            }
        }
    }
    
    String getLevelStyle(final Level level) {
        return this.levelStyles.get(level.name());
    }
    
    @Override
    public boolean handlesThrowable() {
        for (final PatternFormatter formatter : this.patternFormatters) {
            if (formatter.handlesThrowable()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        DEFAULT_STYLES = new HashMap<String, String>();
        LOGBACK_STYLES = new HashMap<String, String>();
        STYLES = new HashMap<String, Map<String, String>>();
        HighlightConverter.DEFAULT_STYLES.put(Level.FATAL.name(), AnsiEscape.createSequence("BRIGHT", "RED"));
        HighlightConverter.DEFAULT_STYLES.put(Level.ERROR.name(), AnsiEscape.createSequence("BRIGHT", "RED"));
        HighlightConverter.DEFAULT_STYLES.put(Level.WARN.name(), AnsiEscape.createSequence("YELLOW"));
        HighlightConverter.DEFAULT_STYLES.put(Level.INFO.name(), AnsiEscape.createSequence("GREEN"));
        HighlightConverter.DEFAULT_STYLES.put(Level.DEBUG.name(), AnsiEscape.createSequence("CYAN"));
        HighlightConverter.DEFAULT_STYLES.put(Level.TRACE.name(), AnsiEscape.createSequence("BLACK"));
        HighlightConverter.LOGBACK_STYLES.put(Level.FATAL.name(), AnsiEscape.createSequence("BLINK", "BRIGHT", "RED"));
        HighlightConverter.LOGBACK_STYLES.put(Level.ERROR.name(), AnsiEscape.createSequence("BRIGHT", "RED"));
        HighlightConverter.LOGBACK_STYLES.put(Level.WARN.name(), AnsiEscape.createSequence("RED"));
        HighlightConverter.LOGBACK_STYLES.put(Level.INFO.name(), AnsiEscape.createSequence("BLUE"));
        HighlightConverter.LOGBACK_STYLES.put(Level.DEBUG.name(), AnsiEscape.createSequence((String[])null));
        HighlightConverter.LOGBACK_STYLES.put(Level.TRACE.name(), AnsiEscape.createSequence((String[])null));
        HighlightConverter.STYLES.put("DEFAULT", HighlightConverter.DEFAULT_STYLES);
        HighlightConverter.STYLES.put("LOGBACK", HighlightConverter.LOGBACK_STYLES);
    }
}
