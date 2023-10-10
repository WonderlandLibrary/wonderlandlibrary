// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.PointerType;
import com.sun.jna.win32.StdCallLibrary;

public interface Cryptui extends StdCallLibrary
{
    public static final Cryptui INSTANCE = Native.load("Cryptui", Cryptui.class, W32APIOptions.UNICODE_OPTIONS);
    
    WinCrypt.CERT_CONTEXT.ByReference CryptUIDlgSelectCertificateFromStore(final WinCrypt.HCERTSTORE p0, final WinDef.HWND p1, final String p2, final String p3, final int p4, final int p5, final PointerType p6);
}
