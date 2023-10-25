/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Advapi32
extends StdCallLibrary {
    public static final Advapi32 INSTANCE = Native.loadLibrary("Advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);

    public boolean GetUserNameW(char[] var1, IntByReference var2);

    public boolean LookupAccountName(String var1, String var2, WinNT.PSID var3, IntByReference var4, char[] var5, IntByReference var6, PointerByReference var7);

    public boolean LookupAccountSid(String var1, WinNT.PSID var2, char[] var3, IntByReference var4, char[] var5, IntByReference var6, PointerByReference var7);

    public boolean ConvertSidToStringSid(WinNT.PSID var1, PointerByReference var2);

    public boolean ConvertStringSidToSid(String var1, WinNT.PSIDByReference var2);

    public int GetLengthSid(WinNT.PSID var1);

    public boolean IsValidSid(WinNT.PSID var1);

    public boolean IsWellKnownSid(WinNT.PSID var1, int var2);

    public boolean CreateWellKnownSid(int var1, WinNT.PSID var2, WinNT.PSID var3, IntByReference var4);

    public boolean LogonUser(String var1, String var2, String var3, int var4, int var5, WinNT.HANDLEByReference var6);

    public boolean OpenThreadToken(WinNT.HANDLE var1, int var2, boolean var3, WinNT.HANDLEByReference var4);

    public boolean OpenProcessToken(WinNT.HANDLE var1, int var2, WinNT.HANDLEByReference var3);

    public boolean DuplicateToken(WinNT.HANDLE var1, int var2, WinNT.HANDLEByReference var3);

    public boolean DuplicateTokenEx(WinNT.HANDLE var1, int var2, WinBase.SECURITY_ATTRIBUTES var3, int var4, int var5, WinNT.HANDLEByReference var6);

    public boolean GetTokenInformation(WinNT.HANDLE var1, int var2, Structure var3, int var4, IntByReference var5);

    public boolean ImpersonateLoggedOnUser(WinNT.HANDLE var1);

    public boolean ImpersonateSelf(int var1);

    public boolean RevertToSelf();

    public int RegOpenKeyEx(WinReg.HKEY var1, String var2, int var3, int var4, WinReg.HKEYByReference var5);

    public int RegQueryValueEx(WinReg.HKEY var1, String var2, int var3, IntByReference var4, char[] var5, IntByReference var6);

    public int RegQueryValueEx(WinReg.HKEY var1, String var2, int var3, IntByReference var4, byte[] var5, IntByReference var6);

    public int RegQueryValueEx(WinReg.HKEY var1, String var2, int var3, IntByReference var4, IntByReference var5, IntByReference var6);

    public int RegQueryValueEx(WinReg.HKEY var1, String var2, int var3, IntByReference var4, LongByReference var5, IntByReference var6);

    public int RegQueryValueEx(WinReg.HKEY var1, String var2, int var3, IntByReference var4, Pointer var5, IntByReference var6);

    public int RegCloseKey(WinReg.HKEY var1);

    public int RegDeleteValue(WinReg.HKEY var1, String var2);

    public int RegSetValueEx(WinReg.HKEY var1, String var2, int var3, int var4, char[] var5, int var6);

    public int RegSetValueEx(WinReg.HKEY var1, String var2, int var3, int var4, byte[] var5, int var6);

    public int RegCreateKeyEx(WinReg.HKEY var1, String var2, int var3, String var4, int var5, int var6, WinBase.SECURITY_ATTRIBUTES var7, WinReg.HKEYByReference var8, IntByReference var9);

    public int RegDeleteKey(WinReg.HKEY var1, String var2);

    public int RegEnumKeyEx(WinReg.HKEY var1, int var2, char[] var3, IntByReference var4, IntByReference var5, char[] var6, IntByReference var7, WinBase.FILETIME var8);

    public int RegEnumValue(WinReg.HKEY var1, int var2, char[] var3, IntByReference var4, IntByReference var5, IntByReference var6, byte[] var7, IntByReference var8);

    public int RegQueryInfoKey(WinReg.HKEY var1, char[] var2, IntByReference var3, IntByReference var4, IntByReference var5, IntByReference var6, IntByReference var7, IntByReference var8, IntByReference var9, IntByReference var10, IntByReference var11, WinBase.FILETIME var12);

    public WinNT.HANDLE RegisterEventSource(String var1, String var2);

    public boolean DeregisterEventSource(WinNT.HANDLE var1);

    public WinNT.HANDLE OpenEventLog(String var1, String var2);

    public boolean CloseEventLog(WinNT.HANDLE var1);

    public boolean GetNumberOfEventLogRecords(WinNT.HANDLE var1, IntByReference var2);

    public boolean ReportEvent(WinNT.HANDLE var1, int var2, int var3, int var4, WinNT.PSID var5, int var6, int var7, String[] var8, Pointer var9);

    public boolean ClearEventLog(WinNT.HANDLE var1, String var2);

    public boolean BackupEventLog(WinNT.HANDLE var1, String var2);

    public WinNT.HANDLE OpenBackupEventLog(String var1, String var2);

    public boolean ReadEventLog(WinNT.HANDLE var1, int var2, int var3, Pointer var4, int var5, IntByReference var6, IntByReference var7);

    public boolean GetOldestEventLogRecord(WinNT.HANDLE var1, IntByReference var2);

    public boolean QueryServiceStatusEx(Winsvc.SC_HANDLE var1, int var2, Winsvc.SERVICE_STATUS_PROCESS var3, int var4, IntByReference var5);

    public boolean ControlService(Winsvc.SC_HANDLE var1, int var2, Winsvc.SERVICE_STATUS var3);

    public boolean StartService(Winsvc.SC_HANDLE var1, int var2, String[] var3);

    public boolean CloseServiceHandle(Winsvc.SC_HANDLE var1);

    public Winsvc.SC_HANDLE OpenService(Winsvc.SC_HANDLE var1, String var2, int var3);

    public Winsvc.SC_HANDLE OpenSCManager(String var1, String var2, int var3);

    public boolean CreateProcessAsUser(WinNT.HANDLE var1, String var2, String var3, WinBase.SECURITY_ATTRIBUTES var4, WinBase.SECURITY_ATTRIBUTES var5, boolean var6, int var7, String var8, String var9, WinBase.STARTUPINFO var10, WinBase.PROCESS_INFORMATION var11);

    public boolean AdjustTokenPrivileges(WinNT.HANDLE var1, boolean var2, WinNT.TOKEN_PRIVILEGES var3, int var4, WinNT.TOKEN_PRIVILEGES var5, IntByReference var6);

    public boolean LookupPrivilegeName(String var1, WinNT.LUID var2, char[] var3, IntByReference var4);

    public boolean LookupPrivilegeValue(String var1, String var2, WinNT.LUID var3);

    public boolean GetFileSecurity(WString var1, int var2, Pointer var3, int var4, IntByReference var5);
}

