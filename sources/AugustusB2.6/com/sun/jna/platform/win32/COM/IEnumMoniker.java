// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;

public interface IEnumMoniker extends IUnknown
{
    public static final Guid.IID IID = new Guid.IID("{00000102-0000-0000-C000-000000000046}");
    
    WinNT.HRESULT Clone(final PointerByReference p0);
    
    WinNT.HRESULT Next(final WinDef.ULONG p0, final PointerByReference p1, final WinDef.ULONGByReference p2);
    
    WinNT.HRESULT Reset();
    
    WinNT.HRESULT Skip(final WinDef.ULONG p0);
}
