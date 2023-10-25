/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;

@Plugin(name="MapFilter", category="Core", elementType="filter", printObject=true)
public class MapFilter
extends AbstractFilter {
    private final Map<String, List<String>> map;
    private final boolean isAnd;

    protected MapFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        if (map == null) {
            throw new NullPointerException("key cannot be null");
        }
        this.isAnd = oper;
        this.map = map;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        if (msg instanceof MapMessage) {
            return this.filter(((MapMessage)msg).getData()) ? this.onMatch : this.onMismatch;
        }
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        Message msg = event.getMessage();
        if (msg instanceof MapMessage) {
            return this.filter(((MapMessage)msg).getData()) ? this.onMatch : this.onMismatch;
        }
        return Filter.Result.NEUTRAL;
    }

    protected boolean filter(Map<String, String> data) {
        boolean match = false;
        for (Map.Entry<String, List<String>> entry : this.map.entrySet()) {
            String toMatch = data.get(entry.getKey());
            match = toMatch != null ? entry.getValue().contains(toMatch) : false;
            if ((this.isAnd || !match) && (!this.isAnd || match)) continue;
            break;
        }
        return match;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("isAnd=").append(this.isAnd);
        if (this.map.size() > 0) {
            sb.append(", {");
            boolean first = true;
            for (Map.Entry<String, List<String>> entry : this.map.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                List<String> list = entry.getValue();
                String value = list.size() > 1 ? list.get(0) : list.toString();
                sb.append(entry.getKey()).append("=").append(value);
            }
            sb.append("}");
        }
        return sb.toString();
    }

    protected boolean isAnd() {
        return this.isAnd;
    }

    protected Map<String, List<String>> getMap() {
        return this.map;
    }

    @PluginFactory
    public static MapFilter createFilter(@PluginElement(value="Pairs") KeyValuePair[] pairs, @PluginAttribute(value="operator") String oper, @PluginAttribute(value="onMatch") String match, @PluginAttribute(value="onMismatch") String mismatch) {
        if (pairs == null || pairs.length == 0) {
            LOGGER.error("keys and values must be specified for the MapFilter");
            return null;
        }
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        for (KeyValuePair pair : pairs) {
            String key = pair.getKey();
            if (key == null) {
                LOGGER.error("A null key is not valid in MapFilter");
                continue;
            }
            String value = pair.getValue();
            if (value == null) {
                LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
                continue;
            }
            ArrayList<String> list = (ArrayList<String>)map.get(pair.getKey());
            if (list != null) {
                list.add(value);
                continue;
            }
            list = new ArrayList<String>();
            list.add(value);
            map.put(pair.getKey(), list);
        }
        if (map.size() == 0) {
            LOGGER.error("MapFilter is not configured with any valid key value pairs");
            return null;
        }
        boolean isAnd = oper == null || !oper.equalsIgnoreCase("or");
        Filter.Result onMatch = Filter.Result.toResult(match);
        Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        return new MapFilter(map, isAnd, onMatch, onMismatch);
    }
}

