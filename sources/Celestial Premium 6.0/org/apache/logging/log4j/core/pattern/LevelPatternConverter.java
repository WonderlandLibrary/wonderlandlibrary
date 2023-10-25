/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.util.EnumMap;
import java.util.Locale;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name="LevelPatternConverter", category="Converter")
@ConverterKeys(value={"p", "level"})
public final class LevelPatternConverter
extends LogEventPatternConverter {
    private static final String OPTION_LENGTH = "length";
    private static final String OPTION_LOWER = "lowerCase";
    private static final LevelPatternConverter INSTANCE = new LevelPatternConverter(null);
    private final EnumMap<Level, String> levelMap;

    private LevelPatternConverter(EnumMap<Level, String> map) {
        super("Level", "level");
        this.levelMap = map;
    }

    public static LevelPatternConverter newInstance(String[] options) {
        String[] definitions;
        if (options == null || options.length == 0) {
            return INSTANCE;
        }
        EnumMap<Level, String> levelMap = new EnumMap<Level, String>(Level.class);
        int length = Integer.MAX_VALUE;
        boolean lowerCase = false;
        for (String string : definitions = options[0].split(",")) {
            String[] pair = string.split("=");
            if (pair == null || pair.length != 2) {
                LOGGER.error("Invalid option {}", string);
                continue;
            }
            String key = pair[0].trim();
            String value = pair[1].trim();
            if (OPTION_LENGTH.equalsIgnoreCase(key)) {
                length = Integer.parseInt(value);
                continue;
            }
            if (OPTION_LOWER.equalsIgnoreCase(key)) {
                lowerCase = Boolean.parseBoolean(value);
                continue;
            }
            Level level = Level.toLevel(key, null);
            if (level == null) {
                LOGGER.error("Invalid Level {}", key);
                continue;
            }
            levelMap.put(level, value);
        }
        if (levelMap.size() == 0 && length == Integer.MAX_VALUE && !lowerCase) {
            return INSTANCE;
        }
        for (Level level : Level.values()) {
            if (levelMap.containsKey((Object)level)) continue;
            String left = LevelPatternConverter.left(level, length);
            levelMap.put(level, lowerCase ? left.toLowerCase(Locale.US) : left);
        }
        return new LevelPatternConverter(levelMap);
    }

    private static String left(Level level, int length) {
        String string = level.toString();
        if (length >= string.length()) {
            return string;
        }
        return string.substring(0, length);
    }

    @Override
    public void format(LogEvent event, StringBuilder output) {
        output.append(this.levelMap == null ? event.getLevel().toString() : this.levelMap.get((Object)event.getLevel()));
    }

    @Override
    public String getStyleClass(Object e) {
        if (e instanceof LogEvent) {
            Level level = ((LogEvent)e).getLevel();
            switch (level) {
                case TRACE: {
                    return "level trace";
                }
                case DEBUG: {
                    return "level debug";
                }
                case INFO: {
                    return "level info";
                }
                case WARN: {
                    return "level warn";
                }
                case ERROR: {
                    return "level error";
                }
                case FATAL: {
                    return "level fatal";
                }
            }
            return "level " + ((LogEvent)e).getLevel().toString();
        }
        return "level";
    }
}

