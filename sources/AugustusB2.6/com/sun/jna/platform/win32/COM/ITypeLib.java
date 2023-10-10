// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef;

public interface ITypeLib extends IUnknown
{
    WinDef.UINT GetTypeInfoCount();
    
    WinNT.HRESULT GetTypeInfo(final WinDef.UINT p0, final PointerByReference p1);
    
    WinNT.HRESULT GetTypeInfoType(final WinDef.UINT p0, final OaIdl.TYPEKIND.ByReference p1);
    
    WinNT.HRESULT GetTypeInfoOfGuid(final Guid.GUID p0, final PointerByReference p1);
    
    WinNT.HRESULT GetLibAttr(final PointerByReference p0);
    
    WinNT.HRESULT GetTypeComp(final PointerByReference p0);
    
    WinNT.HRESULT GetDocumentation(final int p0, final WTypes.BSTRByReference p1, final WTypes.BSTRByReference p2, final WinDef.DWORDByReference p3, final WTypes.BSTRByReference p4);
    
    WinNT.HRESULT IsName(final WTypes.LPOLESTR p0, final WinDef.ULONG p1, final WinDef.BOOLByReference p2);
    
    WinNT.HRESULT FindName(final WTypes.LPOLESTR p0, final WinDef.ULONG p1, final Pointer[] p2, final OaIdl.MEMBERID[] p3, final WinDef.USHORTByReference p4);
    
    void ReleaseTLibAttr(final OaIdl.TLIBATTR p0);
}
