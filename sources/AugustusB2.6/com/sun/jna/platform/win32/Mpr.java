// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Mpr extends StdCallLibrary
{
    public static final Mpr INSTANCE = Native.load("Mpr", Mpr.class, W32APIOptions.DEFAULT_OPTIONS);
    
    int WNetOpenEnum(final int p0, final int p1, final int p2, final Winnetwk.NETRESOURCE.ByReference p3, final WinNT.HANDLEByReference p4);
    
    int WNetEnumResource(final WinNT.HANDLE p0, final IntByReference p1, final Pointer p2, final IntByReference p3);
    
    int WNetCloseEnum(final WinNT.HANDLE p0);
    
    int WNetGetUniversalName(final String p0, final int p1, final Pointer p2, final IntByReference p3);
    
    int WNetUseConnection(final WinDef.HWND p0, final Winnetwk.NETRESOURCE p1, final String p2, final String p3, final int p4, final Pointer p5, final IntByReference p6, final IntByReference p7);
    
    int WNetAddConnection3(final WinDef.HWND p0, final Winnetwk.NETRESOURCE p1, final String p2, final String p3, final int p4);
    
    int WNetCancelConnection2(final String p0, final int p1, final boolean p2);
}
