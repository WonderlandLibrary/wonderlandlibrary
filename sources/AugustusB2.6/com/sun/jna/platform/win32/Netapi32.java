// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.win32.W32APITypeMapper;
import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallLibrary;

public interface Netapi32 extends StdCallLibrary
{
    public static final Netapi32 INSTANCE = Native.load("Netapi32", Netapi32.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int MAX_PREFERRED_LENGTH = -1;
    
    int NetSessionEnum(final WString p0, final WString p1, final WString p2, final int p3, final PointerByReference p4, final int p5, final IntByReference p6, final IntByReference p7, final IntByReference p8);
    
    int NetGetJoinInformation(final String p0, final PointerByReference p1, final IntByReference p2);
    
    int NetApiBufferFree(final Pointer p0);
    
    int NetLocalGroupEnum(final String p0, final int p1, final PointerByReference p2, final int p3, final IntByReference p4, final IntByReference p5, final IntByReference p6);
    
    int NetGetDCName(final String p0, final String p1, final PointerByReference p2);
    
    int NetGroupEnum(final String p0, final int p1, final PointerByReference p2, final int p3, final IntByReference p4, final IntByReference p5, final IntByReference p6);
    
    int NetUserEnum(final String p0, final int p1, final int p2, final PointerByReference p3, final int p4, final IntByReference p5, final IntByReference p6, final IntByReference p7);
    
    int NetUserGetGroups(final String p0, final String p1, final int p2, final PointerByReference p3, final int p4, final IntByReference p5, final IntByReference p6);
    
    int NetUserGetLocalGroups(final String p0, final String p1, final int p2, final int p3, final PointerByReference p4, final int p5, final IntByReference p6, final IntByReference p7);
    
    int NetUserAdd(final String p0, final int p1, final Structure p2, final IntByReference p3);
    
    int NetUserDel(final String p0, final String p1);
    
    int NetUserChangePassword(final String p0, final String p1, final String p2, final String p3);
    
    int DsGetDcName(final String p0, final String p1, final Guid.GUID p2, final String p3, final int p4, final DsGetDC.PDOMAIN_CONTROLLER_INFO p5);
    
    int DsGetForestTrustInformation(final String p0, final String p1, final int p2, final NTSecApi.PLSA_FOREST_TRUST_INFORMATION p3);
    
    int DsEnumerateDomainTrusts(final String p0, final int p1, final PointerByReference p2, final IntByReference p3);
    
    int NetUserGetInfo(final String p0, final String p1, final int p2, final PointerByReference p3);
    
    int NetShareAdd(final String p0, final int p1, final Pointer p2, final IntByReference p3);
    
    int NetShareDel(final String p0, final String p1, final int p2);
    
    @FieldOrder({ "sesi10_cname", "sesi10_username", "sesi10_time", "sesi10_idle_time" })
    public static class SESSION_INFO_10 extends Structure
    {
        public String sesi10_cname;
        public String sesi10_username;
        public int sesi10_time;
        public int sesi10_idle_time;
        
        public SESSION_INFO_10() {
            super(W32APITypeMapper.UNICODE);
        }
        
        public SESSION_INFO_10(final Pointer p) {
            super(p, 0, W32APITypeMapper.UNICODE);
            this.read();
        }
    }
}
