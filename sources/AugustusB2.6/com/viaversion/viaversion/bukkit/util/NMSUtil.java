// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.util;

import org.bukkit.Bukkit;

public class NMSUtil
{
    private static final String BASE;
    private static final String NMS;
    private static final boolean DEBUG_PROPERTY;
    
    private static boolean loadDebugProperty() {
        try {
            final Class<?> serverClass = nms("MinecraftServer", "net.minecraft.server.MinecraftServer");
            final Object server = serverClass.getDeclaredMethod("getServer", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            return (boolean)serverClass.getMethod("isDebugging", (Class<?>[])new Class[0]).invoke(server, new Object[0]);
        }
        catch (ReflectiveOperationException e) {
            return false;
        }
    }
    
    public static Class<?> nms(final String className) throws ClassNotFoundException {
        return Class.forName(NMSUtil.NMS + "." + className);
    }
    
    public static Class<?> nms(final String className, final String fallbackFullClassName) throws ClassNotFoundException {
        try {
            return Class.forName(NMSUtil.NMS + "." + className);
        }
        catch (ClassNotFoundException ignored) {
            return Class.forName(fallbackFullClassName);
        }
    }
    
    public static Class<?> obc(final String className) throws ClassNotFoundException {
        return Class.forName(NMSUtil.BASE + "." + className);
    }
    
    public static String getVersion() {
        return NMSUtil.BASE.substring(NMSUtil.BASE.lastIndexOf(46) + 1);
    }
    
    public static boolean isDebugPropertySet() {
        return NMSUtil.DEBUG_PROPERTY;
    }
    
    static {
        BASE = Bukkit.getServer().getClass().getPackage().getName();
        NMS = NMSUtil.BASE.replace("org.bukkit.craftbukkit", "net.minecraft.server");
        DEBUG_PROPERTY = loadDebugProperty();
    }
}
