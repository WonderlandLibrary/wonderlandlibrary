// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;

public class TypeInfo extends Unknown implements ITypeInfo
{
    public TypeInfo() {
    }
    
    public TypeInfo(final Pointer pvInstance) {
        super(pvInstance);
    }
    
    @Override
    public WinNT.HRESULT GetTypeAttr(final PointerByReference ppTypeAttr) {
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[] { this.getPointer(), ppTypeAttr }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetTypeComp(final PointerByReference ppTComp) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), ppTComp }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetFuncDesc(final WinDef.UINT index, final PointerByReference ppFuncDesc) {
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[] { this.getPointer(), index, ppFuncDesc }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetVarDesc(final WinDef.UINT index, final PointerByReference ppVarDesc) {
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[] { this.getPointer(), index, ppVarDesc }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetNames(final OaIdl.MEMBERID memid, final WTypes.BSTR[] rgBstrNames, final WinDef.UINT cMaxNames, final WinDef.UINTByReference pcNames) {
        return (WinNT.HRESULT)this._invokeNativeObject(7, new Object[] { this.getPointer(), memid, rgBstrNames, cMaxNames, pcNames }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetRefTypeOfImplType(final WinDef.UINT index, final OaIdl.HREFTYPEByReference pRefType) {
        return (WinNT.HRESULT)this._invokeNativeObject(8, new Object[] { this.getPointer(), index, pRefType }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetImplTypeFlags(final WinDef.UINT index, final IntByReference pImplTypeFlags) {
        return (WinNT.HRESULT)this._invokeNativeObject(9, new Object[] { this.getPointer(), index, pImplTypeFlags }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetIDsOfNames(final WTypes.LPOLESTR[] rgszNames, final WinDef.UINT cNames, final OaIdl.MEMBERID[] pMemId) {
        return (WinNT.HRESULT)this._invokeNativeObject(10, new Object[] { this.getPointer(), rgszNames, cNames, pMemId }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT Invoke(final WinDef.PVOID pvInstance, final OaIdl.MEMBERID memid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams, final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final WinDef.UINTByReference puArgErr) {
        return (WinNT.HRESULT)this._invokeNativeObject(11, new Object[] { this.getPointer(), pvInstance, memid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetDocumentation(final OaIdl.MEMBERID memid, final WTypes.BSTRByReference pBstrName, final WTypes.BSTRByReference pBstrDocString, final WinDef.DWORDByReference pdwHelpContext, final WTypes.BSTRByReference pBstrHelpFile) {
        return (WinNT.HRESULT)this._invokeNativeObject(12, new Object[] { this.getPointer(), memid, pBstrName, pBstrDocString, pdwHelpContext, pBstrHelpFile }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetDllEntry(final OaIdl.MEMBERID memid, final OaIdl.INVOKEKIND invKind, final WTypes.BSTRByReference pBstrDllName, final WTypes.BSTRByReference pBstrName, final WinDef.WORDByReference pwOrdinal) {
        return (WinNT.HRESULT)this._invokeNativeObject(13, new Object[] { this.getPointer(), memid, invKind, pBstrDllName, pBstrName, pwOrdinal }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetRefTypeInfo(final OaIdl.HREFTYPE hRefType, final PointerByReference ppTInfo) {
        return (WinNT.HRESULT)this._invokeNativeObject(14, new Object[] { this.getPointer(), hRefType, ppTInfo }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT AddressOfMember(final OaIdl.MEMBERID memid, final OaIdl.INVOKEKIND invKind, final PointerByReference ppv) {
        return (WinNT.HRESULT)this._invokeNativeObject(15, new Object[] { this.getPointer(), memid, invKind, ppv }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT CreateInstance(final IUnknown pUnkOuter, final Guid.REFIID riid, final PointerByReference ppvObj) {
        return (WinNT.HRESULT)this._invokeNativeObject(16, new Object[] { this.getPointer(), pUnkOuter, riid, ppvObj }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetMops(final OaIdl.MEMBERID memid, final WTypes.BSTRByReference pBstrMops) {
        return (WinNT.HRESULT)this._invokeNativeObject(17, new Object[] { this.getPointer(), memid, pBstrMops }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetContainingTypeLib(final PointerByReference ppTLib, final WinDef.UINTByReference pIndex) {
        return (WinNT.HRESULT)this._invokeNativeObject(18, new Object[] { this.getPointer(), ppTLib, pIndex }, WinNT.HRESULT.class);
    }
    
    @Override
    public void ReleaseTypeAttr(final OaIdl.TYPEATTR pTypeAttr) {
        this._invokeNativeVoid(19, new Object[] { this.getPointer(), pTypeAttr });
    }
    
    @Override
    public void ReleaseFuncDesc(final OaIdl.FUNCDESC pFuncDesc) {
        this._invokeNativeVoid(20, new Object[] { this.getPointer(), pFuncDesc });
    }
    
    @Override
    public void ReleaseVarDesc(final OaIdl.VARDESC pVarDesc) {
        this._invokeNativeVoid(21, new Object[] { this.getPointer(), pVarDesc });
    }
    
    public static class ByReference extends TypeInfo implements Structure.ByReference
    {
    }
}
