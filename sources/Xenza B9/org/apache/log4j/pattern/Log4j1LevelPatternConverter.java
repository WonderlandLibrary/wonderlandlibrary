// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.pattern;

import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name = "Log4j1LevelPatternConverter", category = "Converter")
@ConverterKeys({ "v1Level" })
public class Log4j1LevelPatternConverter extends LogEventPatternConverter
{
    private static final Log4j1LevelPatternConverter INSTANCE;
    
    public static Log4j1LevelPatternConverter newInstance(final String[] options) {
        return Log4j1LevelPatternConverter.INSTANCE;
    }
    
    private Log4j1LevelPatternConverter() {
        super("Log4j1Level", "v1Level");
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(OptionConverter.convertLevel(event.getLevel()).toString());
    }
    
    static {
        INSTANCE = new Log4j1LevelPatternConverter();
    }
}
