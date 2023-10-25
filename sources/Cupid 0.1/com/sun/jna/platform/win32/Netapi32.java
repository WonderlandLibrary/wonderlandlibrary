package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Netapi32 extends StdCallLibrary {
  public static final Netapi32 INSTANCE = (Netapi32)Native.loadLibrary("Netapi32", Netapi32.class, W32APIOptions.UNICODE_OPTIONS);
  
  int NetGetJoinInformation(String paramString, PointerByReference paramPointerByReference, IntByReference paramIntByReference);
  
  int NetApiBufferFree(Pointer paramPointer);
  
  int NetLocalGroupEnum(String paramString, int paramInt1, PointerByReference paramPointerByReference, int paramInt2, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);
  
  int NetGetDCName(String paramString1, String paramString2, PointerByReference paramPointerByReference);
  
  int NetGroupEnum(String paramString, int paramInt1, PointerByReference paramPointerByReference, int paramInt2, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);
  
  int NetUserEnum(String paramString, int paramInt1, int paramInt2, PointerByReference paramPointerByReference, int paramInt3, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);
  
  int NetUserGetGroups(String paramString1, String paramString2, int paramInt1, PointerByReference paramPointerByReference, int paramInt2, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  int NetUserGetLocalGroups(String paramString1, String paramString2, int paramInt1, int paramInt2, PointerByReference paramPointerByReference, int paramInt3, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  int NetUserAdd(String paramString, int paramInt, Structure paramStructure, IntByReference paramIntByReference);
  
  int NetUserDel(String paramString1, String paramString2);
  
  int NetUserChangePassword(String paramString1, String paramString2, String paramString3, String paramString4);
  
  int DsGetDcName(String paramString1, String paramString2, Guid.GUID paramGUID, String paramString3, int paramInt, DsGetDC.PDOMAIN_CONTROLLER_INFO.ByReference paramByReference);
  
  int DsGetForestTrustInformation(String paramString1, String paramString2, int paramInt, NTSecApi.PLSA_FOREST_TRUST_INFORMATION.ByReference paramByReference);
  
  int DsEnumerateDomainTrusts(String paramString, NativeLong paramNativeLong, DsGetDC.PDS_DOMAIN_TRUSTS.ByReference paramByReference, NativeLongByReference paramNativeLongByReference);
  
  int NetUserGetInfo(String paramString1, String paramString2, int paramInt, PointerByReference paramPointerByReference);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Netapi32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */