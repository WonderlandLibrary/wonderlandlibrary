// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.Pointer;

public class ConnectionPoint extends Unknown implements IConnectionPoint
{
    public ConnectionPoint(final Pointer pointer) {
        super(pointer);
    }
    
    @Override
    public WinNT.HRESULT GetConnectionInterface(final Guid.IID iid) {
        final int vTableId = 3;
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[] { this.getPointer(), iid }, WinNT.HRESULT.class);
    }
    
    void GetConnectionPointContainer() {
        final int vTableId = 4;
    }
    
    @Override
    public WinNT.HRESULT Advise(final IUnknownCallback pUnkSink, final WinDef.DWORDByReference pdwCookie) {
        final int vTableId = 5;
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[] { this.getPointer(), pUnkSink.getPointer(), pdwCookie }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT Unadvise(final WinDef.DWORD dwCookie) {
        final int vTableId = 6;
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[] { this.getPointer(), dwCookie }, WinNT.HRESULT.class);
    }
    
    void EnumConnections() {
        final int vTableId = 7;
    }
}
