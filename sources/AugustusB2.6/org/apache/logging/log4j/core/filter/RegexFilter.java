// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.regex.Matcher;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RegexFilter", category = "Core", elementType = "filter", printObject = true)
public final class RegexFilter extends AbstractFilter
{
    private static final int DEFAULT_PATTERN_FLAGS = 0;
    private final Pattern pattern;
    private final boolean useRawMessage;
    
    private RegexFilter(final boolean raw, final Pattern pattern, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.pattern = pattern;
        this.useRawMessage = raw;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(msg);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        if (msg == null) {
            return this.onMismatch;
        }
        return this.filter(msg.toString());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        if (msg == null) {
            return this.onMismatch;
        }
        final String text = this.useRawMessage ? msg.getFormat() : msg.getFormattedMessage();
        return this.filter(text);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final String text = this.useRawMessage ? event.getMessage().getFormat() : event.getMessage().getFormattedMessage();
        return this.filter(text);
    }
    
    private Filter.Result filter(final String msg) {
        if (msg == null) {
            return this.onMismatch;
        }
        final Matcher m = this.pattern.matcher(msg);
        return m.matches() ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("useRaw=").append(this.useRawMessage);
        sb.append(", pattern=").append(this.pattern.toString());
        return sb.toString();
    }
    
    @PluginFactory
    public static RegexFilter createFilter(@PluginAttribute("regex") final String regex, @PluginElement("PatternFlags") final String[] patternFlags, @PluginAttribute("useRawMsg") final Boolean useRawMsg, @PluginAttribute("onMatch") final Filter.Result match, @PluginAttribute("onMismatch") final Filter.Result mismatch) throws IllegalArgumentException, IllegalAccessException {
        if (regex == null) {
            RegexFilter.LOGGER.error("A regular expression must be provided for RegexFilter");
            return null;
        }
        return new RegexFilter(useRawMsg, Pattern.compile(regex, toPatternFlags(patternFlags)), match, mismatch);
    }
    
    private static int toPatternFlags(final String[] patternFlags) throws IllegalArgumentException, IllegalAccessException {
        if (patternFlags == null || patternFlags.length == 0) {
            return 0;
        }
        final Field[] fields = Pattern.class.getDeclaredFields();
        final Comparator<Field> comparator = (f1, f2) -> f1.getName().compareTo(f2.getName());
        Arrays.sort(fields, comparator);
        final String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; ++i) {
            fieldNames[i] = fields[i].getName();
        }
        int flags = 0;
        for (final String test : patternFlags) {
            final int index = Arrays.binarySearch(fieldNames, test);
            if (index >= 0) {
                final Field field = fields[index];
                flags |= field.getInt(Pattern.class);
            }
        }
        return flags;
    }
}
