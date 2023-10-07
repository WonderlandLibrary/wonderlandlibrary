// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import java.util.HashMap;
import java.util.Hashtable;
import org.apache.logging.log4j.ThreadContext;
import java.util.Map;

public final class MDC
{
    private static ThreadLocal<Map<String, Object>> localMap;
    
    private MDC() {
    }
    
    public static void put(final String key, final String value) {
        MDC.localMap.get().put((Object)key, (Object)value);
        ThreadContext.put(key, value);
    }
    
    public static void put(final String key, final Object value) {
        MDC.localMap.get().put(key, value);
        ThreadContext.put(key, value.toString());
    }
    
    public static Object get(final String key) {
        return MDC.localMap.get().get(key);
    }
    
    public static void remove(final String key) {
        MDC.localMap.get().remove(key);
        ThreadContext.remove(key);
    }
    
    public static void clear() {
        MDC.localMap.get().clear();
        ThreadContext.clearMap();
    }
    
    public static Hashtable<String, Object> getContext() {
        return new Hashtable<String, Object>(MDC.localMap.get());
    }
    
    static {
        MDC.localMap = new InheritableThreadLocal<Map<String, Object>>() {
            @Override
            protected Map<String, Object> initialValue() {
                return new HashMap<String, Object>();
            }
            
            @Override
            protected Map<String, Object> childValue(final Map<String, Object> parentValue) {
                return (parentValue == null) ? new HashMap<String, Object>() : new HashMap<String, Object>(parentValue);
            }
        };
    }
}
