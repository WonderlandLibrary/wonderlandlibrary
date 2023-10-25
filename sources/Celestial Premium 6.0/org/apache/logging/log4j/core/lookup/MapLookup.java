/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(name="map", category="Lookup")
public class MapLookup
implements StrLookup {
    private final Map<String, String> map;

    public MapLookup(Map<String, String> map) {
        this.map = map;
    }

    public MapLookup() {
        this.map = null;
    }

    @Override
    public String lookup(String key) {
        if (this.map == null) {
            return null;
        }
        String obj = this.map.get(key);
        if (obj == null) {
            return null;
        }
        return obj;
    }

    @Override
    public String lookup(LogEvent event, String key) {
        String obj;
        if (this.map == null && !(event.getMessage() instanceof MapMessage)) {
            return null;
        }
        if (this.map != null && this.map.containsKey(key) && (obj = this.map.get(key)) != null) {
            return obj;
        }
        if (event.getMessage() instanceof MapMessage) {
            return ((MapMessage)event.getMessage()).get(key);
        }
        return null;
    }
}

