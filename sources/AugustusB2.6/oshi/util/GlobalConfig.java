// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.util.Map;
import java.util.Properties;
import oshi.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class GlobalConfig
{
    private static final String OSHI_PROPERTIES = "oshi.properties";
    private static final Properties CONFIG;
    
    private GlobalConfig() {
    }
    
    public static String get(final String key, final String def) {
        return GlobalConfig.CONFIG.getProperty(key, def);
    }
    
    public static int get(final String key, final int def) {
        final String value = GlobalConfig.CONFIG.getProperty(key);
        return (value == null) ? def : ParseUtil.parseIntOrDefault(value, def);
    }
    
    public static double get(final String key, final double def) {
        final String value = GlobalConfig.CONFIG.getProperty(key);
        return (value == null) ? def : ParseUtil.parseDoubleOrDefault(value, def);
    }
    
    public static boolean get(final String key, final boolean def) {
        final String value = GlobalConfig.CONFIG.getProperty(key);
        return (value == null) ? def : Boolean.parseBoolean(value);
    }
    
    public static void set(final String key, final Object val) {
        if (val == null) {
            GlobalConfig.CONFIG.remove(key);
        }
        else {
            GlobalConfig.CONFIG.setProperty(key, val.toString());
        }
    }
    
    public static void remove(final String key) {
        GlobalConfig.CONFIG.remove(key);
    }
    
    public static void clear() {
        GlobalConfig.CONFIG.clear();
    }
    
    public static void load(final Properties properties) {
        GlobalConfig.CONFIG.putAll(properties);
    }
    
    static {
        CONFIG = FileUtil.readPropertiesFromFilename("oshi.properties");
    }
    
    public static class PropertyException extends RuntimeException
    {
        private static final long serialVersionUID = -7482581936621748005L;
        
        public PropertyException(final String property) {
            super("Invalid property: \"" + property + "\" = " + GlobalConfig.get(property, null));
        }
        
        public PropertyException(final String property, final String message) {
            super("Invalid property \"" + property + "\": " + message);
        }
    }
}
