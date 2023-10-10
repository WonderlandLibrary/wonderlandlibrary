// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;

public final class Via
{
    private static ViaManager manager;
    
    public static ViaAPI getAPI() {
        return manager().getPlatform().getApi();
    }
    
    public static ViaManager getManager() {
        return manager();
    }
    
    public static ViaVersionConfig getConfig() {
        return manager().getPlatform().getConf();
    }
    
    public static ViaPlatform getPlatform() {
        return manager().getPlatform();
    }
    
    public static void init(final ViaManager viaManager) {
        Preconditions.checkArgument(Via.manager == null, (Object)"ViaManager is already set");
        Via.manager = viaManager;
    }
    
    private static ViaManager manager() {
        Preconditions.checkArgument(Via.manager != null, (Object)"ViaVersion has not loaded the platform yet");
        return Via.manager;
    }
}
