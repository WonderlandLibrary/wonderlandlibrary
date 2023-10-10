// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.wince;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Library;
import com.sun.jna.platform.win32.WinNT;

public interface CoreDLL extends WinNT, Library
{
    public static final CoreDLL INSTANCE = Native.load("coredll", CoreDLL.class, W32APIOptions.UNICODE_OPTIONS);
}
