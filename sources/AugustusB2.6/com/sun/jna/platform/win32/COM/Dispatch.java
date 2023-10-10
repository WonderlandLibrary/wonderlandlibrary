// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.Pointer;

public class Dispatch extends Unknown implements IDispatch
{
    public Dispatch() {
    }
    
    public Dispatch(final Pointer pvInstance) {
        super(pvInstance);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfoCount(final WinDef.UINTByReference pctinfo) {
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[] { this.getPointer(), pctinfo }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfo(final WinDef.UINT iTInfo, final WinDef.LCID lcid, final PointerByReference ppTInfo) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), iTInfo, lcid, ppTInfo }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetIDsOfNames(final Guid.REFIID riid, final WString[] rgszNames, final int cNames, final WinDef.LCID lcid, final OaIdl.DISPIDByReference rgDispId) {
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[] { this.getPointer(), riid, rgszNames, cNames, lcid, rgDispId }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT Invoke(final OaIdl.DISPID dispIdMember, final Guid.REFIID riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams, final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final IntByReference puArgErr) {
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[] { this.getPointer(), dispIdMember, riid, lcid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr }, WinNT.HRESULT.class);
    }
    
    public static class ByReference extends Dispatch implements Structure.ByReference
    {
    }
}
