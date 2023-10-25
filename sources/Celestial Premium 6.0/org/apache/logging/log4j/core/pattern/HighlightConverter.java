/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.AnsiEscape;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

@Plugin(name="highlight", category="Converter")
@ConverterKeys(value={"highlight"})
public final class HighlightConverter
extends LogEventPatternConverter {
    private static final EnumMap<Level, String> DEFAULT_STYLES = new EnumMap(Level.class);
    private static final EnumMap<Level, String> LOGBACK_STYLES = new EnumMap(Level.class);
    private static final String STYLE_KEY = "STYLE";
    private static final String STYLE_KEY_DEFAULT = "DEFAULT";
    private static final String STYLE_KEY_LOGBACK = "LOGBACK";
    private static final Map<String, EnumMap<Level, String>> STYLES = new HashMap<String, EnumMap<Level, String>>();
    private final EnumMap<Level, String> levelStyles;
    private final List<PatternFormatter> patternFormatters;

    private static EnumMap<Level, String> createLevelStyleMap(String[] options) {
        if (options.length < 2) {
            return DEFAULT_STYLES;
        }
        Map<String, String> styles = AnsiEscape.createMap(options[1], new String[]{STYLE_KEY});
        EnumMap<Level, String> levelStyles = new EnumMap<Level, String>(DEFAULT_STYLES);
        for (Map.Entry<String, String> entry : styles.entrySet()) {
            String key = entry.getKey().toUpperCase(Locale.ENGLISH);
            String value = entry.getValue();
            if (STYLE_KEY.equalsIgnoreCase(key)) {
                EnumMap<Level, String> enumMap = STYLES.get(value.toUpperCase(Locale.ENGLISH));
                if (enumMap == null) {
                    LOGGER.error("Unknown level style: " + value + ". Use one of " + Arrays.toString(STYLES.keySet().toArray()));
                    continue;
                }
                levelStyles.putAll(enumMap);
                continue;
            }
            Level level = Level.valueOf(key);
            if (level == null) {
                LOGGER.error("Unknown level name: " + key + ". Use one of " + Arrays.toString(DEFAULT_STYLES.keySet().toArray()));
                continue;
            }
            levelStyles.put(level, value);
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
        return new HighlightConverter(formatters, HighlightConverter.createLevelStyleMap(options));
    }

    private HighlightConverter(List<PatternFormatter> patternFormatters, EnumMap<Level, String> levelStyles) {
        super("style", "style");
        this.patternFormatters = patternFormatters;
        this.levelStyles = levelStyles;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        StringBuilder buf = new StringBuilder();
        for (PatternFormatter formatter : this.patternFormatters) {
            formatter.format(event, buf);
        }
        if (buf.length() > 0) {
            toAppendTo.append(this.levelStyles.get((Object)event.getLevel())).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
        }
    }

    @Override
    public boolean handlesThrowable() {
        for (PatternFormatter formatter : this.patternFormatters) {
            if (!formatter.handlesThrowable()) continue;
            return true;
        }
        return false;
    }

    static {
        DEFAULT_STYLES.put(Level.FATAL, AnsiEscape.createSequence("BRIGHT", "RED"));
        DEFAULT_STYLES.put(Level.ERROR, AnsiEscape.createSequence("BRIGHT", "RED"));
        DEFAULT_STYLES.put(Level.WARN, AnsiEscape.createSequence("YELLOW"));
        DEFAULT_STYLES.put(Level.INFO, AnsiEscape.createSequence("GREEN"));
        DEFAULT_STYLES.put(Level.DEBUG, AnsiEscape.createSequence("CYAN"));
        DEFAULT_STYLES.put(Level.TRACE, AnsiEscape.createSequence("BLACK"));
        LOGBACK_STYLES.put(Level.FATAL, AnsiEscape.createSequence("BLINK", "BRIGHT", "RED"));
        LOGBACK_STYLES.put(Level.ERROR, AnsiEscape.createSequence("BRIGHT", "RED"));
        LOGBACK_STYLES.put(Level.WARN, AnsiEscape.createSequence("RED"));
        LOGBACK_STYLES.put(Level.INFO, AnsiEscape.createSequence("BLUE"));
        LOGBACK_STYLES.put(Level.DEBUG, AnsiEscape.createSequence(null));
        LOGBACK_STYLES.put(Level.TRACE, AnsiEscape.createSequence(null));
        STYLES.put(STYLE_KEY_DEFAULT, DEFAULT_STYLES);
        STYLES.put(STYLE_KEY_LOGBACK, LOGBACK_STYLES);
    }
}

