// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.Pointer;

public class TypeLib extends Unknown implements ITypeLib
{
    public TypeLib() {
    }
    
    public TypeLib(final Pointer pvInstance) {
        super(pvInstance);
    }
    
    @Override
    public WinDef.UINT GetTypeInfoCount() {
        return (WinDef.UINT)this._invokeNativeObject(3, new Object[] { this.getPointer() }, WinDef.UINT.class);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfo(final WinDef.UINT index, final PointerByReference pTInfo) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), index, pTInfo }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfoType(final WinDef.UINT index, final OaIdl.TYPEKIND.ByReference pTKind) {
        return (WinNT.HRESULT)this._invokeNativeObject(5, new Object[] { this.getPointer(), index, pTKind }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfoOfGuid(final Guid.GUID guid, final PointerByReference pTinfo) {
        return (WinNT.HRESULT)this._invokeNativeObject(6, new Object[] { this.getPointer(), guid, pTinfo }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetLibAttr(final PointerByReference ppTLibAttr) {
        return (WinNT.HRESULT)this._invokeNativeObject(7, new Object[] { this.getPointer(), ppTLibAttr }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetTypeComp(final PointerByReference pTComp) {
        return (WinNT.HRESULT)this._invokeNativeObject(8, new Object[] { this.getPointer(), pTComp }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT GetDocumentation(final int index, final WTypes.BSTRByReference pBstrName, final WTypes.BSTRByReference pBstrDocString, final WinDef.DWORDByReference pdwHelpContext, final WTypes.BSTRByReference pBstrHelpFile) {
        return (WinNT.HRESULT)this._invokeNativeObject(9, new Object[] { this.getPointer(), index, pBstrName, pBstrDocString, pdwHelpContext, pBstrHelpFile }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT IsName(final WTypes.LPOLESTR szNameBuf, final WinDef.ULONG lHashVal, final WinDef.BOOLByReference pfName) {
        return (WinNT.HRESULT)this._invokeNativeObject(10, new Object[] { this.getPointer(), szNameBuf, lHashVal, pfName }, WinNT.HRESULT.class);
    }
    
    @Override
    public WinNT.HRESULT FindName(final WTypes.LPOLESTR szNameBuf, final WinDef.ULONG lHashVal, final Pointer[] ppTInfo, final OaIdl.MEMBERID[] rgMemId, final WinDef.USHORTByReference pcFound) {
        return (WinNT.HRESULT)this._invokeNativeObject(11, new Object[] { this.getPointer(), szNameBuf, lHashVal, ppTInfo, rgMemId, pcFound }, WinNT.HRESULT.class);
    }
    
    @Override
    public void ReleaseTLibAttr(final OaIdl.TLIBATTR pTLibAttr) {
        this._invokeNativeObject(12, new Object[] { this.getPointer(), pTLibAttr.getPointer() }, WinNT.HRESULT.class);
    }
    
    public static class ByReference extends TypeLib implements Structure.ByReference
    {
    }
}
