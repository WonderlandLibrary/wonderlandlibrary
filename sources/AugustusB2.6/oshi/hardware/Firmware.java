// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import oshi.annotation.concurrent.Immutable;

@Immutable
public interface Firmware
{
    String getManufacturer();
    
    String getName();
    
    String getDescription();
    
    String getVersion();
    
    String getReleaseDate();
}
