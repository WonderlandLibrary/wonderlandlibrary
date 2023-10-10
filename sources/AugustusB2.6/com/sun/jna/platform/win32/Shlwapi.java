// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Shlwapi extends StdCallLibrary
{
    public static final Shlwapi INSTANCE = Native.load("Shlwapi", Shlwapi.class, W32APIOptions.DEFAULT_OPTIONS);
    
    WinNT.HRESULT StrRetToStr(final ShTypes.STRRET p0, final Pointer p1, final PointerByReference p2);
    
    boolean PathIsUNC(final String p0);
}
