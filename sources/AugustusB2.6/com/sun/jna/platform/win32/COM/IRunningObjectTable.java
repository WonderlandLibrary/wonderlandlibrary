// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;

public interface IRunningObjectTable extends IUnknown
{
    public static final Guid.IID IID = new Guid.IID("{00000010-0000-0000-C000-000000000046}");
    
    WinNT.HRESULT EnumRunning(final PointerByReference p0);
    
    WinNT.HRESULT GetObject(final Pointer p0, final PointerByReference p1);
    
    WinNT.HRESULT GetTimeOfLastChange(final Pointer p0, final WinBase.FILETIME.ByReference p1);
    
    WinNT.HRESULT IsRunning(final Pointer p0);
    
    WinNT.HRESULT NoteChangeTime(final WinDef.DWORD p0, final WinBase.FILETIME p1);
    
    WinNT.HRESULT Register(final WinDef.DWORD p0, final Pointer p1, final Pointer p2, final WinDef.DWORDByReference p3);
    
    WinNT.HRESULT Revoke(final WinDef.DWORD p0);
}
