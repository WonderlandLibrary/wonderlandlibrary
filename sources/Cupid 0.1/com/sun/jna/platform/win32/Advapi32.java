package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Advapi32 extends StdCallLibrary {
  public static final Advapi32 INSTANCE = (Advapi32)Native.loadLibrary("Advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);
  
  boolean GetUserNameW(char[] paramArrayOfchar, IntByReference paramIntByReference);
  
  boolean LookupAccountName(String paramString1, String paramString2, WinNT.PSID paramPSID, IntByReference paramIntByReference1, char[] paramArrayOfchar, IntByReference paramIntByReference2, PointerByReference paramPointerByReference);
  
  boolean LookupAccountSid(String paramString, WinNT.PSID paramPSID, char[] paramArrayOfchar1, IntByReference paramIntByReference1, char[] paramArrayOfchar2, IntByReference paramIntByReference2, PointerByReference paramPointerByReference);
  
  boolean ConvertSidToStringSid(WinNT.PSID paramPSID, PointerByReference paramPointerByReference);
  
  boolean ConvertStringSidToSid(String paramString, WinNT.PSIDByReference paramPSIDByReference);
  
  int GetLengthSid(WinNT.PSID paramPSID);
  
  boolean IsValidSid(WinNT.PSID paramPSID);
  
  boolean IsWellKnownSid(WinNT.PSID paramPSID, int paramInt);
  
  boolean CreateWellKnownSid(int paramInt, WinNT.PSID paramPSID1, WinNT.PSID paramPSID2, IntByReference paramIntByReference);
  
  boolean LogonUser(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, WinNT.HANDLEByReference paramHANDLEByReference);
  
  boolean OpenThreadToken(WinNT.HANDLE paramHANDLE, int paramInt, boolean paramBoolean, WinNT.HANDLEByReference paramHANDLEByReference);
  
  boolean OpenProcessToken(WinNT.HANDLE paramHANDLE, int paramInt, WinNT.HANDLEByReference paramHANDLEByReference);
  
  boolean DuplicateToken(WinNT.HANDLE paramHANDLE, int paramInt, WinNT.HANDLEByReference paramHANDLEByReference);
  
  boolean DuplicateTokenEx(WinNT.HANDLE paramHANDLE, int paramInt1, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, int paramInt2, int paramInt3, WinNT.HANDLEByReference paramHANDLEByReference);
  
  boolean GetTokenInformation(WinNT.HANDLE paramHANDLE, int paramInt1, Structure paramStructure, int paramInt2, IntByReference paramIntByReference);
  
  boolean ImpersonateLoggedOnUser(WinNT.HANDLE paramHANDLE);
  
  boolean ImpersonateSelf(int paramInt);
  
  boolean RevertToSelf();
  
  int RegOpenKeyEx(WinReg.HKEY paramHKEY, String paramString, int paramInt1, int paramInt2, WinReg.HKEYByReference paramHKEYByReference);
  
  int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, char[] paramArrayOfchar, IntByReference paramIntByReference2);
  
  int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, byte[] paramArrayOfbyte, IntByReference paramIntByReference2);
  
  int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);
  
  int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, LongByReference paramLongByReference, IntByReference paramIntByReference2);
  
  int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, Pointer paramPointer, IntByReference paramIntByReference2);
  
  int RegCloseKey(WinReg.HKEY paramHKEY);
  
  int RegDeleteValue(WinReg.HKEY paramHKEY, String paramString);
  
  int RegSetValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt1, int paramInt2, char[] paramArrayOfchar, int paramInt3);
  
  int RegSetValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3);
  
  int RegCreateKeyEx(WinReg.HKEY paramHKEY, String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, WinReg.HKEYByReference paramHKEYByReference, IntByReference paramIntByReference);
  
  int RegDeleteKey(WinReg.HKEY paramHKEY, String paramString);
  
  int RegEnumKeyEx(WinReg.HKEY paramHKEY, int paramInt, char[] paramArrayOfchar1, IntByReference paramIntByReference1, IntByReference paramIntByReference2, char[] paramArrayOfchar2, IntByReference paramIntByReference3, WinBase.FILETIME paramFILETIME);
  
  int RegEnumValue(WinReg.HKEY paramHKEY, int paramInt, char[] paramArrayOfchar, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3, byte[] paramArrayOfbyte, IntByReference paramIntByReference4);
  
  int RegQueryInfoKey(WinReg.HKEY paramHKEY, char[] paramArrayOfchar, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3, IntByReference paramIntByReference4, IntByReference paramIntByReference5, IntByReference paramIntByReference6, IntByReference paramIntByReference7, IntByReference paramIntByReference8, IntByReference paramIntByReference9, WinBase.FILETIME paramFILETIME);
  
  WinNT.HANDLE RegisterEventSource(String paramString1, String paramString2);
  
  boolean DeregisterEventSource(WinNT.HANDLE paramHANDLE);
  
  WinNT.HANDLE OpenEventLog(String paramString1, String paramString2);
  
  boolean CloseEventLog(WinNT.HANDLE paramHANDLE);
  
  boolean GetNumberOfEventLogRecords(WinNT.HANDLE paramHANDLE, IntByReference paramIntByReference);
  
  boolean ReportEvent(WinNT.HANDLE paramHANDLE, int paramInt1, int paramInt2, int paramInt3, WinNT.PSID paramPSID, int paramInt4, int paramInt5, String[] paramArrayOfString, Pointer paramPointer);
  
  boolean ClearEventLog(WinNT.HANDLE paramHANDLE, String paramString);
  
  boolean BackupEventLog(WinNT.HANDLE paramHANDLE, String paramString);
  
  WinNT.HANDLE OpenBackupEventLog(String paramString1, String paramString2);
  
  boolean ReadEventLog(WinNT.HANDLE paramHANDLE, int paramInt1, int paramInt2, Pointer paramPointer, int paramInt3, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  boolean GetOldestEventLogRecord(WinNT.HANDLE paramHANDLE, IntByReference paramIntByReference);
  
  boolean QueryServiceStatusEx(Winsvc.SC_HANDLE paramSC_HANDLE, int paramInt1, Winsvc.SERVICE_STATUS_PROCESS paramSERVICE_STATUS_PROCESS, int paramInt2, IntByReference paramIntByReference);
  
  boolean ControlService(Winsvc.SC_HANDLE paramSC_HANDLE, int paramInt, Winsvc.SERVICE_STATUS paramSERVICE_STATUS);
  
  boolean StartService(Winsvc.SC_HANDLE paramSC_HANDLE, int paramInt, String[] paramArrayOfString);
  
  boolean CloseServiceHandle(Winsvc.SC_HANDLE paramSC_HANDLE);
  
  Winsvc.SC_HANDLE OpenService(Winsvc.SC_HANDLE paramSC_HANDLE, String paramString, int paramInt);
  
  Winsvc.SC_HANDLE OpenSCManager(String paramString1, String paramString2, int paramInt);
  
  boolean CreateProcessAsUser(WinNT.HANDLE paramHANDLE, String paramString1, String paramString2, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES1, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES2, boolean paramBoolean, int paramInt, String paramString3, String paramString4, WinBase.STARTUPINFO paramSTARTUPINFO, WinBase.PROCESS_INFORMATION paramPROCESS_INFORMATION);
  
  boolean AdjustTokenPrivileges(WinNT.HANDLE paramHANDLE, boolean paramBoolean, WinNT.TOKEN_PRIVILEGES paramTOKEN_PRIVILEGES1, int paramInt, WinNT.TOKEN_PRIVILEGES paramTOKEN_PRIVILEGES2, IntByReference paramIntByReference);
  
  boolean LookupPrivilegeName(String paramString, WinNT.LUID paramLUID, char[] paramArrayOfchar, IntByReference paramIntByReference);
  
  boolean LookupPrivilegeValue(String paramString1, String paramString2, WinNT.LUID paramLUID);
  
  boolean GetFileSecurity(WString paramWString, int paramInt1, Pointer paramPointer, int paramInt2, IntByReference paramIntByReference);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Advapi32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */