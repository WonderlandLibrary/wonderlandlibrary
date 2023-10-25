/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Ole32
extends StdCallLibrary {
    public static final Ole32 INSTANCE = Native.loadLibrary("Ole32", Ole32.class, W32APIOptions.UNICODE_OPTIONS);

    public WinNT.HRESULT CoCreateGuid(Guid.GUID.ByReference var1);

    public int StringFromGUID2(Guid.GUID.ByReference var1, char[] var2, int var3);

    public WinNT.HRESULT IIDFromString(String var1, Guid.GUID.ByReference var2);

    public WinNT.HRESULT CoInitializeEx(Pointer var1, int var2);

    public void CoUninitialize();

    public WinNT.HRESULT CoCreateInstance(Guid.GUID var1, Pointer var2, int var3, Guid.GUID var4, PointerByReference var5);
}

