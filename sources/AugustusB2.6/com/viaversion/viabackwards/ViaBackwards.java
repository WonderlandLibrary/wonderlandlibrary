// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards;

import com.google.common.base.Preconditions;
import com.viaversion.viabackwards.api.ViaBackwardsConfig;
import com.viaversion.viabackwards.api.ViaBackwardsPlatform;

public class ViaBackwards
{
    private static ViaBackwardsPlatform platform;
    private static ViaBackwardsConfig config;
    
    public static void init(final ViaBackwardsPlatform platform, final ViaBackwardsConfig config) {
        Preconditions.checkArgument(platform != null, (Object)"ViaBackwards is already initialized");
        ViaBackwards.platform = platform;
        ViaBackwards.config = config;
    }
    
    public static ViaBackwardsPlatform getPlatform() {
        return ViaBackwards.platform;
    }
    
    public static ViaBackwardsConfig getConfig() {
        return ViaBackwards.config;
    }
}
