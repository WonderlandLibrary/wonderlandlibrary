// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "StructuredDataFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({ "allocation" })
public final class StructuredDataFilter extends MapFilter
{
    private static final int MAX_BUFFER_SIZE = 2048;
    private static ThreadLocal<StringBuilder> threadLocalStringBuilder;
    
    private StructuredDataFilter(final Map<String, List<String>> map, final boolean oper, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(map, oper, onMatch, onMismatch);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        if (msg instanceof StructuredDataMessage) {
            return this.filter((StructuredDataMessage)msg);
        }
        return Filter.Result.NEUTRAL;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final Message msg = event.getMessage();
        if (msg instanceof StructuredDataMessage) {
            return this.filter((StructuredDataMessage)msg);
        }
        return super.filter(event);
    }
    
    protected Filter.Result filter(final StructuredDataMessage message) {
        boolean match = false;
        final IndexedReadOnlyStringMap map = this.getStringMap();
        for (int i = 0; i < map.size(); ++i) {
            final StringBuilder toMatch = this.getValue(message, map.getKeyAt(i));
            match = (toMatch != null && this.listContainsValue(map.getValueAt(i), toMatch));
            if (!this.isAnd() && match) {
                break;
            }
            if (this.isAnd() && !match) {
                break;
            }
        }
        return match ? this.onMatch : this.onMismatch;
    }
    
    private StringBuilder getValue(final StructuredDataMessage data, final String key) {
        final StringBuilder sb = this.getStringBuilder();
        if (key.equalsIgnoreCase("id")) {
            data.getId().formatTo(sb);
            return sb;
        }
        if (key.equalsIgnoreCase("id.name")) {
            return this.appendOrNull(data.getId().getName(), sb);
        }
        if (key.equalsIgnoreCase("type")) {
            return this.appendOrNull(data.getType(), sb);
        }
        if (key.equalsIgnoreCase("message")) {
            data.formatTo(sb);
            return sb;
        }
        return this.appendOrNull(data.get(key), sb);
    }
    
    private StringBuilder getStringBuilder() {
        StringBuilder result = StructuredDataFilter.threadLocalStringBuilder.get();
        if (result == null) {
            result = new StringBuilder();
            StructuredDataFilter.threadLocalStringBuilder.set(result);
        }
        StringBuilders.trimToMaxSize(result, 2048);
        result.setLength(0);
        return result;
    }
    
    private StringBuilder appendOrNull(final String value, final StringBuilder sb) {
        if (value == null) {
            return null;
        }
        sb.append(value);
        return sb;
    }
    
    private boolean listContainsValue(final List<String> candidates, final StringBuilder toMatch) {
        if (toMatch == null) {
            for (int i = 0; i < candidates.size(); ++i) {
                final String candidate = candidates.get(i);
                if (candidate == null) {
                    return true;
                }
            }
        }
        else {
            for (int i = 0; i < candidates.size(); ++i) {
                final String candidate = candidates.get(i);
                if (candidate == null) {
                    return false;
                }
                if (StringBuilders.equals(candidate, 0, candidate.length(), toMatch, 0, toMatch.length())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @PluginFactory
    public static StructuredDataFilter createFilter(@PluginElement("Pairs") final KeyValuePair[] pairs, @PluginAttribute("operator") final String oper, @PluginAttribute("onMatch") final Filter.Result match, @PluginAttribute("onMismatch") final Filter.Result mismatch) {
        if (pairs == null || pairs.length == 0) {
            StructuredDataFilter.LOGGER.error("keys and values must be specified for the StructuredDataFilter");
            return null;
        }
        final Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (final KeyValuePair pair : pairs) {
            final String key = pair.getKey();
            if (key == null) {
                StructuredDataFilter.LOGGER.error("A null key is not valid in MapFilter");
            }
            else {
                final String value = pair.getValue();
                if (value == null) {
                    StructuredDataFilter.LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
                }
                else {
                    List<String> list = map.get(pair.getKey());
                    if (list != null) {
                        list.add(value);
                    }
                    else {
                        list = new ArrayList<String>();
                        list.add(value);
                        map.put(pair.getKey(), list);
                    }
                }
            }
        }
        if (map.isEmpty()) {
            StructuredDataFilter.LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
            return null;
        }
        final boolean isAnd = oper == null || !oper.equalsIgnoreCase("or");
        return new StructuredDataFilter(map, isAnd, match, mismatch);
    }
    
    static {
        StructuredDataFilter.threadLocalStringBuilder = new ThreadLocal<StringBuilder>();
    }
}
