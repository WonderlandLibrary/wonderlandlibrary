// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Ole32 extends StdCallLibrary
{
    public static final Ole32 INSTANCE = Native.load("Ole32", Ole32.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int COINIT_APARTMENTTHREADED = 2;
    public static final int COINIT_MULTITHREADED = 0;
    public static final int COINIT_DISABLE_OLE1DDE = 4;
    public static final int COINIT_SPEED_OVER_MEMORY = 8;
    public static final int RPC_C_AUTHN_LEVEL_DEFAULT = 0;
    public static final int RPC_C_AUTHN_WINNT = 10;
    public static final int RPC_C_IMP_LEVEL_IMPERSONATE = 3;
    public static final int RPC_C_AUTHZ_NONE = 0;
    public static final int RPC_C_AUTHN_LEVEL_CALL = 3;
    public static final int EOAC_NONE = 0;
    
    WinNT.HRESULT CoCreateGuid(final Guid.GUID p0);
    
    int StringFromGUID2(final Guid.GUID p0, final char[] p1, final int p2);
    
    WinNT.HRESULT IIDFromString(final String p0, final Guid.GUID p1);
    
    WinNT.HRESULT CoInitialize(final WinDef.LPVOID p0);
    
    WinNT.HRESULT CoInitializeEx(final Pointer p0, final int p1);
    
    WinNT.HRESULT CoInitializeSecurity(final WinNT.SECURITY_DESCRIPTOR p0, final int p1, final Pointer p2, final Pointer p3, final int p4, final int p5, final Pointer p6, final int p7, final Pointer p8);
    
    WinNT.HRESULT CoSetProxyBlanket(final Unknown p0, final int p1, final int p2, final WTypes.LPOLESTR p3, final int p4, final int p5, final Pointer p6, final int p7);
    
    void CoUninitialize();
    
    WinNT.HRESULT CoCreateInstance(final Guid.GUID p0, final Pointer p1, final int p2, final Guid.GUID p3, final PointerByReference p4);
    
    WinNT.HRESULT CLSIDFromProgID(final String p0, final Guid.CLSID.ByReference p1);
    
    WinNT.HRESULT CLSIDFromString(final String p0, final Guid.CLSID.ByReference p1);
    
    Pointer CoTaskMemAlloc(final long p0);
    
    Pointer CoTaskMemRealloc(final Pointer p0, final long p1);
    
    void CoTaskMemFree(final Pointer p0);
    
    WinNT.HRESULT CoGetMalloc(final WinDef.DWORD p0, final PointerByReference p1);
    
    WinNT.HRESULT GetRunningObjectTable(final WinDef.DWORD p0, final PointerByReference p1);
    
    WinNT.HRESULT CreateBindCtx(final WinDef.DWORD p0, final PointerByReference p1);
    
    boolean CoIsHandlerConnected(final Pointer p0);
    
    WinNT.HRESULT OleInitialize(final Pointer p0);
    
    void OleUninitialize();
    
    WinNT.HRESULT OleFlushClipboard();
    
    WinNT.HRESULT OleRun(final Pointer p0);
}
