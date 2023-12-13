// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.Strings;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "map", category = "Lookup")
public class MapLookup implements StrLookup
{
    private final Map<String, String> map;
    
    public MapLookup() {
        this.map = null;
    }
    
    public MapLookup(final Map<String, String> map) {
        this.map = map;
    }
    
    static Map<String, String> initMap(final String[] srcArgs, final Map<String, String> destMap) {
        for (int i = 0; i < srcArgs.length; ++i) {
            final int next = i + 1;
            final String value = srcArgs[i];
            destMap.put(Integer.toString(i), value);
            destMap.put(value, (next < srcArgs.length) ? srcArgs[next] : null);
        }
        return destMap;
    }
    
    static HashMap<String, String> newMap(final int initialCapacity) {
        return new HashMap<String, String>(initialCapacity);
    }
    
    @Deprecated
    public static void setMainArguments(final String... args) {
        MainMapLookup.setMainArguments(args);
    }
    
    static Map<String, String> toMap(final List<String> args) {
        if (args == null) {
            return null;
        }
        final int size = args.size();
        return initMap(args.toArray(Strings.EMPTY_ARRAY), newMap(size));
    }
    
    static Map<String, String> toMap(final String[] args) {
        if (args == null) {
            return null;
        }
        return initMap(args, newMap(args.length));
    }
    
    protected Map<String, String> getMap() {
        return this.map;
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        final boolean isMapMessage = event != null && event.getMessage() instanceof MapMessage;
        if (isMapMessage) {
            final String obj = ((MapMessage)event.getMessage()).get(key);
            if (obj != null) {
                return obj;
            }
        }
        if (this.map != null) {
            return this.map.get(key);
        }
        return null;
    }
    
    @Override
    public String lookup(final String key) {
        if (key == null || this.map == null) {
            return null;
        }
        return this.map.get(key);
    }
}
