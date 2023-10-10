// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Native;

public abstract class Shell32Util
{
    public static String getFolderPath(final WinDef.HWND hwnd, final int nFolder, final WinDef.DWORD dwFlags) {
        final char[] pszPath = new char[260];
        final WinNT.HRESULT hr = Shell32.INSTANCE.SHGetFolderPath(hwnd, nFolder, null, dwFlags, pszPath);
        if (!hr.equals(W32Errors.S_OK)) {
            throw new Win32Exception(hr);
        }
        return Native.toString(pszPath);
    }
    
    public static String getFolderPath(final int nFolder) {
        return getFolderPath(null, nFolder, ShlObj.SHGFP_TYPE_CURRENT);
    }
    
    public static String getKnownFolderPath(final Guid.GUID guid) throws Win32Exception {
        final int flags = ShlObj.KNOWN_FOLDER_FLAG.NONE.getFlag();
        final PointerByReference outPath = new PointerByReference();
        final WinNT.HANDLE token = null;
        final WinNT.HRESULT hr = Shell32.INSTANCE.SHGetKnownFolderPath(guid, flags, token, outPath);
        if (!W32Errors.SUCCEEDED(hr.intValue())) {
            throw new Win32Exception(hr);
        }
        final String result = outPath.getValue().getWideString(0L);
        Ole32.INSTANCE.CoTaskMemFree(outPath.getValue());
        return result;
    }
    
    public static final String getSpecialFolderPath(final int csidl, final boolean create) {
        final char[] pszPath = new char[260];
        if (!Shell32.INSTANCE.SHGetSpecialFolderPath(null, pszPath, csidl, create)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(pszPath);
    }
    
    public static final String[] CommandLineToArgv(final String cmdLine) {
        final WString cl = new WString(cmdLine);
        final IntByReference nargs = new IntByReference();
        final Pointer strArr = Shell32.INSTANCE.CommandLineToArgvW(cl, nargs);
        if (strArr != null) {
            try {
                return strArr.getWideStringArray(0L, nargs.getValue());
            }
            finally {
                Kernel32.INSTANCE.LocalFree(strArr);
            }
        }
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
}
