// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.unix;

import com.sun.jna.Native;
import com.sun.jna.Library;

public interface LibC extends LibCAPI, Library
{
    public static final String NAME = "c";
    public static final LibC INSTANCE = Native.load("c", LibC.class);
}
