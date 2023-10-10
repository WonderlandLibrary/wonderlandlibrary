// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import oshi.annotation.concurrent.Immutable;

@Immutable
public interface GraphicsCard
{
    String getName();
    
    String getDeviceId();
    
    String getVendor();
    
    String getVersionInfo();
    
    long getVRam();
}
