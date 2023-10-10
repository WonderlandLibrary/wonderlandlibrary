// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary, WinUser, WinNT
{
    public static final User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final WinDef.HWND HWND_MESSAGE = new WinDef.HWND(Pointer.createConstant(-3));
    public static final int CS_GLOBALCLASS = 16384;
    public static final int WS_EX_TOPMOST = 8;
    public static final int DEVICE_NOTIFY_WINDOW_HANDLE = 0;
    public static final int DEVICE_NOTIFY_SERVICE_HANDLE = 1;
    public static final int DEVICE_NOTIFY_ALL_INTERFACE_CLASSES = 4;
    public static final int SW_SHOWDEFAULT = 10;
    
    WinDef.HDC GetDC(final WinDef.HWND p0);
    
    int ReleaseDC(final WinDef.HWND p0, final WinDef.HDC p1);
    
    WinDef.HWND FindWindow(final String p0, final String p1);
    
    int GetClassName(final WinDef.HWND p0, final char[] p1, final int p2);
    
    boolean GetGUIThreadInfo(final int p0, final GUITHREADINFO p1);
    
    boolean GetWindowInfo(final WinDef.HWND p0, final WINDOWINFO p1);
    
    boolean GetWindowRect(final WinDef.HWND p0, final WinDef.RECT p1);
    
    boolean GetClientRect(final WinDef.HWND p0, final WinDef.RECT p1);
    
    int GetWindowText(final WinDef.HWND p0, final char[] p1, final int p2);
    
    int GetWindowTextLength(final WinDef.HWND p0);
    
    int GetWindowModuleFileName(final WinDef.HWND p0, final char[] p1, final int p2);
    
    int GetWindowThreadProcessId(final WinDef.HWND p0, final IntByReference p1);
    
    boolean EnumWindows(final WNDENUMPROC p0, final Pointer p1);
    
    boolean EnumChildWindows(final WinDef.HWND p0, final WNDENUMPROC p1, final Pointer p2);
    
    boolean EnumThreadWindows(final int p0, final WNDENUMPROC p1, final Pointer p2);
    
    boolean BringWindowToTop(final WinDef.HWND p0);
    
    boolean FlashWindowEx(final FLASHWINFO p0);
    
    WinDef.HICON LoadIcon(final WinDef.HINSTANCE p0, final String p1);
    
    HANDLE LoadImage(final WinDef.HINSTANCE p0, final String p1, final int p2, final int p3, final int p4, final int p5);
    
    boolean DestroyIcon(final WinDef.HICON p0);
    
    int GetWindowLong(final WinDef.HWND p0, final int p1);
    
    int SetWindowLong(final WinDef.HWND p0, final int p1, final int p2);
    
    BaseTSD.LONG_PTR GetWindowLongPtr(final WinDef.HWND p0, final int p1);
    
    Pointer SetWindowLongPtr(final WinDef.HWND p0, final int p1, final Pointer p2);
    
    boolean SetLayeredWindowAttributes(final WinDef.HWND p0, final int p1, final byte p2, final int p3);
    
    boolean GetLayeredWindowAttributes(final WinDef.HWND p0, final IntByReference p1, final ByteByReference p2, final IntByReference p3);
    
    boolean UpdateLayeredWindow(final WinDef.HWND p0, final WinDef.HDC p1, final WinDef.POINT p2, final SIZE p3, final WinDef.HDC p4, final WinDef.POINT p5, final int p6, final BLENDFUNCTION p7, final int p8);
    
    int SetWindowRgn(final WinDef.HWND p0, final WinDef.HRGN p1, final boolean p2);
    
    boolean GetKeyboardState(final byte[] p0);
    
    short GetAsyncKeyState(final int p0);
    
    HHOOK SetWindowsHookEx(final int p0, final HOOKPROC p1, final WinDef.HINSTANCE p2, final int p3);
    
    WinDef.LRESULT CallNextHookEx(final HHOOK p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3);
    
    boolean UnhookWindowsHookEx(final HHOOK p0);
    
    int GetMessage(final MSG p0, final WinDef.HWND p1, final int p2, final int p3);
    
    boolean PeekMessage(final MSG p0, final WinDef.HWND p1, final int p2, final int p3, final int p4);
    
    boolean TranslateMessage(final MSG p0);
    
    WinDef.LRESULT DispatchMessage(final MSG p0);
    
    void PostMessage(final WinDef.HWND p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3);
    
    int PostThreadMessage(final int p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3);
    
    void PostQuitMessage(final int p0);
    
    int GetSystemMetrics(final int p0);
    
    WinDef.HWND SetParent(final WinDef.HWND p0, final WinDef.HWND p1);
    
    boolean IsWindowVisible(final WinDef.HWND p0);
    
    boolean MoveWindow(final WinDef.HWND p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    boolean SetWindowPos(final WinDef.HWND p0, final WinDef.HWND p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    boolean AttachThreadInput(final WinDef.DWORD p0, final WinDef.DWORD p1, final boolean p2);
    
    boolean SetForegroundWindow(final WinDef.HWND p0);
    
    WinDef.HWND GetForegroundWindow();
    
    WinDef.HWND SetFocus(final WinDef.HWND p0);
    
    WinDef.DWORD SendInput(final WinDef.DWORD p0, final INPUT[] p1, final int p2);
    
    WinDef.DWORD WaitForInputIdle(final HANDLE p0, final WinDef.DWORD p1);
    
    boolean InvalidateRect(final WinDef.HWND p0, final WinDef.RECT p1, final boolean p2);
    
    boolean RedrawWindow(final WinDef.HWND p0, final WinDef.RECT p1, final WinDef.HRGN p2, final WinDef.DWORD p3);
    
    WinDef.HWND GetWindow(final WinDef.HWND p0, final WinDef.DWORD p1);
    
    boolean UpdateWindow(final WinDef.HWND p0);
    
    boolean ShowWindow(final WinDef.HWND p0, final int p1);
    
    boolean CloseWindow(final WinDef.HWND p0);
    
    boolean RegisterHotKey(final WinDef.HWND p0, final int p1, final int p2, final int p3);
    
    boolean UnregisterHotKey(final Pointer p0, final int p1);
    
    boolean GetLastInputInfo(final LASTINPUTINFO p0);
    
    WinDef.ATOM RegisterClassEx(final WNDCLASSEX p0);
    
    boolean UnregisterClass(final String p0, final WinDef.HINSTANCE p1);
    
    WinDef.HWND CreateWindowEx(final int p0, final String p1, final String p2, final int p3, final int p4, final int p5, final int p6, final int p7, final WinDef.HWND p8, final WinDef.HMENU p9, final WinDef.HINSTANCE p10, final WinDef.LPVOID p11);
    
    boolean DestroyWindow(final WinDef.HWND p0);
    
    boolean GetClassInfoEx(final WinDef.HINSTANCE p0, final String p1, final WNDCLASSEX p2);
    
    WinDef.LRESULT CallWindowProc(final Pointer p0, final WinDef.HWND p1, final int p2, final WinDef.WPARAM p3, final WinDef.LPARAM p4);
    
    WinDef.LRESULT DefWindowProc(final WinDef.HWND p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3);
    
    HDEVNOTIFY RegisterDeviceNotification(final HANDLE p0, final Structure p1, final int p2);
    
    boolean UnregisterDeviceNotification(final HDEVNOTIFY p0);
    
    int RegisterWindowMessage(final String p0);
    
    HMONITOR MonitorFromPoint(final WinDef.POINT.ByValue p0, final int p1);
    
    HMONITOR MonitorFromRect(final WinDef.RECT p0, final int p1);
    
    HMONITOR MonitorFromWindow(final WinDef.HWND p0, final int p1);
    
    WinDef.BOOL GetMonitorInfo(final HMONITOR p0, final MONITORINFO p1);
    
    WinDef.BOOL GetMonitorInfo(final HMONITOR p0, final MONITORINFOEX p1);
    
    WinDef.BOOL EnumDisplayMonitors(final WinDef.HDC p0, final WinDef.RECT p1, final MONITORENUMPROC p2, final WinDef.LPARAM p3);
    
    WinDef.BOOL GetWindowPlacement(final WinDef.HWND p0, final WINDOWPLACEMENT p1);
    
    WinDef.BOOL SetWindowPlacement(final WinDef.HWND p0, final WINDOWPLACEMENT p1);
    
    WinDef.BOOL AdjustWindowRect(final WinDef.RECT p0, final WinDef.DWORD p1, final WinDef.BOOL p2);
    
    WinDef.BOOL AdjustWindowRectEx(final WinDef.RECT p0, final WinDef.DWORD p1, final WinDef.BOOL p2, final WinDef.DWORD p3);
    
    WinDef.BOOL ExitWindowsEx(final WinDef.UINT p0, final WinDef.DWORD p1);
    
    WinDef.BOOL LockWorkStation();
    
    boolean GetIconInfo(final WinDef.HICON p0, final WinGDI.ICONINFO p1);
    
    WinDef.LRESULT SendMessageTimeout(final WinDef.HWND p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3, final int p4, final int p5, final WinDef.DWORDByReference p6);
    
    BaseTSD.ULONG_PTR GetClassLongPtr(final WinDef.HWND p0, final int p1);
    
    int GetRawInputDeviceList(final RAWINPUTDEVICELIST[] p0, final IntByReference p1, final int p2);
    
    WinDef.HWND GetDesktopWindow();
    
    boolean PrintWindow(final WinDef.HWND p0, final WinDef.HDC p1, final int p2);
    
    boolean IsWindowEnabled(final WinDef.HWND p0);
    
    boolean IsWindow(final WinDef.HWND p0);
    
    WinDef.HWND FindWindowEx(final WinDef.HWND p0, final WinDef.HWND p1, final String p2, final String p3);
    
    WinDef.HWND GetAncestor(final WinDef.HWND p0, final int p1);
    
    WinDef.HWND GetParent(final WinDef.HWND p0);
    
    boolean GetCursorPos(final WinDef.POINT p0);
    
    boolean SetCursorPos(final long p0, final long p1);
    
    HANDLE SetWinEventHook(final int p0, final int p1, final WinDef.HMODULE p2, final WinEventProc p3, final int p4, final int p5, final int p6);
    
    boolean UnhookWinEvent(final HANDLE p0);
    
    WinDef.HICON CopyIcon(final WinDef.HICON p0);
    
    int GetClassLong(final WinDef.HWND p0, final int p1);
    
    int RegisterClipboardFormat(final String p0);
    
    WinDef.HWND GetActiveWindow();
    
    WinDef.LRESULT SendMessage(final WinDef.HWND p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3);
    
    int GetKeyboardLayoutList(final int p0, final WinDef.HKL[] p1);
    
    WinDef.HKL GetKeyboardLayout(final int p0);
    
    boolean GetKeyboardLayoutName(final char[] p0);
    
    short VkKeyScanExA(final byte p0, final WinDef.HKL p1);
    
    short VkKeyScanExW(final char p0, final WinDef.HKL p1);
    
    int MapVirtualKeyEx(final int p0, final int p1, final WinDef.HKL p2);
    
    int ToUnicodeEx(final int p0, final int p1, final byte[] p2, final char[] p3, final int p4, final int p5, final WinDef.HKL p6);
    
    int LoadString(final WinDef.HINSTANCE p0, final int p1, final Pointer p2, final int p3);
}
