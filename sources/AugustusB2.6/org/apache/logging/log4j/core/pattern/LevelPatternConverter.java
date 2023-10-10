// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import java.util.Map;
import java.util.Locale;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.Level;
import java.util.HashMap;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LevelPatternConverter", category = "Converter")
@ConverterKeys({ "p", "level" })
@PerformanceSensitive({ "allocation" })
public class LevelPatternConverter extends LogEventPatternConverter
{
    private static final String OPTION_LENGTH = "length";
    private static final String OPTION_LOWER = "lowerCase";
    private static final LevelPatternConverter INSTANCE;
    
    private LevelPatternConverter() {
        super("Level", "level");
    }
    
    public static LevelPatternConverter newInstance(final String[] options) {
        if (options == null || options.length == 0) {
            return LevelPatternConverter.INSTANCE;
        }
        final Map<Level, String> levelMap = new HashMap<Level, String>();
        int length = Integer.MAX_VALUE;
        boolean lowerCase = false;
        final String[] split;
        final String[] definitions = split = options[0].split(Patterns.COMMA_SEPARATOR);
        for (final String def : split) {
            final String[] pair = def.split("=");
            if (pair == null || pair.length != 2) {
                LevelPatternConverter.LOGGER.error("Invalid option {}", def);
            }
            else {
                final String key = pair[0].trim();
                final String value = pair[1].trim();
                if ("length".equalsIgnoreCase(key)) {
                    length = Integer.parseInt(value);
                }
                else if ("lowerCase".equalsIgnoreCase(key)) {
                    lowerCase = Boolean.parseBoolean(value);
                }
                else {
                    final Level level = Level.toLevel(key, null);
                    if (level == null) {
                        LevelPatternConverter.LOGGER.error("Invalid Level {}", key);
                    }
                    else {
                        levelMap.put(level, value);
                    }
                }
            }
        }
        if (levelMap.isEmpty() && length == Integer.MAX_VALUE && !lowerCase) {
            return LevelPatternConverter.INSTANCE;
        }
        for (final Level level2 : Level.values()) {
            if (!levelMap.containsKey(level2)) {
                final String left = left(level2, length);
                levelMap.put(level2, lowerCase ? left.toLowerCase(Locale.US) : left);
            }
        }
        return new LevelMapLevelPatternConverter((Map)levelMap);
    }
    
    private static String left(final Level level, final int length) {
        final String string = level.toString();
        if (length >= string.length()) {
            return string;
        }
        return string.substring(0, length);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
        throw new UnsupportedOperationException("Overridden by subclasses");
    }
    
    @Override
    public String getStyleClass(final Object e) {
        if (e instanceof LogEvent) {
            return "level " + ((LogEvent)e).getLevel().name().toLowerCase(Locale.ENGLISH);
        }
        return "level";
    }
    
    static {
        INSTANCE = new SimpleLevelPatternConverter();
    }
    
    private static final class SimpleLevelPatternConverter extends LevelPatternConverter
    {
        private SimpleLevelPatternConverter() {
            super(null);
        }
        
        @Override
        public void format(final LogEvent event, final StringBuilder output) {
            output.append(event.getLevel());
        }
    }
    
    private static final class LevelMapLevelPatternConverter extends LevelPatternConverter
    {
        private final Map<Level, String> levelMap;
        
        private LevelMapLevelPatternConverter(final Map<Level, String> levelMap) {
            super(null);
            this.levelMap = levelMap;
        }
        
        @Override
        public void format(final LogEvent event, final StringBuilder output) {
            output.append(this.levelMap.get(event.getLevel()));
        }
    }
}
