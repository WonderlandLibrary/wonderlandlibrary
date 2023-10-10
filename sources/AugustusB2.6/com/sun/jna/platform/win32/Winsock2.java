// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Library;

public interface Winsock2 extends Library
{
    public static final Winsock2 INSTANCE = Native.load("ws2_32", Winsock2.class, W32APIOptions.ASCII_OPTIONS);
    
    int gethostname(final byte[] p0, final int p1);
}
