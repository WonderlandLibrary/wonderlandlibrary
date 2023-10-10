// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.Pointer;

public class Unknown extends COMInvoker implements IUnknown
{
    public Unknown() {
    }
    
    public Unknown(final Pointer pvInstance) {
        this.setPointer(pvInstance);
    }
    
    @Override
    public WinNT.HRESULT QueryInterface(final Guid.REFIID riid, final PointerByReference ppvObject) {
        return (WinNT.HRESULT)this._invokeNativeObject(0, new Object[] { this.getPointer(), riid, ppvObject }, WinNT.HRESULT.class);
    }
    
    @Override
    public int AddRef() {
        return this._invokeNativeInt(1, new Object[] { this.getPointer() });
    }
    
    @Override
    public int Release() {
        return this._invokeNativeInt(2, new Object[] { this.getPointer() });
    }
    
    public static class ByReference extends Unknown implements Structure.ByReference
    {
    }
}
