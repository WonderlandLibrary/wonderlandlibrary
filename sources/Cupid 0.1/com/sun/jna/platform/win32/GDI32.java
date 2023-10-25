package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface GDI32 extends StdCallLibrary {
  public static final GDI32 INSTANCE = (GDI32)Native.loadLibrary("gdi32", GDI32.class, W32APIOptions.DEFAULT_OPTIONS);
  
  WinDef.HRGN ExtCreateRegion(Pointer paramPointer, int paramInt, WinGDI.RGNDATA paramRGNDATA);
  
  int CombineRgn(WinDef.HRGN paramHRGN1, WinDef.HRGN paramHRGN2, WinDef.HRGN paramHRGN3, int paramInt);
  
  WinDef.HRGN CreateRectRgn(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  WinDef.HRGN CreateRoundRectRgn(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  WinDef.HRGN CreatePolyPolygonRgn(WinUser.POINT[] paramArrayOfPOINT, int[] paramArrayOfint, int paramInt1, int paramInt2);
  
  boolean SetRectRgn(WinDef.HRGN paramHRGN, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  int SetPixel(WinDef.HDC paramHDC, int paramInt1, int paramInt2, int paramInt3);
  
  WinDef.HDC CreateCompatibleDC(WinDef.HDC paramHDC);
  
  boolean DeleteDC(WinDef.HDC paramHDC);
  
  WinDef.HBITMAP CreateDIBitmap(WinDef.HDC paramHDC, WinGDI.BITMAPINFOHEADER paramBITMAPINFOHEADER, int paramInt1, Pointer paramPointer, WinGDI.BITMAPINFO paramBITMAPINFO, int paramInt2);
  
  WinDef.HBITMAP CreateDIBSection(WinDef.HDC paramHDC, WinGDI.BITMAPINFO paramBITMAPINFO, int paramInt1, PointerByReference paramPointerByReference, Pointer paramPointer, int paramInt2);
  
  WinDef.HBITMAP CreateCompatibleBitmap(WinDef.HDC paramHDC, int paramInt1, int paramInt2);
  
  WinNT.HANDLE SelectObject(WinDef.HDC paramHDC, WinNT.HANDLE paramHANDLE);
  
  boolean DeleteObject(WinNT.HANDLE paramHANDLE);
  
  int GetDeviceCaps(WinDef.HDC paramHDC, int paramInt);
  
  int GetDIBits(WinDef.HDC paramHDC, WinDef.HBITMAP paramHBITMAP, int paramInt1, int paramInt2, Pointer paramPointer, WinGDI.BITMAPINFO paramBITMAPINFO, int paramInt3);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\GDI32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */