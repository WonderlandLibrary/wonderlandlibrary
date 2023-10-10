// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os;

import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface OSFileStore
{
    String getName();
    
    String getVolume();
    
    String getLabel();
    
    String getLogicalVolume();
    
    String getMount();
    
    String getDescription();
    
    String getType();
    
    String getOptions();
    
    String getUUID();
    
    long getFreeSpace();
    
    long getUsableSpace();
    
    long getTotalSpace();
    
    long getFreeInodes();
    
    long getTotalInodes();
    
    boolean updateAttributes();
}
