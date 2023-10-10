// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.Pointer;

public class ConnectionPointContainer extends Unknown implements IConnectionPointContainer
{
    public ConnectionPointContainer(final Pointer pointer) {
        super(pointer);
    }
    
    public WinNT.HRESULT EnumConnectionPoints() {
        final int vTableId = 3;
        throw new UnsupportedOperationException();
    }
    
    @Override
    public WinNT.HRESULT FindConnectionPoint(final Guid.REFIID riid, final PointerByReference ppCP) {
        final int vTableId = 4;
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), riid, ppCP }, WinNT.HRESULT.class);
    }
}
