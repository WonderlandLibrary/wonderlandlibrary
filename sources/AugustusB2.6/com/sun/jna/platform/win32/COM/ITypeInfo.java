// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public interface ITypeInfo extends IUnknown
{
    WinNT.HRESULT GetTypeAttr(final PointerByReference p0);
    
    WinNT.HRESULT GetTypeComp(final PointerByReference p0);
    
    WinNT.HRESULT GetFuncDesc(final WinDef.UINT p0, final PointerByReference p1);
    
    WinNT.HRESULT GetVarDesc(final WinDef.UINT p0, final PointerByReference p1);
    
    WinNT.HRESULT GetNames(final OaIdl.MEMBERID p0, final WTypes.BSTR[] p1, final WinDef.UINT p2, final WinDef.UINTByReference p3);
    
    WinNT.HRESULT GetRefTypeOfImplType(final WinDef.UINT p0, final OaIdl.HREFTYPEByReference p1);
    
    WinNT.HRESULT GetImplTypeFlags(final WinDef.UINT p0, final IntByReference p1);
    
    WinNT.HRESULT GetIDsOfNames(final WTypes.LPOLESTR[] p0, final WinDef.UINT p1, final OaIdl.MEMBERID[] p2);
    
    WinNT.HRESULT Invoke(final WinDef.PVOID p0, final OaIdl.MEMBERID p1, final WinDef.WORD p2, final OleAuto.DISPPARAMS.ByReference p3, final Variant.VARIANT.ByReference p4, final OaIdl.EXCEPINFO.ByReference p5, final WinDef.UINTByReference p6);
    
    WinNT.HRESULT GetDocumentation(final OaIdl.MEMBERID p0, final WTypes.BSTRByReference p1, final WTypes.BSTRByReference p2, final WinDef.DWORDByReference p3, final WTypes.BSTRByReference p4);
    
    WinNT.HRESULT GetDllEntry(final OaIdl.MEMBERID p0, final OaIdl.INVOKEKIND p1, final WTypes.BSTRByReference p2, final WTypes.BSTRByReference p3, final WinDef.WORDByReference p4);
    
    WinNT.HRESULT GetRefTypeInfo(final OaIdl.HREFTYPE p0, final PointerByReference p1);
    
    WinNT.HRESULT AddressOfMember(final OaIdl.MEMBERID p0, final OaIdl.INVOKEKIND p1, final PointerByReference p2);
    
    WinNT.HRESULT CreateInstance(final IUnknown p0, final Guid.REFIID p1, final PointerByReference p2);
    
    WinNT.HRESULT GetMops(final OaIdl.MEMBERID p0, final WTypes.BSTRByReference p1);
    
    WinNT.HRESULT GetContainingTypeLib(final PointerByReference p0, final WinDef.UINTByReference p1);
    
    void ReleaseTypeAttr(final OaIdl.TYPEATTR p0);
    
    void ReleaseFuncDesc(final OaIdl.FUNCDESC p0);
    
    void ReleaseVarDesc(final OaIdl.VARDESC p0);
}
