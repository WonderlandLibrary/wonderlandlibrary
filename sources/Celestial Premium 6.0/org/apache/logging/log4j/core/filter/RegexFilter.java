/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

@Plugin(name="RegexFilter", category="Core", elementType="filter", printObject=true)
public final class RegexFilter
extends AbstractFilter {
    private final Pattern pattern;
    private final boolean useRawMessage;

    private RegexFilter(boolean raw, Pattern pattern, Filter.Result onMatch, Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.pattern = pattern;
        this.useRawMessage = raw;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object ... params) {
        return this.filter(msg);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        if (msg == null) {
            return this.onMismatch;
        }
        return this.filter(msg.toString());
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        if (msg == null) {
            return this.onMismatch;
        }
        String text = this.useRawMessage ? msg.getFormat() : msg.getFormattedMessage();
        return this.filter(text);
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        String text = this.useRawMessage ? event.getMessage().getFormat() : event.getMessage().getFormattedMessage();
        return this.filter(text);
    }

    private Filter.Result filter(String msg) {
        if (msg == null) {
            return this.onMismatch;
        }
        Matcher m = this.pattern.matcher(msg);
        return m.matches() ? this.onMatch : this.onMismatch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("useRaw=").append(this.useRawMessage);
        sb.append(", pattern=").append(this.pattern.toString());
        return sb.toString();
    }

    @PluginFactory
    public static RegexFilter createFilter(@PluginAttribute(value="regex") String regex, @PluginAttribute(value="useRawMsg") String useRawMsg, @PluginAttribute(value="onMatch") String match, @PluginAttribute(value="onMismatch") String mismatch) {
        Pattern pattern;
        if (regex == null) {
            LOGGER.error("A regular expression must be provided for RegexFilter");
            return null;
        }
        boolean raw = Boolean.parseBoolean(useRawMsg);
        try {
            pattern = Pattern.compile(regex);
        }
        catch (Exception ex) {
            LOGGER.error("RegexFilter caught exception compiling pattern: " + regex + " cause: " + ex.getMessage());
            return null;
        }
        Filter.Result onMatch = Filter.Result.toResult(match);
        Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        return new RegexFilter(raw, pattern, onMatch, onMismatch);
    }
}

