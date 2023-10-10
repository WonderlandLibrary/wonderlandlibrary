// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Secur32 extends StdCallLibrary
{
    public static final Secur32 INSTANCE = Native.load("Secur32", Secur32.class, W32APIOptions.DEFAULT_OPTIONS);
    
    boolean GetUserNameEx(final int p0, final char[] p1, final IntByReference p2);
    
    int AcquireCredentialsHandle(final String p0, final String p1, final int p2, final WinNT.LUID p3, final Pointer p4, final Pointer p5, final Pointer p6, final Sspi.CredHandle p7, final Sspi.TimeStamp p8);
    
    int InitializeSecurityContext(final Sspi.CredHandle p0, final Sspi.CtxtHandle p1, final String p2, final int p3, final int p4, final int p5, final Sspi.SecBufferDesc p6, final int p7, final Sspi.CtxtHandle p8, final Sspi.SecBufferDesc p9, final IntByReference p10, final Sspi.TimeStamp p11);
    
    int DeleteSecurityContext(final Sspi.CtxtHandle p0);
    
    int FreeCredentialsHandle(final Sspi.CredHandle p0);
    
    int AcceptSecurityContext(final Sspi.CredHandle p0, final Sspi.CtxtHandle p1, final Sspi.SecBufferDesc p2, final int p3, final int p4, final Sspi.CtxtHandle p5, final Sspi.SecBufferDesc p6, final IntByReference p7, final Sspi.TimeStamp p8);
    
    int CompleteAuthToken(final Sspi.CtxtHandle p0, final Sspi.SecBufferDesc p1);
    
    int EnumerateSecurityPackages(final IntByReference p0, final Sspi.PSecPkgInfo p1);
    
    int FreeContextBuffer(final Pointer p0);
    
    int QuerySecurityContextToken(final Sspi.CtxtHandle p0, final WinNT.HANDLEByReference p1);
    
    int ImpersonateSecurityContext(final Sspi.CtxtHandle p0);
    
    int RevertSecurityContext(final Sspi.CtxtHandle p0);
    
    int QueryContextAttributes(final Sspi.CtxtHandle p0, final int p1, final Structure p2);
    
    int QueryCredentialsAttributes(final Sspi.CredHandle p0, final int p1, final Structure p2);
    
    int QuerySecurityPackageInfo(final String p0, final Sspi.PSecPkgInfo p1);
    
    int EncryptMessage(final Sspi.CtxtHandle p0, final int p1, final Sspi.SecBufferDesc p2, final int p3);
    
    int VerifySignature(final Sspi.CtxtHandle p0, final Sspi.SecBufferDesc p1, final int p2, final IntByReference p3);
    
    int MakeSignature(final Sspi.CtxtHandle p0, final int p1, final Sspi.SecBufferDesc p2, final int p3);
    
    int DecryptMessage(final Sspi.CtxtHandle p0, final Sspi.SecBufferDesc p1, final int p2, final IntByReference p3);
    
    public abstract static class EXTENDED_NAME_FORMAT
    {
        public static final int NameUnknown = 0;
        public static final int NameFullyQualifiedDN = 1;
        public static final int NameSamCompatible = 2;
        public static final int NameDisplay = 3;
        public static final int NameUniqueId = 6;
        public static final int NameCanonical = 7;
        public static final int NameUserPrincipal = 8;
        public static final int NameCanonicalEx = 9;
        public static final int NameServicePrincipal = 10;
        public static final int NameDnsDomain = 12;
    }
}
