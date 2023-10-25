package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface User32 extends StdCallLibrary, WinUser {
  public static final User32 INSTANCE = (User32)Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
  
  WinDef.HDC GetDC(WinDef.HWND paramHWND);
  
  int ReleaseDC(WinDef.HWND paramHWND, WinDef.HDC paramHDC);
  
  WinDef.HWND FindWindow(String paramString1, String paramString2);
  
  int GetClassName(WinDef.HWND paramHWND, char[] paramArrayOfchar, int paramInt);
  
  boolean GetGUIThreadInfo(int paramInt, WinUser.GUITHREADINFO paramGUITHREADINFO);
  
  boolean GetWindowInfo(WinDef.HWND paramHWND, WinUser.WINDOWINFO paramWINDOWINFO);
  
  boolean GetWindowRect(WinDef.HWND paramHWND, WinDef.RECT paramRECT);
  
  int GetWindowText(WinDef.HWND paramHWND, char[] paramArrayOfchar, int paramInt);
  
  int GetWindowTextLength(WinDef.HWND paramHWND);
  
  int GetWindowModuleFileName(WinDef.HWND paramHWND, char[] paramArrayOfchar, int paramInt);
  
  int GetWindowThreadProcessId(WinDef.HWND paramHWND, IntByReference paramIntByReference);
  
  boolean EnumWindows(WinUser.WNDENUMPROC paramWNDENUMPROC, Pointer paramPointer);
  
  boolean EnumChildWindows(WinDef.HWND paramHWND, WinUser.WNDENUMPROC paramWNDENUMPROC, Pointer paramPointer);
  
  boolean EnumThreadWindows(int paramInt, WinUser.WNDENUMPROC paramWNDENUMPROC, Pointer paramPointer);
  
  boolean FlashWindowEx(WinUser.FLASHWINFO paramFLASHWINFO);
  
  WinDef.HICON LoadIcon(WinDef.HINSTANCE paramHINSTANCE, String paramString);
  
  WinNT.HANDLE LoadImage(WinDef.HINSTANCE paramHINSTANCE, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  boolean DestroyIcon(WinDef.HICON paramHICON);
  
  int GetWindowLong(WinDef.HWND paramHWND, int paramInt);
  
  int SetWindowLong(WinDef.HWND paramHWND, int paramInt1, int paramInt2);
  
  Pointer SetWindowLong(WinDef.HWND paramHWND, int paramInt, Pointer paramPointer);
  
  BaseTSD.LONG_PTR GetWindowLongPtr(WinDef.HWND paramHWND, int paramInt);
  
  BaseTSD.LONG_PTR SetWindowLongPtr(WinDef.HWND paramHWND, int paramInt, BaseTSD.LONG_PTR paramLONG_PTR);
  
  Pointer SetWindowLongPtr(WinDef.HWND paramHWND, int paramInt, Pointer paramPointer);
  
  boolean SetLayeredWindowAttributes(WinDef.HWND paramHWND, int paramInt1, byte paramByte, int paramInt2);
  
  boolean GetLayeredWindowAttributes(WinDef.HWND paramHWND, IntByReference paramIntByReference1, ByteByReference paramByteByReference, IntByReference paramIntByReference2);
  
  boolean UpdateLayeredWindow(WinDef.HWND paramHWND, WinDef.HDC paramHDC1, WinUser.POINT paramPOINT1, WinUser.SIZE paramSIZE, WinDef.HDC paramHDC2, WinUser.POINT paramPOINT2, int paramInt1, WinUser.BLENDFUNCTION paramBLENDFUNCTION, int paramInt2);
  
  int SetWindowRgn(WinDef.HWND paramHWND, WinDef.HRGN paramHRGN, boolean paramBoolean);
  
  boolean GetKeyboardState(byte[] paramArrayOfbyte);
  
  short GetAsyncKeyState(int paramInt);
  
  WinUser.HHOOK SetWindowsHookEx(int paramInt1, WinUser.HOOKPROC paramHOOKPROC, WinDef.HINSTANCE paramHINSTANCE, int paramInt2);
  
  WinDef.LRESULT CallNextHookEx(WinUser.HHOOK paramHHOOK, int paramInt, WinDef.WPARAM paramWPARAM, WinDef.LPARAM paramLPARAM);
  
  WinDef.LRESULT CallNextHookEx(WinUser.HHOOK paramHHOOK, int paramInt, WinDef.WPARAM paramWPARAM, Pointer paramPointer);
  
  boolean UnhookWindowsHookEx(WinUser.HHOOK paramHHOOK);
  
  int GetMessage(WinUser.MSG paramMSG, WinDef.HWND paramHWND, int paramInt1, int paramInt2);
  
  boolean PeekMessage(WinUser.MSG paramMSG, WinDef.HWND paramHWND, int paramInt1, int paramInt2, int paramInt3);
  
  boolean TranslateMessage(WinUser.MSG paramMSG);
  
  WinDef.LRESULT DispatchMessage(WinUser.MSG paramMSG);
  
  void PostMessage(WinDef.HWND paramHWND, int paramInt, WinDef.WPARAM paramWPARAM, WinDef.LPARAM paramLPARAM);
  
  void PostQuitMessage(int paramInt);
  
  int GetSystemMetrics(int paramInt);
  
  WinDef.HWND SetParent(WinDef.HWND paramHWND1, WinDef.HWND paramHWND2);
  
  boolean IsWindowVisible(WinDef.HWND paramHWND);
  
  boolean MoveWindow(WinDef.HWND paramHWND, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  
  boolean SetWindowPos(WinDef.HWND paramHWND1, WinDef.HWND paramHWND2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  boolean AttachThreadInput(WinDef.DWORD paramDWORD1, WinDef.DWORD paramDWORD2, boolean paramBoolean);
  
  boolean SetForegroundWindow(WinDef.HWND paramHWND);
  
  WinDef.HWND GetForegroundWindow();
  
  WinDef.HWND SetFocus(WinDef.HWND paramHWND);
  
  WinDef.DWORD SendInput(WinDef.DWORD paramDWORD, WinUser.INPUT[] paramArrayOfINPUT, int paramInt);
  
  WinDef.DWORD WaitForInputIdle(WinNT.HANDLE paramHANDLE, WinDef.DWORD paramDWORD);
  
  boolean InvalidateRect(WinDef.HWND paramHWND, Structure.ByReference paramByReference, boolean paramBoolean);
  
  boolean RedrawWindow(WinDef.HWND paramHWND, Structure.ByReference paramByReference, WinDef.HRGN paramHRGN, WinDef.DWORD paramDWORD);
  
  WinDef.HWND GetWindow(WinDef.HWND paramHWND, WinDef.DWORD paramDWORD);
  
  boolean UpdateWindow(WinDef.HWND paramHWND);
  
  boolean ShowWindow(WinDef.HWND paramHWND, int paramInt);
  
  boolean CloseWindow(WinDef.HWND paramHWND);
  
  boolean RegisterHotKey(WinDef.HWND paramHWND, int paramInt1, int paramInt2, int paramInt3);
  
  boolean UnregisterHotKey(Pointer paramPointer, int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\User32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */