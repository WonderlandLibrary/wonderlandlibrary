// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.WString;
import com.sun.jna.Pointer;

public class TypeComp extends Unknown
{
    public TypeComp() {
    }
    
    public TypeComp(final Pointer pvInstance) {
        super(pvInstance);
    }
    
    public WinNT.HRESULT Bind(final WString szName, final WinDef.ULONG lHashVal, final WinDef.WORD wFlags, final PointerByReference ppTInfo, final OaIdl.DESCKIND.ByReference pDescKind, final OaIdl.BINDPTR.ByReference pBindPtr) {
        return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[] { this.getPointer(), szName, lHashVal, wFlags, ppTInfo, pDescKind, pBindPtr }, WinNT.HRESULT.class);
    }
    
    public WinNT.HRESULT BindType(final WString szName, final WinDef.ULONG lHashVal, final PointerByReference ppTInfo, final PointerByReference ppTComp) {
        return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), szName, lHashVal, ppTInfo, ppTComp }, WinNT.HRESULT.class);
    }
    
    public static class ByReference extends TypeComp implements Structure.ByReference
    {
    }
}
