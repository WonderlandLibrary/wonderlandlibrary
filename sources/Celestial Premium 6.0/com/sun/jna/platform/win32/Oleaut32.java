/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Oleaut32
extends StdCallLibrary {
    public static final Oleaut32 INSTANCE = Native.loadLibrary("Oleaut32", Oleaut32.class, W32APIOptions.UNICODE_OPTIONS);

    public Pointer SysAllocString(String var1);

    public void SysFreeString(Pointer var1);
}

