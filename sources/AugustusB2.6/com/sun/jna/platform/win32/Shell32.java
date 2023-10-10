// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Shell32 extends ShellAPI, StdCallLibrary
{
    public static final Shell32 INSTANCE = Native.load("shell32", Shell32.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int SHERB_NOCONFIRMATION = 1;
    public static final int SHERB_NOPROGRESSUI = 2;
    public static final int SHERB_NOSOUND = 4;
    public static final int SEE_MASK_NOCLOSEPROCESS = 64;
    public static final int SEE_MASK_FLAG_NO_UI = 1024;
    
    int SHFileOperation(final SHFILEOPSTRUCT p0);
    
    WinNT.HRESULT SHGetFolderPath(final WinDef.HWND p0, final int p1, final WinNT.HANDLE p2, final WinDef.DWORD p3, final char[] p4);
    
    WinNT.HRESULT SHGetKnownFolderPath(final Guid.GUID p0, final int p1, final WinNT.HANDLE p2, final PointerByReference p3);
    
    WinNT.HRESULT SHGetDesktopFolder(final PointerByReference p0);
    
    WinDef.INT_PTR ShellExecute(final WinDef.HWND p0, final String p1, final String p2, final String p3, final String p4, final int p5);
    
    boolean SHGetSpecialFolderPath(final WinDef.HWND p0, final char[] p1, final int p2, final boolean p3);
    
    WinDef.UINT_PTR SHAppBarMessage(final WinDef.DWORD p0, final APPBARDATA p1);
    
    int SHEmptyRecycleBin(final WinNT.HANDLE p0, final String p1, final int p2);
    
    boolean ShellExecuteEx(final SHELLEXECUTEINFO p0);
    
    WinNT.HRESULT SHGetSpecialFolderLocation(final WinDef.HWND p0, final int p1, final PointerByReference p2);
    
    int ExtractIconEx(final String p0, final int p1, final WinDef.HICON[] p2, final WinDef.HICON[] p3, final int p4);
    
    WinNT.HRESULT GetCurrentProcessExplicitAppUserModelID(final PointerByReference p0);
    
    WinNT.HRESULT SetCurrentProcessExplicitAppUserModelID(final WString p0);
    
    Pointer CommandLineToArgvW(final WString p0, final IntByReference p1);
}
