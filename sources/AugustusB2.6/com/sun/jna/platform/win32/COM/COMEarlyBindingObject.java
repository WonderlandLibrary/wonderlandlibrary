// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Guid;

public class COMEarlyBindingObject extends COMBindingBaseObject implements IDispatch
{
    public COMEarlyBindingObject(final Guid.CLSID clsid, final boolean useActiveInstance, final int dwClsContext) {
        super(clsid, useActiveInstance, dwClsContext);
    }
    
    protected String getStringProperty(final OaIdl.DISPID dispId) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, dispId);
        return result.getValue().toString();
    }
    
    protected void setProperty(final OaIdl.DISPID dispId, final boolean value) {
        this.oleMethod(4, null, dispId, new Variant.VARIANT(value));
    }
    
    @Override
    public WinNT.HRESULT QueryInterface(final Guid.REFIID riid, final PointerByReference ppvObject) {
        return this.getIDispatch().QueryInterface(riid, ppvObject);
    }
    
    @Override
    public int AddRef() {
        return this.getIDispatch().AddRef();
    }
    
    @Override
    public int Release() {
        return this.getIDispatch().Release();
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfoCount(final WinDef.UINTByReference pctinfo) {
        return this.getIDispatch().GetTypeInfoCount(pctinfo);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfo(final WinDef.UINT iTInfo, final WinDef.LCID lcid, final PointerByReference ppTInfo) {
        return this.getIDispatch().GetTypeInfo(iTInfo, lcid, ppTInfo);
    }
    
    @Override
    public WinNT.HRESULT GetIDsOfNames(final Guid.REFIID riid, final WString[] rgszNames, final int cNames, final WinDef.LCID lcid, final OaIdl.DISPIDByReference rgDispId) {
        return this.getIDispatch().GetIDsOfNames(riid, rgszNames, cNames, lcid, rgDispId);
    }
    
    @Override
    public WinNT.HRESULT Invoke(final OaIdl.DISPID dispIdMember, final Guid.REFIID riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams, final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final IntByReference puArgErr) {
        return this.getIDispatch().Invoke(dispIdMember, riid, lcid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr);
    }
}
