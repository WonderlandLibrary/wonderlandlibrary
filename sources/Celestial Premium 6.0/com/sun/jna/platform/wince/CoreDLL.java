/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.wince;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public interface CoreDLL
extends WinNT {
    public static final CoreDLL INSTANCE = Native.loadLibrary("coredll", CoreDLL.class, W32APIOptions.UNICODE_OPTIONS);
}

