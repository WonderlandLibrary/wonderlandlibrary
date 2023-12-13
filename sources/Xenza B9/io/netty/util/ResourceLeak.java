// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

public interface ResourceLeak
{
    void record();
    
    boolean close();
}
