// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.WString;

public interface ITypeComp extends IUnknown
{
    WinNT.HRESULT Bind(final WString p0, final WinDef.ULONG p1, final WinDef.WORD p2, final PointerByReference p3, final OaIdl.DESCKIND.ByReference p4, final OaIdl.BINDPTR.ByReference p5);
    
    WinNT.HRESULT BindType(final WString p0, final WinDef.ULONG p1, final PointerByReference p2, final PointerByReference p3);
}
