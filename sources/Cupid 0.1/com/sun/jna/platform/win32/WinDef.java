package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;
import java.awt.Rectangle;

public interface WinDef extends StdCallLibrary {
  public static final int MAX_PATH = 260;
  
  public static class WORD extends IntegerType {
    public WORD() {
      this(0L);
    }
    
    public WORD(long value) {
      super(2, value);
    }
  }
  
  public static class DWORD extends IntegerType {
    public DWORD() {
      this(0L);
    }
    
    public DWORD(long value) {
      super(4, value, true);
    }
    
    public WinDef.WORD getLow() {
      return new WinDef.WORD(longValue() & 0xFFL);
    }
    
    public WinDef.WORD getHigh() {
      return new WinDef.WORD(longValue() >> 16L & 0xFFL);
    }
  }
  
  public static class LONG extends IntegerType {
    public LONG() {
      this(0L);
    }
    
    public LONG(long value) {
      super(Native.LONG_SIZE, value);
    }
  }
  
  public static class HDC extends WinNT.HANDLE {
    public HDC() {}
    
    public HDC(Pointer p) {
      super(p);
    }
  }
  
  public static class HICON extends WinNT.HANDLE {
    public HICON() {}
    
    public HICON(Pointer p) {
      super(p);
    }
  }
  
  public static class HCURSOR extends HICON {
    public HCURSOR() {}
    
    public HCURSOR(Pointer p) {
      super(p);
    }
  }
  
  public static class HMENU extends WinNT.HANDLE {
    public HMENU() {}
    
    public HMENU(Pointer p) {
      super(p);
    }
  }
  
  public static class HPEN extends WinNT.HANDLE {
    public HPEN() {}
    
    public HPEN(Pointer p) {
      super(p);
    }
  }
  
  public static class HRSRC extends WinNT.HANDLE {
    public HRSRC() {}
    
    public HRSRC(Pointer p) {
      super(p);
    }
  }
  
  public static class HPALETTE extends WinNT.HANDLE {
    public HPALETTE() {}
    
    public HPALETTE(Pointer p) {
      super(p);
    }
  }
  
  public static class HBITMAP extends WinNT.HANDLE {
    public HBITMAP() {}
    
    public HBITMAP(Pointer p) {
      super(p);
    }
  }
  
  public static class HRGN extends WinNT.HANDLE {
    public HRGN() {}
    
    public HRGN(Pointer p) {
      super(p);
    }
  }
  
  public static class HWND extends WinNT.HANDLE {
    public HWND() {}
    
    public HWND(Pointer p) {
      super(p);
    }
  }
  
  public static class HINSTANCE extends WinNT.HANDLE {}
  
  public static class HMODULE extends HINSTANCE {}
  
  public static class HFONT extends WinNT.HANDLE {
    public HFONT() {}
    
    public HFONT(Pointer p) {
      super(p);
    }
  }
  
  public static class LPARAM extends BaseTSD.LONG_PTR {
    public LPARAM() {
      this(0L);
    }
    
    public LPARAM(long value) {
      super(value);
    }
  }
  
  public static class LRESULT extends BaseTSD.LONG_PTR {
    public LRESULT() {
      this(0L);
    }
    
    public LRESULT(long value) {
      super(value);
    }
  }
  
  public static class UINT_PTR extends IntegerType {
    public UINT_PTR() {
      super(Pointer.SIZE);
    }
    
    public UINT_PTR(long value) {
      super(Pointer.SIZE, value);
    }
    
    public Pointer toPointer() {
      return Pointer.createConstant(longValue());
    }
  }
  
  public static class WPARAM extends UINT_PTR {
    public WPARAM() {
      this(0L);
    }
    
    public WPARAM(long value) {
      super(value);
    }
  }
  
  public static class RECT extends Structure {
    public int left;
    
    public int top;
    
    public int right;
    
    public int bottom;
    
    public Rectangle toRectangle() {
      return new Rectangle(this.left, this.top, this.right - this.left, this.bottom - this.top);
    }
    
    public String toString() {
      return "[(" + this.left + "," + this.top + ")(" + this.right + "," + this.bottom + ")]";
    }
  }
  
  public static class ULONGLONG extends IntegerType {
    public ULONGLONG() {
      this(0L);
    }
    
    public ULONGLONG(long value) {
      super(8, value);
    }
  }
  
  public static class DWORDLONG extends IntegerType {
    public DWORDLONG() {
      this(0L);
    }
    
    public DWORDLONG(long value) {
      super(8, value);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\WinDef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */