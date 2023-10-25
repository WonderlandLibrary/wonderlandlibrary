/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rewrite;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="MapRewritePolicy", category="Core", elementType="rewritePolicy", printObject=true)
public final class MapRewritePolicy
implements RewritePolicy {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private final Map<String, String> map;
    private final Mode mode;

    private MapRewritePolicy(Map<String, String> map, Mode mode) {
        this.map = map;
        this.mode = mode;
    }

    @Override
    public LogEvent rewrite(LogEvent source) {
        Message msg = source.getMessage();
        if (msg == null || !(msg instanceof MapMessage)) {
            return source;
        }
        HashMap<String, String> newMap = new HashMap<String, String>(((MapMessage)msg).getData());
        switch (this.mode) {
            case Add: {
                newMap.putAll(this.map);
                break;
            }
            default: {
                for (Map.Entry<String, String> entry : this.map.entrySet()) {
                    if (!newMap.containsKey(entry.getKey())) continue;
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        MapMessage message = ((MapMessage)msg).newInstance(newMap);
        if (source instanceof Log4jLogEvent) {
            Log4jLogEvent event = (Log4jLogEvent)source;
            return Log4jLogEvent.createEvent(event.getLoggerName(), event.getMarker(), event.getFQCN(), event.getLevel(), message, event.getThrownProxy(), event.getContextMap(), event.getContextStack(), event.getThreadName(), event.getSource(), event.getMillis());
        }
        return new Log4jLogEvent(source.getLoggerName(), source.getMarker(), source.getFQCN(), source.getLevel(), (Message)message, source.getThrown(), source.getContextMap(), source.getContextStack(), source.getThreadName(), source.getSource(), source.getMillis());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("mode=").append((Object)this.mode);
        sb.append(" {");
        boolean first = true;
        for (Map.Entry<String, String> entry : this.map.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    @PluginFactory
    public static MapRewritePolicy createPolicy(@PluginAttribute(value="mode") String mode, @PluginElement(value="KeyValuePair") KeyValuePair[] pairs) {
        Mode op;
        if (mode == null) {
            op = Mode.Add;
        } else {
            op = Mode.valueOf(mode);
            if (op == null) {
                LOGGER.error("Undefined mode " + mode);
                return null;
            }
        }
        if (pairs == null || pairs.length == 0) {
            LOGGER.error("keys and values must be specified for the MapRewritePolicy");
            return null;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        for (KeyValuePair pair : pairs) {
            String key = pair.getKey();
            if (key == null) {
                LOGGER.error("A null key is not valid in MapRewritePolicy");
                continue;
            }
            String value = pair.getValue();
            if (value == null) {
                LOGGER.error("A null value for key " + key + " is not allowed in MapRewritePolicy");
                continue;
            }
            map.put(pair.getKey(), pair.getValue());
        }
        if (map.size() == 0) {
            LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
            return null;
        }
        return new MapRewritePolicy(map, op);
    }

    public static enum Mode {
        Add,
        Update;

    }
}

