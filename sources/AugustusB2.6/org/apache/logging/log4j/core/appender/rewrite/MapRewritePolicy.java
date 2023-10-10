// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Iterator;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.util.HashMap;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MapRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public final class MapRewritePolicy implements RewritePolicy
{
    protected static final Logger LOGGER;
    private final Map<String, Object> map;
    private final Mode mode;
    
    private MapRewritePolicy(final Map<String, Object> map, final Mode mode) {
        this.map = map;
        this.mode = mode;
    }
    
    @Override
    public LogEvent rewrite(final LogEvent source) {
        final Message msg = source.getMessage();
        if (msg == null || !(msg instanceof MapMessage)) {
            return source;
        }
        final MapMessage<?, Object> mapMsg = (MapMessage<?, Object>)msg;
        final Map<String, Object> newMap = new HashMap<String, Object>(mapMsg.getData());
        switch (this.mode) {
            case Add: {
                newMap.putAll(this.map);
                break;
            }
            default: {
                for (final Map.Entry<String, Object> entry : this.map.entrySet()) {
                    if (newMap.containsKey(entry.getKey())) {
                        newMap.put(entry.getKey(), entry.getValue());
                    }
                }
                break;
            }
        }
        final Message message = (Message)mapMsg.newInstance(newMap);
        return new Log4jLogEvent.Builder(source).setMessage(message).build();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("mode=").append(this.mode);
        sb.append(" {");
        boolean first = true;
        for (final Map.Entry<String, Object> entry : this.map.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue());
            first = false;
        }
        sb.append('}');
        return sb.toString();
    }
    
    @PluginFactory
    public static MapRewritePolicy createPolicy(@PluginAttribute("mode") final String mode, @PluginElement("KeyValuePair") final KeyValuePair[] pairs) {
        Mode op = (mode == null) ? (op = Mode.Add) : Mode.valueOf(mode);
        if (pairs == null || pairs.length == 0) {
            MapRewritePolicy.LOGGER.error("keys and values must be specified for the MapRewritePolicy");
            return null;
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final KeyValuePair pair : pairs) {
            final String key = pair.getKey();
            if (key == null) {
                MapRewritePolicy.LOGGER.error("A null key is not valid in MapRewritePolicy");
            }
            else {
                final String value = pair.getValue();
                if (value == null) {
                    MapRewritePolicy.LOGGER.error("A null value for key " + key + " is not allowed in MapRewritePolicy");
                }
                else {
                    map.put(pair.getKey(), pair.getValue());
                }
            }
        }
        if (map.isEmpty()) {
            MapRewritePolicy.LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
            return null;
        }
        return new MapRewritePolicy(map, op);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public enum Mode
    {
        Add, 
        Update;
    }
}
