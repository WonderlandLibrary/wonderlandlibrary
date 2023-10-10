// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Crypt32 extends StdCallLibrary
{
    public static final Crypt32 INSTANCE = Native.load("Crypt32", Crypt32.class, W32APIOptions.DEFAULT_OPTIONS);
    
    boolean CryptProtectData(final WinCrypt.DATA_BLOB p0, final String p1, final WinCrypt.DATA_BLOB p2, final Pointer p3, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT p4, final int p5, final WinCrypt.DATA_BLOB p6);
    
    boolean CryptUnprotectData(final WinCrypt.DATA_BLOB p0, final PointerByReference p1, final WinCrypt.DATA_BLOB p2, final Pointer p3, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT p4, final int p5, final WinCrypt.DATA_BLOB p6);
    
    boolean CertAddEncodedCertificateToSystemStore(final String p0, final Pointer p1, final int p2);
    
    WinCrypt.HCERTSTORE CertOpenSystemStore(final Pointer p0, final String p1);
    
    boolean CryptSignMessage(final WinCrypt.CRYPT_SIGN_MESSAGE_PARA p0, final boolean p1, final int p2, final Pointer[] p3, final int[] p4, final Pointer p5, final IntByReference p6);
    
    boolean CryptVerifyMessageSignature(final WinCrypt.CRYPT_VERIFY_MESSAGE_PARA p0, final int p1, final Pointer p2, final int p3, final Pointer p4, final IntByReference p5, final PointerByReference p6);
    
    boolean CertGetCertificateChain(final WinCrypt.HCERTCHAINENGINE p0, final WinCrypt.CERT_CONTEXT p1, final WinBase.FILETIME p2, final WinCrypt.HCERTSTORE p3, final WinCrypt.CERT_CHAIN_PARA p4, final int p5, final Pointer p6, final PointerByReference p7);
    
    boolean CertFreeCertificateContext(final WinCrypt.CERT_CONTEXT p0);
    
    void CertFreeCertificateChain(final WinCrypt.CERT_CHAIN_CONTEXT p0);
    
    boolean CertCloseStore(final WinCrypt.HCERTSTORE p0, final int p1);
    
    int CertNameToStr(final int p0, final WinCrypt.DATA_BLOB p1, final int p2, final Pointer p3, final int p4);
    
    boolean CertVerifyCertificateChainPolicy(final WTypes.LPSTR p0, final WinCrypt.CERT_CHAIN_CONTEXT p1, final WinCrypt.CERT_CHAIN_POLICY_PARA p2, final WinCrypt.CERT_CHAIN_POLICY_STATUS p3);
    
    WinCrypt.CERT_CONTEXT.ByReference CertFindCertificateInStore(final WinCrypt.HCERTSTORE p0, final int p1, final int p2, final int p3, final Pointer p4, final WinCrypt.CERT_CONTEXT p5);
    
    WinCrypt.HCERTSTORE PFXImportCertStore(final WinCrypt.DATA_BLOB p0, final WTypes.LPWSTR p1, final int p2);
    
    WinCrypt.CERT_CONTEXT.ByReference CertEnumCertificatesInStore(final WinCrypt.HCERTSTORE p0, final Pointer p1);
    
    WinCrypt.CTL_CONTEXT.ByReference CertEnumCTLsInStore(final WinCrypt.HCERTSTORE p0, final Pointer p1);
    
    WinCrypt.CRL_CONTEXT.ByReference CertEnumCRLsInStore(final WinCrypt.HCERTSTORE p0, final Pointer p1);
    
    boolean CryptQueryObject(final int p0, final Pointer p1, final int p2, final int p3, final int p4, final IntByReference p5, final IntByReference p6, final IntByReference p7, final PointerByReference p8, final PointerByReference p9, final PointerByReference p10);
}
