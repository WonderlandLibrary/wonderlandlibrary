// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.util;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import java.lang.reflect.Method;

public class ProtocolSupportUtil
{
    private static Method protocolVersionMethod;
    private static Method getIdMethod;
    
    public static int getProtocolVersion(final Player player) {
        if (ProtocolSupportUtil.protocolVersionMethod == null) {
            return -1;
        }
        try {
            final Object version = ProtocolSupportUtil.protocolVersionMethod.invoke(null, player);
            return (int)ProtocolSupportUtil.getIdMethod.invoke(version, new Object[0]);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
        return -1;
    }
    
    static {
        ProtocolSupportUtil.protocolVersionMethod = null;
        ProtocolSupportUtil.getIdMethod = null;
        try {
            ProtocolSupportUtil.protocolVersionMethod = Class.forName("protocolsupport.api.ProtocolSupportAPI").getMethod("getProtocolVersion", Player.class);
            ProtocolSupportUtil.getIdMethod = Class.forName("protocolsupport.api.ProtocolVersion").getMethod("getId", (Class<?>[])new Class[0]);
        }
        catch (Exception ex) {}
    }
}
