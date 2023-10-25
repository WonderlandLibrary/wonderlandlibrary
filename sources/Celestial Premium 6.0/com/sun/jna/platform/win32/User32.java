/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface User32
extends StdCallLibrary,
WinUser {
    public static final User32 INSTANCE = Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    public WinDef.HDC GetDC(WinDef.HWND var1);

    public int ReleaseDC(WinDef.HWND var1, WinDef.HDC var2);

    public WinDef.HWND FindWindow(String var1, String var2);

    public int GetClassName(WinDef.HWND var1, char[] var2, int var3);

    public boolean GetGUIThreadInfo(int var1, WinUser.GUITHREADINFO var2);

    public boolean GetWindowInfo(WinDef.HWND var1, WinUser.WINDOWINFO var2);

    public boolean GetWindowRect(WinDef.HWND var1, WinDef.RECT var2);

    public int GetWindowText(WinDef.HWND var1, char[] var2, int var3);

    public int GetWindowTextLength(WinDef.HWND var1);

    public int GetWindowModuleFileName(WinDef.HWND var1, char[] var2, int var3);

    public int GetWindowThreadProcessId(WinDef.HWND var1, IntByReference var2);

    public boolean EnumWindows(WinUser.WNDENUMPROC var1, Pointer var2);

    public boolean EnumChildWindows(WinDef.HWND var1, WinUser.WNDENUMPROC var2, Pointer var3);

    public boolean EnumThreadWindows(int var1, WinUser.WNDENUMPROC var2, Pointer var3);

    public boolean FlashWindowEx(WinUser.FLASHWINFO var1);

    public WinDef.HICON LoadIcon(WinDef.HINSTANCE var1, String var2);

    public WinNT.HANDLE LoadImage(WinDef.HINSTANCE var1, String var2, int var3, int var4, int var5, int var6);

    public boolean DestroyIcon(WinDef.HICON var1);

    public int GetWindowLong(WinDef.HWND var1, int var2);

    public int SetWindowLong(WinDef.HWND var1, int var2, int var3);

    public Pointer SetWindowLong(WinDef.HWND var1, int var2, Pointer var3);

    public BaseTSD.LONG_PTR GetWindowLongPtr(WinDef.HWND var1, int var2);

    public BaseTSD.LONG_PTR SetWindowLongPtr(WinDef.HWND var1, int var2, BaseTSD.LONG_PTR var3);

    public Pointer SetWindowLongPtr(WinDef.HWND var1, int var2, Pointer var3);

    public boolean SetLayeredWindowAttributes(WinDef.HWND var1, int var2, byte var3, int var4);

    public boolean GetLayeredWindowAttributes(WinDef.HWND var1, IntByReference var2, ByteByReference var3, IntByReference var4);

    public boolean UpdateLayeredWindow(WinDef.HWND var1, WinDef.HDC var2, WinUser.POINT var3, WinUser.SIZE var4, WinDef.HDC var5, WinUser.POINT var6, int var7, WinUser.BLENDFUNCTION var8, int var9);

    public int SetWindowRgn(WinDef.HWND var1, WinDef.HRGN var2, boolean var3);

    public boolean GetKeyboardState(byte[] var1);

    public short GetAsyncKeyState(int var1);

    public WinUser.HHOOK SetWindowsHookEx(int var1, WinUser.HOOKPROC var2, WinDef.HINSTANCE var3, int var4);

    public WinDef.LRESULT CallNextHookEx(WinUser.HHOOK var1, int var2, WinDef.WPARAM var3, WinDef.LPARAM var4);

    public WinDef.LRESULT CallNextHookEx(WinUser.HHOOK var1, int var2, WinDef.WPARAM var3, Pointer var4);

    public boolean UnhookWindowsHookEx(WinUser.HHOOK var1);

    public int GetMessage(WinUser.MSG var1, WinDef.HWND var2, int var3, int var4);

    public boolean PeekMessage(WinUser.MSG var1, WinDef.HWND var2, int var3, int var4, int var5);

    public boolean TranslateMessage(WinUser.MSG var1);

    public WinDef.LRESULT DispatchMessage(WinUser.MSG var1);

    public void PostMessage(WinDef.HWND var1, int var2, WinDef.WPARAM var3, WinDef.LPARAM var4);

    public void PostQuitMessage(int var1);

    public int GetSystemMetrics(int var1);

    public WinDef.HWND SetParent(WinDef.HWND var1, WinDef.HWND var2);

    public boolean IsWindowVisible(WinDef.HWND var1);

    public boolean MoveWindow(WinDef.HWND var1, int var2, int var3, int var4, int var5, boolean var6);

    public boolean SetWindowPos(WinDef.HWND var1, WinDef.HWND var2, int var3, int var4, int var5, int var6, int var7);

    public boolean AttachThreadInput(WinDef.DWORD var1, WinDef.DWORD var2, boolean var3);

    public boolean SetForegroundWindow(WinDef.HWND var1);

    public WinDef.HWND GetForegroundWindow();

    public WinDef.HWND SetFocus(WinDef.HWND var1);

    public WinDef.DWORD SendInput(WinDef.DWORD var1, WinUser.INPUT[] var2, int var3);

    public WinDef.DWORD WaitForInputIdle(WinNT.HANDLE var1, WinDef.DWORD var2);

    public boolean InvalidateRect(WinDef.HWND var1, Structure.ByReference var2, boolean var3);

    public boolean RedrawWindow(WinDef.HWND var1, Structure.ByReference var2, WinDef.HRGN var3, WinDef.DWORD var4);

    public WinDef.HWND GetWindow(WinDef.HWND var1, WinDef.DWORD var2);

    public boolean UpdateWindow(WinDef.HWND var1);

    public boolean ShowWindow(WinDef.HWND var1, int var2);

    public boolean CloseWindow(WinDef.HWND var1);

    public boolean RegisterHotKey(WinDef.HWND var1, int var2, int var3, int var4);

    public boolean UnregisterHotKey(Pointer var1, int var2);
}

