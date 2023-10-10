// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.win32.W32APITypeMapper;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.sun.jna.Structure;
import com.sun.jna.Memory;
import java.nio.ByteOrder;
import java.util.TreeMap;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;
import com.sun.jna.LastErrorException;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public abstract class Kernel32Util implements WinDef
{
    public static final String VOLUME_GUID_PATH_PREFIX = "\\\\?\\Volume{";
    public static final String VOLUME_GUID_PATH_SUFFIX = "}\\";
    
    public static String getComputerName() {
        final char[] buffer = new char[WinBase.MAX_COMPUTERNAME_LENGTH + 1];
        final IntByReference lpnSize = new IntByReference(buffer.length);
        if (!Kernel32.INSTANCE.GetComputerName(buffer, lpnSize)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(buffer);
    }
    
    public static void freeLocalMemory(final Pointer ptr) {
        final Pointer res = Kernel32.INSTANCE.LocalFree(ptr);
        if (res != null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static void freeGlobalMemory(final Pointer ptr) {
        final Pointer res = Kernel32.INSTANCE.GlobalFree(ptr);
        if (res != null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static void closeHandleRefs(final WinNT.HANDLEByReference... refs) {
        Win32Exception err = null;
        for (final WinNT.HANDLEByReference r : refs) {
            try {
                closeHandleRef(r);
            }
            catch (Win32Exception e) {
                if (err == null) {
                    err = e;
                }
                else {
                    err.addSuppressedReflected(e);
                }
            }
        }
        if (err != null) {
            throw err;
        }
    }
    
    public static void closeHandleRef(final WinNT.HANDLEByReference ref) {
        closeHandle((ref == null) ? null : ref.getValue());
    }
    
    public static void closeHandles(final WinNT.HANDLE... handles) {
        Win32Exception err = null;
        for (final WinNT.HANDLE h : handles) {
            try {
                closeHandle(h);
            }
            catch (Win32Exception e) {
                if (err == null) {
                    err = e;
                }
                else {
                    err.addSuppressedReflected(e);
                }
            }
        }
        if (err != null) {
            throw err;
        }
    }
    
    public static void closeHandle(final WinNT.HANDLE h) {
        if (h == null) {
            return;
        }
        if (!Kernel32.INSTANCE.CloseHandle(h)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static String formatMessage(final int code) {
        final PointerByReference buffer = new PointerByReference();
        final int nLen = Kernel32.INSTANCE.FormatMessage(4864, null, code, 0, buffer, 0, null);
        if (nLen == 0) {
            throw new LastErrorException(Native.getLastError());
        }
        final Pointer ptr = buffer.getValue();
        try {
            final String s = ptr.getWideString(0L);
            return s.trim();
        }
        finally {
            freeLocalMemory(ptr);
        }
    }
    
    public static String formatMessage(final WinNT.HRESULT code) {
        return formatMessage(code.intValue());
    }
    
    public static String formatMessageFromLastErrorCode(final int code) {
        return formatMessage(W32Errors.HRESULT_FROM_WIN32(code));
    }
    
    public static String getLastErrorMessage() {
        return formatMessageFromLastErrorCode(Kernel32.INSTANCE.GetLastError());
    }
    
    public static String getTempPath() {
        final DWORD nBufferLength = new DWORD(260L);
        final char[] buffer = new char[nBufferLength.intValue()];
        if (Kernel32.INSTANCE.GetTempPath(nBufferLength, buffer).intValue() == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(buffer);
    }
    
    public static void deleteFile(final String filename) {
        if (!Kernel32.INSTANCE.DeleteFile(filename)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static List<String> getLogicalDriveStrings() {
        DWORD dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(new DWORD(0L), null);
        if (dwSize.intValue() <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final char[] buf = new char[dwSize.intValue()];
        dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(dwSize, buf);
        final int bufSize = dwSize.intValue();
        if (bufSize <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toStringList(buf, 0, bufSize);
    }
    
    public static int getFileAttributes(final String fileName) {
        final int fileAttributes = Kernel32.INSTANCE.GetFileAttributes(fileName);
        if (fileAttributes == -1) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return fileAttributes;
    }
    
    public static int getFileType(final String fileName) throws FileNotFoundException {
        final File f = new File(fileName);
        if (!f.exists()) {
            throw new FileNotFoundException(fileName);
        }
        WinNT.HANDLE hFile = null;
        Win32Exception err = null;
        try {
            hFile = Kernel32.INSTANCE.CreateFile(fileName, Integer.MIN_VALUE, 1, new WinBase.SECURITY_ATTRIBUTES(), 3, 128, new WinNT.HANDLEByReference().getValue());
            if (WinBase.INVALID_HANDLE_VALUE.equals(hFile)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            final int type = Kernel32.INSTANCE.GetFileType(hFile);
            Label_0165: {
                switch (type) {
                    case 0: {
                        final int rc = Kernel32.INSTANCE.GetLastError();
                        switch (rc) {
                            case 0: {
                                break Label_0165;
                            }
                            default: {
                                throw new Win32Exception(rc);
                            }
                        }
                        break;
                    }
                }
            }
            return type;
        }
        catch (Win32Exception e) {
            err = e;
            throw err;
        }
        finally {
            try {
                closeHandle(hFile);
            }
            catch (Win32Exception e2) {
                if (err == null) {
                    err = e2;
                }
                else {
                    err.addSuppressedReflected(e2);
                }
            }
            if (err != null) {
                throw err;
            }
        }
    }
    
    public static int getDriveType(final String rootName) {
        return Kernel32.INSTANCE.GetDriveType(rootName);
    }
    
    public static String getEnvironmentVariable(final String name) {
        int size = Kernel32.INSTANCE.GetEnvironmentVariable(name, null, 0);
        if (size == 0) {
            return null;
        }
        if (size < 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final char[] buffer = new char[size];
        size = Kernel32.INSTANCE.GetEnvironmentVariable(name, buffer, buffer.length);
        if (size <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(buffer);
    }
    
    public static Map<String, String> getEnvironmentVariables() {
        final Pointer lpszEnvironmentBlock = Kernel32.INSTANCE.GetEnvironmentStrings();
        if (lpszEnvironmentBlock == null) {
            throw new LastErrorException(Kernel32.INSTANCE.GetLastError());
        }
        try {
            return getEnvironmentVariables(lpszEnvironmentBlock, 0L);
        }
        finally {
            if (!Kernel32.INSTANCE.FreeEnvironmentStrings(lpszEnvironmentBlock)) {
                throw new LastErrorException(Kernel32.INSTANCE.GetLastError());
            }
        }
    }
    
    public static Map<String, String> getEnvironmentVariables(final Pointer lpszEnvironmentBlock, final long offset) {
        if (lpszEnvironmentBlock == null) {
            return null;
        }
        final Map<String, String> vars = new TreeMap<String, String>();
        final boolean asWideChars = isWideCharEnvironmentStringBlock(lpszEnvironmentBlock, offset);
        final long stepFactor = asWideChars ? 2L : 1L;
        long curOffset = offset;
        while (true) {
            final String nvp = readEnvironmentStringBlockEntry(lpszEnvironmentBlock, curOffset, asWideChars);
            final int len = nvp.length();
            if (len == 0) {
                return vars;
            }
            final int pos = nvp.indexOf(61);
            if (pos < 0) {
                throw new IllegalArgumentException("Missing variable value separator in " + nvp);
            }
            final String name = nvp.substring(0, pos);
            final String value = nvp.substring(pos + 1);
            vars.put(name, value);
            curOffset += (len + 1) * stepFactor;
        }
    }
    
    public static String readEnvironmentStringBlockEntry(final Pointer lpszEnvironmentBlock, final long offset, final boolean asWideChars) {
        final long endOffset = findEnvironmentStringBlockEntryEnd(lpszEnvironmentBlock, offset, asWideChars);
        final int dataLen = (int)(endOffset - offset);
        if (dataLen == 0) {
            return "";
        }
        final int charsLen = asWideChars ? (dataLen / 2) : dataLen;
        final char[] chars = new char[charsLen];
        long curOffset = offset;
        final long stepSize = asWideChars ? 2L : 1L;
        final ByteOrder byteOrder = ByteOrder.nativeOrder();
        for (int index = 0; index < chars.length; ++index, curOffset += stepSize) {
            final byte b = lpszEnvironmentBlock.getByte(curOffset);
            if (asWideChars) {
                final byte x = lpszEnvironmentBlock.getByte(curOffset + 1L);
                if (ByteOrder.LITTLE_ENDIAN.equals(byteOrder)) {
                    chars[index] = (char)((x << 8 & 0xFF00) | (b & 0xFF));
                }
                else {
                    chars[index] = (char)((b << 8 & 0xFF00) | (x & 0xFF));
                }
            }
            else {
                chars[index] = (char)(b & 0xFF);
            }
        }
        return new String(chars);
    }
    
    public static long findEnvironmentStringBlockEntryEnd(final Pointer lpszEnvironmentBlock, final long offset, final boolean asWideChars) {
        long curOffset = offset;
        final long stepSize = asWideChars ? 2L : 1L;
        while (true) {
            final byte b = lpszEnvironmentBlock.getByte(curOffset);
            if (b == 0) {
                break;
            }
            curOffset += stepSize;
        }
        return curOffset;
    }
    
    public static boolean isWideCharEnvironmentStringBlock(final Pointer lpszEnvironmentBlock, final long offset) {
        final byte b0 = lpszEnvironmentBlock.getByte(offset);
        final byte b2 = lpszEnvironmentBlock.getByte(offset + 1L);
        final ByteOrder byteOrder = ByteOrder.nativeOrder();
        if (ByteOrder.LITTLE_ENDIAN.equals(byteOrder)) {
            return isWideCharEnvironmentStringBlock(b2);
        }
        return isWideCharEnvironmentStringBlock(b0);
    }
    
    private static boolean isWideCharEnvironmentStringBlock(final byte charsetIndicator) {
        return charsetIndicator == 0;
    }
    
    public static final int getPrivateProfileInt(final String appName, final String keyName, final int defaultValue, final String fileName) {
        return Kernel32.INSTANCE.GetPrivateProfileInt(appName, keyName, defaultValue, fileName);
    }
    
    public static final String getPrivateProfileString(final String lpAppName, final String lpKeyName, final String lpDefault, final String lpFileName) {
        final char[] buffer = new char[1024];
        Kernel32.INSTANCE.GetPrivateProfileString(lpAppName, lpKeyName, lpDefault, buffer, new DWORD((long)buffer.length), lpFileName);
        return Native.toString(buffer);
    }
    
    public static final void writePrivateProfileString(final String appName, final String keyName, final String string, final String fileName) {
        if (!Kernel32.INSTANCE.WritePrivateProfileString(appName, keyName, string, fileName)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[] getLogicalProcessorInformation() {
        final int sizePerStruct = new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION().size();
        final DWORDByReference bufferSize = new DWORDByReference(new DWORD((long)sizePerStruct));
        while (true) {
            final Memory memory = new Memory(bufferSize.getValue().intValue());
            if (Kernel32.INSTANCE.GetLogicalProcessorInformation(memory, bufferSize)) {
                final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION firstInformation = new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION(memory);
                final int returnedStructCount = bufferSize.getValue().intValue() / sizePerStruct;
                return (WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[])firstInformation.toArray(new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[returnedStructCount]);
            }
            final int err = Kernel32.INSTANCE.GetLastError();
            if (err != 122) {
                throw new Win32Exception(err);
            }
        }
    }
    
    public static final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX[] getLogicalProcessorInformationEx(final int relationshipType) {
        final DWORDByReference bufferSize = new DWORDByReference(new DWORD(1L));
        while (true) {
            final Memory memory = new Memory(bufferSize.getValue().intValue());
            if (Kernel32.INSTANCE.GetLogicalProcessorInformationEx(relationshipType, memory, bufferSize)) {
                final List<WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX> procInfoList = new ArrayList<WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX>();
                WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX information;
                for (int offset = 0; offset < bufferSize.getValue().intValue(); offset += information.size) {
                    information = WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX.fromPointer(memory.share(offset));
                    procInfoList.add(information);
                }
                return procInfoList.toArray(new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX[0]);
            }
            final int err = Kernel32.INSTANCE.GetLastError();
            if (err != 122) {
                throw new Win32Exception(err);
            }
        }
    }
    
    public static final String[] getPrivateProfileSection(final String appName, final String fileName) {
        final char[] buffer = new char[32768];
        if (Kernel32.INSTANCE.GetPrivateProfileSection(appName, buffer, new DWORD((long)buffer.length), fileName).intValue() == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return new String(buffer).split("\u0000");
    }
    
    public static final String[] getPrivateProfileSectionNames(final String fileName) {
        final char[] buffer = new char[65536];
        if (Kernel32.INSTANCE.GetPrivateProfileSectionNames(buffer, new DWORD((long)buffer.length), fileName).intValue() == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return new String(buffer).split("\u0000");
    }
    
    public static final void writePrivateProfileSection(final String appName, final String[] strings, final String fileName) {
        final StringBuilder buffer = new StringBuilder();
        for (final String string : strings) {
            buffer.append(string).append('\0');
        }
        buffer.append('\0');
        if (!Kernel32.INSTANCE.WritePrivateProfileSection(appName, buffer.toString(), fileName)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static final List<String> queryDosDevice(final String lpszDeviceName, final int maxTargetSize) {
        final char[] lpTargetPath = new char[maxTargetSize];
        final int dwSize = Kernel32.INSTANCE.QueryDosDevice(lpszDeviceName, lpTargetPath, lpTargetPath.length);
        if (dwSize == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toStringList(lpTargetPath, 0, dwSize);
    }
    
    public static final List<String> getVolumePathNamesForVolumeName(final String lpszVolumeName) {
        char[] lpszVolumePathNames = new char[261];
        final IntByReference lpcchReturnLength = new IntByReference();
        if (!Kernel32.INSTANCE.GetVolumePathNamesForVolumeName(lpszVolumeName, lpszVolumePathNames, lpszVolumePathNames.length, lpcchReturnLength)) {
            final int hr = Kernel32.INSTANCE.GetLastError();
            if (hr != 234) {
                throw new Win32Exception(hr);
            }
            final int required = lpcchReturnLength.getValue();
            lpszVolumePathNames = new char[required];
            if (!Kernel32.INSTANCE.GetVolumePathNamesForVolumeName(lpszVolumeName, lpszVolumePathNames, lpszVolumePathNames.length, lpcchReturnLength)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
        final int bufSize = lpcchReturnLength.getValue();
        return Native.toStringList(lpszVolumePathNames, 0, bufSize);
    }
    
    public static final String extractVolumeGUID(final String volumeGUIDPath) {
        if (volumeGUIDPath == null || volumeGUIDPath.length() <= "\\\\?\\Volume{".length() + "}\\".length() || !volumeGUIDPath.startsWith("\\\\?\\Volume{") || !volumeGUIDPath.endsWith("}\\")) {
            throw new IllegalArgumentException("Bad volume GUID path format: " + volumeGUIDPath);
        }
        return volumeGUIDPath.substring("\\\\?\\Volume{".length(), volumeGUIDPath.length() - "}\\".length());
    }
    
    public static final String QueryFullProcessImageName(final int pid, final int dwFlags) {
        WinNT.HANDLE hProcess = null;
        Win32Exception we = null;
        try {
            hProcess = Kernel32.INSTANCE.OpenProcess(1040, false, pid);
            if (hProcess == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return QueryFullProcessImageName(hProcess, dwFlags);
        }
        catch (Win32Exception e) {
            we = e;
            throw we;
        }
        finally {
            try {
                closeHandle(hProcess);
            }
            catch (Win32Exception e2) {
                if (we == null) {
                    we = e2;
                }
                else {
                    we.addSuppressed(e2);
                }
            }
            if (we != null) {
                throw we;
            }
        }
    }
    
    public static final String QueryFullProcessImageName(final WinNT.HANDLE hProcess, final int dwFlags) {
        int size = 260;
        final IntByReference lpdwSize = new IntByReference();
        do {
            final char[] lpExeName = new char[size];
            lpdwSize.setValue(size);
            if (Kernel32.INSTANCE.QueryFullProcessImageName(hProcess, dwFlags, lpExeName, lpdwSize)) {
                return new String(lpExeName, 0, lpdwSize.getValue());
            }
            size += 1024;
        } while (Kernel32.INSTANCE.GetLastError() == 122);
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    public static byte[] getResource(final String path, final String type, final String name) {
        final HMODULE target = Kernel32.INSTANCE.LoadLibraryEx(path, null, 2);
        if (target == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Win32Exception err = null;
        Pointer start = null;
        int length = 0;
        byte[] results = null;
        try {
            Pointer t = null;
            try {
                t = new Pointer(Long.parseLong(type));
            }
            catch (NumberFormatException e) {
                t = new Memory(Native.WCHAR_SIZE * (type.length() + 1));
                t.setWideString(0L, type);
            }
            Pointer n = null;
            try {
                n = new Pointer(Long.parseLong(name));
            }
            catch (NumberFormatException e2) {
                n = new Memory(Native.WCHAR_SIZE * (name.length() + 1));
                n.setWideString(0L, name);
            }
            final HRSRC hrsrc = Kernel32.INSTANCE.FindResource(target, n, t);
            if (hrsrc == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            final WinNT.HANDLE loaded = Kernel32.INSTANCE.LoadResource(target, hrsrc);
            if (loaded == null) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            length = Kernel32.INSTANCE.SizeofResource(target, hrsrc);
            if (length == 0) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            start = Kernel32.INSTANCE.LockResource(loaded);
            if (start == null) {
                throw new IllegalStateException("LockResource returned null.");
            }
            results = start.getByteArray(0L, length);
        }
        catch (Win32Exception we) {
            err = we;
        }
        finally {
            if (target != null && !Kernel32.INSTANCE.FreeLibrary(target)) {
                final Win32Exception we2 = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                if (err != null) {
                    we2.addSuppressedReflected(err);
                }
                throw we2;
            }
        }
        if (err != null) {
            throw err;
        }
        return results;
    }
    
    public static Map<String, List<String>> getResourceNames(final String path) {
        final HMODULE target = Kernel32.INSTANCE.LoadLibraryEx(path, null, 2);
        if (target == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final List<String> types = new ArrayList<String>();
        final Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
        final WinBase.EnumResTypeProc ertp = new WinBase.EnumResTypeProc() {
            @Override
            public boolean invoke(final HMODULE module, final Pointer type, final Pointer lParam) {
                if (Pointer.nativeValue(type) <= 65535L) {
                    types.add(Pointer.nativeValue(type) + "");
                }
                else {
                    types.add(type.getWideString(0L));
                }
                return true;
            }
        };
        final WinBase.EnumResNameProc ernp = new WinBase.EnumResNameProc() {
            @Override
            public boolean invoke(final HMODULE module, final Pointer type, final Pointer name, final Pointer lParam) {
                String typeName = "";
                if (Pointer.nativeValue(type) <= 65535L) {
                    typeName = Pointer.nativeValue(type) + "";
                }
                else {
                    typeName = type.getWideString(0L);
                }
                if (Pointer.nativeValue(name) < 65535L) {
                    result.get(typeName).add(Pointer.nativeValue(name) + "");
                }
                else {
                    result.get(typeName).add(name.getWideString(0L));
                }
                return true;
            }
        };
        Win32Exception err = null;
        try {
            if (!Kernel32.INSTANCE.EnumResourceTypes(target, ertp, null)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            for (final String typeName : types) {
                result.put(typeName, new ArrayList<String>());
                Pointer pointer = null;
                try {
                    pointer = new Pointer(Long.parseLong(typeName));
                }
                catch (NumberFormatException e2) {
                    pointer = new Memory(Native.WCHAR_SIZE * (typeName.length() + 1));
                    pointer.setWideString(0L, typeName);
                }
                final boolean callResult = Kernel32.INSTANCE.EnumResourceNames(target, pointer, ernp, null);
                if (!callResult) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
            }
        }
        catch (Win32Exception e) {
            err = e;
        }
        finally {
            if (target != null && !Kernel32.INSTANCE.FreeLibrary(target)) {
                final Win32Exception we = new Win32Exception(Kernel32.INSTANCE.GetLastError());
                if (err != null) {
                    we.addSuppressedReflected(err);
                }
                throw we;
            }
        }
        if (err != null) {
            throw err;
        }
        return result;
    }
    
    public static List<Tlhelp32.MODULEENTRY32W> getModules(final int processID) {
        final WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPMODULE, new DWORD((long)processID));
        if (snapshot == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Win32Exception we = null;
        try {
            final Tlhelp32.MODULEENTRY32W first = new Tlhelp32.MODULEENTRY32W();
            if (!Kernel32.INSTANCE.Module32FirstW(snapshot, first)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            final List<Tlhelp32.MODULEENTRY32W> modules = new ArrayList<Tlhelp32.MODULEENTRY32W>();
            modules.add(first);
            for (Tlhelp32.MODULEENTRY32W next = new Tlhelp32.MODULEENTRY32W(); Kernel32.INSTANCE.Module32NextW(snapshot, next); next = new Tlhelp32.MODULEENTRY32W()) {
                modules.add(next);
            }
            final int lastError = Kernel32.INSTANCE.GetLastError();
            if (lastError != 0 && lastError != 18) {
                throw new Win32Exception(lastError);
            }
            return modules;
        }
        catch (Win32Exception e) {
            we = e;
            throw we;
        }
        finally {
            try {
                closeHandle(snapshot);
            }
            catch (Win32Exception e2) {
                if (we == null) {
                    we = e2;
                }
                else {
                    we.addSuppressedReflected(e2);
                }
            }
            if (we != null) {
                throw we;
            }
        }
    }
    
    public static String expandEnvironmentStrings(final String input) {
        if (input == null) {
            return "";
        }
        int resultChars = Kernel32.INSTANCE.ExpandEnvironmentStrings(input, null, 0);
        if (resultChars == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        Memory resultMemory;
        if (W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE) {
            resultMemory = new Memory(resultChars * Native.WCHAR_SIZE);
        }
        else {
            resultMemory = new Memory(resultChars + 1);
        }
        resultChars = Kernel32.INSTANCE.ExpandEnvironmentStrings(input, resultMemory, resultChars);
        if (resultChars == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        if (W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE) {
            return resultMemory.getWideString(0L);
        }
        return resultMemory.getString(0L);
    }
}
