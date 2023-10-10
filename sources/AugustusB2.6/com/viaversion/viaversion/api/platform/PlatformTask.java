// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.platform;

public interface PlatformTask<T>
{
    T getObject();
    
    void cancel();
}
