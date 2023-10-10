// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.linux;

import com.sun.jna.Native;
import com.sun.jna.Library;

public interface LibRT extends Library
{
    public static final LibRT INSTANCE = Native.load("rt", LibRT.class);
    
    int shm_open(final String p0, final int p1, final int p2);
    
    int shm_unlink(final String p0);
}
