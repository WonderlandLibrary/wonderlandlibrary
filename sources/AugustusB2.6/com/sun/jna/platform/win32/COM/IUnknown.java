// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;

public interface IUnknown
{
    public static final Guid.IID IID_IUNKNOWN = new Guid.IID("{00000000-0000-0000-C000-000000000046}");
    
    WinNT.HRESULT QueryInterface(final Guid.REFIID p0, final PointerByReference p1);
    
    int AddRef();
    
    int Release();
}
