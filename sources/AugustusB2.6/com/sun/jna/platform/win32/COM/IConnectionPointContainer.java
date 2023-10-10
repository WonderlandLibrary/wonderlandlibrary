// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;

public interface IConnectionPointContainer extends IUnknown
{
    public static final Guid.IID IID_IConnectionPointContainer = new Guid.IID("B196B284-BAB4-101A-B69C-00AA00341D07");
    
    WinNT.HRESULT FindConnectionPoint(final Guid.REFIID p0, final PointerByReference p1);
}
