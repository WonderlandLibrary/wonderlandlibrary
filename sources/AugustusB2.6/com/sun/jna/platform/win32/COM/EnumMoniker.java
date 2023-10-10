// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.Pointer;

public class EnumMoniker extends Unknown implements IEnumMoniker
{
    public EnumMoniker(final Pointer pointer) {
        super(pointer);
    }
    
    @Override
    public WinNT.HRESULT Next(final WinDef.ULONG celt, final PointerByReference rgelt, final WinDef.ULONGByReference pceltFetched) {
        final int vTableId = 3;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(3, new Object[] { this.getPointer(), celt, rgelt, pceltFetched }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT Skip(final WinDef.ULONG celt) {
        final int vTableId = 4;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), celt }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT Reset() {
        final int vTableId = 5;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(5, new Object[] { this.getPointer() }, WinNT.HRESULT.class);
        return hr;
    }
    
    @Override
    public WinNT.HRESULT Clone(final PointerByReference ppenum) {
        final int vTableId = 6;
        final WinNT.HRESULT hr = (WinNT.HRESULT)this._invokeNativeObject(6, new Object[] { this.getPointer(), ppenum }, WinNT.HRESULT.class);
        return hr;
    }
}
