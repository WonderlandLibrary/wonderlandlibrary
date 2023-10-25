/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class Advapi32Util {
    public static String getUserName() {
        char[] buffer = new char[128];
        IntByReference len = new IntByReference(buffer.length);
        boolean result = Advapi32.INSTANCE.GetUserNameW(buffer, len);
        if (!result) {
            switch (Kernel32.INSTANCE.GetLastError()) {
                case 122: {
                    buffer = new char[len.getValue()];
                    break;
                }
                default: {
                    throw new Win32Exception(Native.getLastError());
                }
            }
            result = Advapi32.INSTANCE.GetUserNameW(buffer, len);
        }
        if (!result) {
            throw new Win32Exception(Native.getLastError());
        }
        return Native.toString(buffer);
    }

    public static Account getAccountByName(String accountName) {
        return Advapi32Util.getAccountByName(null, accountName);
    }

    public static Account getAccountByName(String systemName, String accountName) {
        char[] referencedDomainName;
        IntByReference pSid = new IntByReference(0);
        IntByReference cchDomainName = new IntByReference(0);
        PointerByReference peUse = new PointerByReference();
        if (Advapi32.INSTANCE.LookupAccountName(systemName, accountName, null, pSid, null, cchDomainName, peUse)) {
            throw new RuntimeException("LookupAccountNameW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        int rc = Kernel32.INSTANCE.GetLastError();
        if (pSid.getValue() == 0 || rc != 122) {
            throw new Win32Exception(rc);
        }
        Memory sidMemory = new Memory(pSid.getValue());
        WinNT.PSID result = new WinNT.PSID(sidMemory);
        if (!Advapi32.INSTANCE.LookupAccountName(systemName, accountName, result, pSid, referencedDomainName = new char[cchDomainName.getValue() + 1], cchDomainName, peUse)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Account account = new Account();
        account.accountType = peUse.getPointer().getInt(0L);
        account.name = accountName;
        String[] accountNamePartsBs = accountName.split("\\\\", 2);
        String[] accountNamePartsAt = accountName.split("@", 2);
        account.name = accountNamePartsBs.length == 2 ? accountNamePartsBs[1] : (accountNamePartsAt.length == 2 ? accountNamePartsAt[0] : accountName);
        if (cchDomainName.getValue() > 0) {
            account.domain = Native.toString(referencedDomainName);
            account.fqn = account.domain + "\\" + account.name;
        } else {
            account.fqn = account.name;
        }
        account.sid = result.getBytes();
        account.sidString = Advapi32Util.convertSidToStringSid(new WinNT.PSID(account.sid));
        return account;
    }

    public static Account getAccountBySid(WinNT.PSID sid) {
        return Advapi32Util.getAccountBySid(null, sid);
    }

    public static Account getAccountBySid(String systemName, WinNT.PSID sid) {
        IntByReference cchName = new IntByReference();
        IntByReference cchDomainName = new IntByReference();
        PointerByReference peUse = new PointerByReference();
        if (Advapi32.INSTANCE.LookupAccountSid(null, sid, null, cchName, null, cchDomainName, peUse)) {
            throw new RuntimeException("LookupAccountSidW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        int rc = Kernel32.INSTANCE.GetLastError();
        if (cchName.getValue() == 0 || rc != 122) {
            throw new Win32Exception(rc);
        }
        char[] domainName = new char[cchDomainName.getValue()];
        char[] name = new char[cchName.getValue()];
        if (!Advapi32.INSTANCE.LookupAccountSid(null, sid, name, cchName, domainName, cchDomainName, peUse)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Account account = new Account();
        account.accountType = peUse.getPointer().getInt(0L);
        account.name = Native.toString(name);
        if (cchDomainName.getValue() > 0) {
            account.domain = Native.toString(domainName);
            account.fqn = account.domain + "\\" + account.name;
        } else {
            account.fqn = account.name;
        }
        account.sid = sid.getBytes();
        account.sidString = Advapi32Util.convertSidToStringSid(sid);
        return account;
    }

    public static String convertSidToStringSid(WinNT.PSID sid) {
        PointerByReference stringSid = new PointerByReference();
        if (!Advapi32.INSTANCE.ConvertSidToStringSid(sid, stringSid)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        String result = stringSid.getValue().getString(0L, true);
        Kernel32.INSTANCE.LocalFree(stringSid.getValue());
        return result;
    }

    public static byte[] convertStringSidToSid(String sidString) {
        WinNT.PSIDByReference pSID = new WinNT.PSIDByReference();
        if (!Advapi32.INSTANCE.ConvertStringSidToSid(sidString, pSID)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return pSID.getValue().getBytes();
    }

    public static boolean isWellKnownSid(String sidString, int wellKnownSidType) {
        WinNT.PSIDByReference pSID = new WinNT.PSIDByReference();
        if (!Advapi32.INSTANCE.ConvertStringSidToSid(sidString, pSID)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Advapi32.INSTANCE.IsWellKnownSid(pSID.getValue(), wellKnownSidType);
    }

    public static boolean isWellKnownSid(byte[] sidBytes, int wellKnownSidType) {
        WinNT.PSID pSID = new WinNT.PSID(sidBytes);
        return Advapi32.INSTANCE.IsWellKnownSid(pSID, wellKnownSidType);
    }

    public static Account getAccountBySid(String sidString) {
        return Advapi32Util.getAccountBySid(null, sidString);
    }

    public static Account getAccountBySid(String systemName, String sidString) {
        return Advapi32Util.getAccountBySid(systemName, new WinNT.PSID(Advapi32Util.convertStringSidToSid(sidString)));
    }

    public static Account[] getTokenGroups(WinNT.HANDLE hToken) {
        IntByReference tokenInformationLength = new IntByReference();
        if (Advapi32.INSTANCE.GetTokenInformation(hToken, 2, null, 0, tokenInformationLength)) {
            throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        int rc = Kernel32.INSTANCE.GetLastError();
        if (rc != 122) {
            throw new Win32Exception(rc);
        }
        WinNT.TOKEN_GROUPS groups = new WinNT.TOKEN_GROUPS(tokenInformationLength.getValue());
        if (!Advapi32.INSTANCE.GetTokenInformation(hToken, 2, groups, tokenInformationLength.getValue(), tokenInformationLength)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        ArrayList<Account> userGroups = new ArrayList<Account>();
        for (WinNT.SID_AND_ATTRIBUTES sidAndAttribute : groups.getGroups()) {
            Account group = null;
            try {
                group = Advapi32Util.getAccountBySid(sidAndAttribute.Sid);
            }
            catch (Exception e) {
                group = new Account();
                group.sid = sidAndAttribute.Sid.getBytes();
                group.name = group.sidString = Advapi32Util.convertSidToStringSid(sidAndAttribute.Sid);
                group.fqn = group.sidString;
                group.accountType = 2;
            }
            userGroups.add(group);
        }
        return userGroups.toArray(new Account[0]);
    }

    public static Account getTokenAccount(WinNT.HANDLE hToken) {
        IntByReference tokenInformationLength = new IntByReference();
        if (Advapi32.INSTANCE.GetTokenInformation(hToken, 1, null, 0, tokenInformationLength)) {
            throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
        }
        int rc = Kernel32.INSTANCE.GetLastError();
        if (rc != 122) {
            throw new Win32Exception(rc);
        }
        WinNT.TOKEN_USER user = new WinNT.TOKEN_USER(tokenInformationLength.getValue());
        if (!Advapi32.INSTANCE.GetTokenInformation(hToken, 1, user, tokenInformationLength.getValue(), tokenInformationLength)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Advapi32Util.getAccountBySid(user.User.Sid);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Account[] getCurrentUserGroups() {
        WinNT.HANDLEByReference phToken = new WinNT.HANDLEByReference();
        try {
            WinNT.HANDLE threadHandle = Kernel32.INSTANCE.GetCurrentThread();
            if (!Advapi32.INSTANCE.OpenThreadToken(threadHandle, 10, true, phToken)) {
                if (1008 != Kernel32.INSTANCE.GetLastError()) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                WinNT.HANDLE processHandle = Kernel32.INSTANCE.GetCurrentProcess();
                if (!Advapi32.INSTANCE.OpenProcessToken(processHandle, 10, phToken)) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
            }
            Account[] arraccount = Advapi32Util.getTokenGroups(phToken.getValue());
            return arraccount;
        }
        finally {
            if (phToken.getValue() != WinBase.INVALID_HANDLE_VALUE && !Kernel32.INSTANCE.CloseHandle(phToken.getValue())) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
    }

    public static boolean registryKeyExists(WinReg.HKEY root, String key) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        switch (rc) {
            case 0: {
                Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
                return true;
            }
            case 2: {
                return false;
            }
        }
        throw new Win32Exception(rc);
    }

    public static boolean registryValueExists(WinReg.HKEY root, String key, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        try {
            switch (rc) {
                case 0: {
                    break;
                }
                case 2: {
                    boolean bl = false;
                    return bl;
                }
                default: {
                    throw new Win32Exception(rc);
                }
            }
            IntByReference lpcbData = new IntByReference();
            IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
            switch (rc) {
                case 0: 
                case 122: {
                    boolean bl = true;
                    return bl;
                }
                case 2: {
                    boolean bl = false;
                    return bl;
                }
            }
            throw new Win32Exception(rc);
        }
        finally {
            if (phkKey.getValue() != WinBase.INVALID_HANDLE_VALUE && (rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue())) != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String registryGetStringValue(WinReg.HKEY root, String key, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            IntByReference lpcbData = new IntByReference();
            IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            if (lpType.getValue() != 1 && lpType.getValue() != 2) {
                throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ or REG_EXPAND_SZ");
            }
            char[] data = new char[lpcbData.getValue()];
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, data, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            String string = Native.toString(data);
            return string;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String registryGetExpandableStringValue(WinReg.HKEY root, String key, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            IntByReference lpcbData = new IntByReference();
            IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            if (lpType.getValue() != 2) {
                throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ");
            }
            char[] data = new char[lpcbData.getValue()];
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, data, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            String string = Native.toString(data);
            return string;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String[] registryGetStringArray(WinReg.HKEY root, String key, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            IntByReference lpcbData = new IntByReference();
            IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            if (lpType.getValue() != 7) {
                throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ");
            }
            Memory data = new Memory(lpcbData.getValue());
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, data, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            ArrayList<String> result = new ArrayList<String>();
            int offset = 0;
            while ((long)offset < data.size()) {
                String s = data.getString((long)offset, true);
                offset += s.length() * Native.WCHAR_SIZE;
                offset += Native.WCHAR_SIZE;
                result.add(s);
            }
            String[] arrstring = result.toArray(new String[0]);
            return arrstring;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] registryGetBinaryValue(WinReg.HKEY root, String key, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            IntByReference lpcbData = new IntByReference();
            IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            if (lpType.getValue() != 3) {
                throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_BINARY");
            }
            byte[] data = new byte[lpcbData.getValue()];
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, data, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            byte[] arrby = data;
            return arrby;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int registryGetIntValue(WinReg.HKEY root, String key, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            IntByReference lpcbData = new IntByReference();
            IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            if (lpType.getValue() != 4) {
                throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_DWORD");
            }
            IntByReference data = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, data, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            int n = data.getValue();
            return n;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static long registryGetLongValue(WinReg.HKEY root, String key, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            IntByReference lpcbData = new IntByReference();
            IntByReference lpType = new IntByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            if (lpType.getValue() != 11) {
                throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_QWORD");
            }
            LongByReference data = new LongByReference();
            rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, data, lpcbData);
            if (rc != 0 && rc != 122) {
                throw new Win32Exception(rc);
            }
            long l = data.getValue();
            return l;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static boolean registryCreateKey(WinReg.HKEY hKey, String keyName) {
        WinReg.HKEYByReference phkResult = new WinReg.HKEYByReference();
        IntByReference lpdwDisposition = new IntByReference();
        int rc = Advapi32.INSTANCE.RegCreateKeyEx(hKey, keyName, 0, null, 0, 131097, null, phkResult, lpdwDisposition);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        rc = Advapi32.INSTANCE.RegCloseKey(phkResult.getValue());
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        return 1 == lpdwDisposition.getValue();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean registryCreateKey(WinReg.HKEY root, String parentPath, String keyName) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, parentPath, 0, 4, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            boolean bl = Advapi32Util.registryCreateKey(phkKey.getValue(), keyName);
            return bl;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registrySetIntValue(WinReg.HKEY hKey, String name, int value) {
        byte[] data = new byte[]{(byte)(value & 0xFF), (byte)(value >> 8 & 0xFF), (byte)(value >> 16 & 0xFF), (byte)(value >> 24 & 0xFF)};
        int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 4, data, 4);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registrySetIntValue(WinReg.HKEY root, String keyPath, String name, int value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registrySetIntValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registrySetLongValue(WinReg.HKEY hKey, String name, long value) {
        byte[] data = new byte[]{(byte)(value & 0xFFL), (byte)(value >> 8 & 0xFFL), (byte)(value >> 16 & 0xFFL), (byte)(value >> 24 & 0xFFL), (byte)(value >> 32 & 0xFFL), (byte)(value >> 40 & 0xFFL), (byte)(value >> 48 & 0xFFL), (byte)(value >> 56 & 0xFFL)};
        int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 11, data, 8);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registrySetLongValue(WinReg.HKEY root, String keyPath, String name, long value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registrySetLongValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registrySetStringValue(WinReg.HKEY hKey, String name, String value) {
        char[] data = Native.toCharArray(value);
        int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 1, data, data.length * Native.WCHAR_SIZE);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registrySetStringValue(WinReg.HKEY root, String keyPath, String name, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registrySetStringValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registrySetExpandableStringValue(WinReg.HKEY hKey, String name, String value) {
        char[] data = Native.toCharArray(value);
        int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 2, data, data.length * Native.WCHAR_SIZE);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registrySetExpandableStringValue(WinReg.HKEY root, String keyPath, String name, String value) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registrySetExpandableStringValue(phkKey.getValue(), name, value);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registrySetStringArray(WinReg.HKEY hKey, String name, String[] arr) {
        int size = 0;
        for (String s : arr) {
            size += s.length() * Native.WCHAR_SIZE;
            size += Native.WCHAR_SIZE;
        }
        int offset = 0;
        Memory data = new Memory(size);
        for (String s : arr) {
            data.setString((long)offset, s, true);
            offset += s.length() * Native.WCHAR_SIZE;
            offset += Native.WCHAR_SIZE;
        }
        int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 7, data.getByteArray(0L, size), size);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registrySetStringArray(WinReg.HKEY root, String keyPath, String name, String[] arr) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registrySetStringArray(phkKey.getValue(), name, arr);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registrySetBinaryValue(WinReg.HKEY hKey, String name, byte[] data) {
        int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 3, data, data.length);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registrySetBinaryValue(WinReg.HKEY root, String keyPath, String name, byte[] data) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registrySetBinaryValue(phkKey.getValue(), name, data);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registryDeleteKey(WinReg.HKEY hKey, String keyName) {
        int rc = Advapi32.INSTANCE.RegDeleteKey(hKey, keyName);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registryDeleteKey(WinReg.HKEY root, String keyPath, String keyName) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registryDeleteKey(phkKey.getValue(), keyName);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registryDeleteValue(WinReg.HKEY hKey, String valueName) {
        int rc = Advapi32.INSTANCE.RegDeleteValue(hKey, valueName);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registryDeleteValue(WinReg.HKEY root, String keyPath, String valueName) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            Advapi32Util.registryDeleteValue(phkKey.getValue(), valueName);
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static String[] registryGetKeys(WinReg.HKEY hKey) {
        IntByReference lpcSubKeys = new IntByReference();
        IntByReference lpcMaxSubKeyLen = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, null, null, null, lpcSubKeys, lpcMaxSubKeyLen, null, null, null, null, null, null);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        ArrayList<String> keys = new ArrayList<String>(lpcSubKeys.getValue());
        char[] name = new char[lpcMaxSubKeyLen.getValue() + 1];
        for (int i = 0; i < lpcSubKeys.getValue(); ++i) {
            IntByReference lpcchValueName = new IntByReference(lpcMaxSubKeyLen.getValue() + 1);
            rc = Advapi32.INSTANCE.RegEnumKeyEx(hKey, i, name, lpcchValueName, null, null, null, null);
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            keys.add(Native.toString(name));
        }
        return keys.toArray(new String[0]);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String[] registryGetKeys(WinReg.HKEY root, String keyPath) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            String[] arrstring = Advapi32Util.registryGetKeys(phkKey.getValue());
            return arrstring;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static TreeMap<String, Object> registryGetValues(WinReg.HKEY hKey) {
        IntByReference lpcValues = new IntByReference();
        IntByReference lpcMaxValueNameLen = new IntByReference();
        IntByReference lpcMaxValueLen = new IntByReference();
        int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, null, null, null, null, null, null, lpcValues, lpcMaxValueNameLen, lpcMaxValueLen, null, null);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        TreeMap<String, Object> keyValues = new TreeMap<String, Object>();
        char[] name = new char[lpcMaxValueNameLen.getValue() + 1];
        byte[] data = new byte[lpcMaxValueLen.getValue()];
        block7: for (int i = 0; i < lpcValues.getValue(); ++i) {
            IntByReference lpcbData;
            IntByReference lpType;
            IntByReference lpcchValueName = new IntByReference(lpcMaxValueNameLen.getValue() + 1);
            rc = Advapi32.INSTANCE.RegEnumValue(hKey, i, name, lpcchValueName, null, lpType = new IntByReference(), data, lpcbData = new IntByReference(lpcMaxValueLen.getValue()));
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            String nameString = Native.toString(name);
            Memory byteData = new Memory(lpcbData.getValue());
            byteData.write(0L, data, 0, lpcbData.getValue());
            switch (lpType.getValue()) {
                case 11: {
                    keyValues.put(nameString, byteData.getLong(0L));
                    continue block7;
                }
                case 4: {
                    keyValues.put(nameString, byteData.getInt(0L));
                    continue block7;
                }
                case 1: 
                case 2: {
                    keyValues.put(nameString, byteData.getString(0L, true));
                    continue block7;
                }
                case 3: {
                    keyValues.put(nameString, byteData.getByteArray(0L, lpcbData.getValue()));
                    continue block7;
                }
                case 7: {
                    Memory stringData = new Memory(lpcbData.getValue());
                    stringData.write(0L, data, 0, lpcbData.getValue());
                    ArrayList<String> result = new ArrayList<String>();
                    int offset = 0;
                    while ((long)offset < stringData.size()) {
                        String s = stringData.getString((long)offset, true);
                        offset += s.length() * Native.WCHAR_SIZE;
                        offset += Native.WCHAR_SIZE;
                        result.add(s);
                    }
                    keyValues.put(nameString, result.toArray(new String[0]));
                    continue block7;
                }
                default: {
                    throw new RuntimeException("Unsupported type: " + lpType.getValue());
                }
            }
        }
        return keyValues;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static TreeMap<String, Object> registryGetValues(WinReg.HKEY root, String keyPath) {
        WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
        int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131097, phkKey);
        if (rc != 0) {
            throw new Win32Exception(rc);
        }
        try {
            TreeMap<String, Object> treeMap = Advapi32Util.registryGetValues(phkKey.getValue());
            return treeMap;
        }
        finally {
            rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static String getEnvironmentBlock(Map<String, String> environment) {
        StringBuffer out = new StringBuffer();
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            if (entry.getValue() == null) continue;
            out.append(entry.getKey() + "=" + entry.getValue() + "\u0000");
        }
        return out.toString() + "\u0000";
    }

    public static WinNT.ACCESS_ACEStructure[] getFileSecurity(String fileName, boolean compact) {
        int infoType = 4;
        int nLength = 1024;
        boolean repeat = false;
        Memory memory = null;
        do {
            int lengthNeeded;
            repeat = false;
            memory = new Memory(nLength);
            IntByReference lpnSize = new IntByReference();
            boolean succeded = Advapi32.INSTANCE.GetFileSecurity(new WString(fileName), infoType, memory, nLength, lpnSize);
            if (!succeded) {
                int lastError = Kernel32.INSTANCE.GetLastError();
                memory.clear();
                if (122 != lastError) {
                    throw new Win32Exception(lastError);
                }
            }
            if (nLength >= (lengthNeeded = lpnSize.getValue())) continue;
            repeat = true;
            nLength = lengthNeeded;
            memory.clear();
        } while (repeat);
        WinNT.SECURITY_DESCRIPTOR_RELATIVE sdr = new WinNT.SECURITY_DESCRIPTOR_RELATIVE(memory);
        memory.clear();
        WinNT.ACL dacl = sdr.getDiscretionaryACL();
        WinNT.ACCESS_ACEStructure[] aceStructures = dacl.getACEStructures();
        if (compact) {
            HashMap<String, WinNT.ACCESS_ACEStructure> aceMap = new HashMap<String, WinNT.ACCESS_ACEStructure>();
            for (WinNT.ACCESS_ACEStructure aceStructure : aceStructures) {
                boolean inherted = (aceStructure.AceFlags & 0x1F) != 0;
                String key = aceStructure.getSidString() + "/" + inherted + "/" + aceStructure.getClass().getName();
                WinNT.ACCESS_ACEStructure aceStructure2 = (WinNT.ACCESS_ACEStructure)aceMap.get(key);
                if (aceStructure2 != null) {
                    int accessMask = aceStructure2.Mask;
                    aceStructure2.Mask = accessMask |= aceStructure.Mask;
                    continue;
                }
                aceMap.put(key, aceStructure);
            }
            return aceMap.values().toArray(new WinNT.ACCESS_ACEStructure[aceMap.size()]);
        }
        return aceStructures;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class EventLogIterator
    implements Iterable<EventLogRecord>,
    Iterator<EventLogRecord> {
        private WinNT.HANDLE _h = null;
        private Memory _buffer = new Memory(65536L);
        private boolean _done = false;
        private int _dwRead = 0;
        private Pointer _pevlr = null;
        private int _flags = 4;

        public EventLogIterator(String sourceName) {
            this(null, sourceName, 4);
        }

        public EventLogIterator(String serverName, String sourceName, int flags) {
            this._flags = flags;
            this._h = Advapi32.INSTANCE.OpenEventLog(serverName, sourceName);
            if (this._h == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }

        private boolean read() {
            if (this._done || this._dwRead > 0) {
                return false;
            }
            IntByReference pnBytesRead = new IntByReference();
            IntByReference pnMinNumberOfBytesNeeded = new IntByReference();
            if (!Advapi32.INSTANCE.ReadEventLog(this._h, 1 | this._flags, 0, this._buffer, (int)this._buffer.size(), pnBytesRead, pnMinNumberOfBytesNeeded)) {
                int rc = Kernel32.INSTANCE.GetLastError();
                if (rc == 122) {
                    this._buffer = new Memory(pnMinNumberOfBytesNeeded.getValue());
                    if (!Advapi32.INSTANCE.ReadEventLog(this._h, 1 | this._flags, 0, this._buffer, (int)this._buffer.size(), pnBytesRead, pnMinNumberOfBytesNeeded)) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    }
                } else {
                    this.close();
                    if (rc != 38) {
                        throw new Win32Exception(rc);
                    }
                    return false;
                }
            }
            this._dwRead = pnBytesRead.getValue();
            this._pevlr = this._buffer;
            return true;
        }

        public void close() {
            this._done = true;
            if (this._h != null) {
                if (!Advapi32.INSTANCE.CloseEventLog(this._h)) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                this._h = null;
            }
        }

        @Override
        public Iterator<EventLogRecord> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            this.read();
            return !this._done;
        }

        @Override
        public EventLogRecord next() {
            this.read();
            EventLogRecord record = new EventLogRecord(this._pevlr);
            this._dwRead -= record.getLength();
            this._pevlr = this._pevlr.share(record.getLength());
            return record;
        }

        @Override
        public void remove() {
        }
    }

    public static class EventLogRecord {
        private WinNT.EVENTLOGRECORD _record = null;
        private String _source;
        private byte[] _data;
        private String[] _strings;

        public WinNT.EVENTLOGRECORD getRecord() {
            return this._record;
        }

        public int getEventId() {
            return this._record.EventID.intValue();
        }

        public String getSource() {
            return this._source;
        }

        public int getStatusCode() {
            return this._record.EventID.intValue() & 0xFFFF;
        }

        public int getRecordNumber() {
            return this._record.RecordNumber.intValue();
        }

        public int getLength() {
            return this._record.Length.intValue();
        }

        public String[] getStrings() {
            return this._strings;
        }

        public EventLogType getType() {
            switch (this._record.EventType.intValue()) {
                case 0: 
                case 4: {
                    return EventLogType.Informational;
                }
                case 16: {
                    return EventLogType.AuditFailure;
                }
                case 8: {
                    return EventLogType.AuditSuccess;
                }
                case 1: {
                    return EventLogType.Error;
                }
                case 2: {
                    return EventLogType.Warning;
                }
            }
            throw new RuntimeException("Invalid type: " + this._record.EventType.intValue());
        }

        public byte[] getData() {
            return this._data;
        }

        public EventLogRecord(Pointer pevlr) {
            this._record = new WinNT.EVENTLOGRECORD(pevlr);
            this._source = pevlr.getString((long)this._record.size(), true);
            if (this._record.DataLength.intValue() > 0) {
                this._data = pevlr.getByteArray(this._record.DataOffset.intValue(), this._record.DataLength.intValue());
            }
            if (this._record.NumStrings.intValue() > 0) {
                ArrayList<String> strings = new ArrayList<String>();
                long offset = this._record.StringOffset.intValue();
                for (int count = this._record.NumStrings.intValue(); count > 0; --count) {
                    String s = pevlr.getString(offset, true);
                    strings.add(s);
                    offset += (long)(s.length() * Native.WCHAR_SIZE);
                    offset += (long)Native.WCHAR_SIZE;
                }
                this._strings = strings.toArray(new String[0]);
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum EventLogType {
        Error,
        Warning,
        Informational,
        AuditSuccess,
        AuditFailure;

    }

    public static class Account {
        public String name;
        public String domain;
        public byte[] sid;
        public String sidString;
        public int accountType;
        public String fqn;
    }
}

