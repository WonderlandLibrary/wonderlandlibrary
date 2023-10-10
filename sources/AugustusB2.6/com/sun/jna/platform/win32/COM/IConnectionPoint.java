// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Guid;

public interface IConnectionPoint extends IUnknown
{
    public static final Guid.IID IID_IConnectionPoint = new Guid.IID("B196B286-BAB4-101A-B69C-00AA00341D07");
    
    WinNT.HRESULT GetConnectionInterface(final Guid.IID p0);
    
    WinNT.HRESULT Advise(final IUnknownCallback p0, final WinDef.DWORDByReference p1);
    
    WinNT.HRESULT Unadvise(final WinDef.DWORD p0);
}
