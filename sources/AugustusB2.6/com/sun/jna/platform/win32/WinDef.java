// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.PointerType;
import java.awt.Rectangle;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.IntegerType;

public interface WinDef
{
    public static final int MAX_PATH = 260;
    
    public static class WORD extends IntegerType implements Comparable<WORD>
    {
        public static final int SIZE = 2;
        
        public WORD() {
            this(0L);
        }
        
        public WORD(final long value) {
            super(2, value, true);
        }
        
        @Override
        public int compareTo(final WORD other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class WORDByReference extends ByReference
    {
        public WORDByReference() {
            this(new WORD(0L));
        }
        
        public WORDByReference(final WORD value) {
            super(2);
            this.setValue(value);
        }
        
        public void setValue(final WORD value) {
            this.getPointer().setShort(0L, value.shortValue());
        }
        
        public WORD getValue() {
            return new WORD((long)this.getPointer().getShort(0L));
        }
    }
    
    public static class DWORD extends IntegerType implements Comparable<DWORD>
    {
        public static final int SIZE = 4;
        
        public DWORD() {
            this(0L);
        }
        
        public DWORD(final long value) {
            super(4, value, true);
        }
        
        public WORD getLow() {
            return new WORD(this.longValue() & 0xFFFFL);
        }
        
        public WORD getHigh() {
            return new WORD(this.longValue() >> 16 & 0xFFFFL);
        }
        
        @Override
        public int compareTo(final DWORD other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class DWORDByReference extends ByReference
    {
        public DWORDByReference() {
            this(new DWORD(0L));
        }
        
        public DWORDByReference(final DWORD value) {
            super(4);
            this.setValue(value);
        }
        
        public void setValue(final DWORD value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public DWORD getValue() {
            return new DWORD((long)this.getPointer().getInt(0L));
        }
    }
    
    public static class LONG extends IntegerType implements Comparable<LONG>
    {
        public static final int SIZE;
        
        public LONG() {
            this(0L);
        }
        
        public LONG(final long value) {
            super(LONG.SIZE, value);
        }
        
        @Override
        public int compareTo(final LONG other) {
            return IntegerType.compare(this, other);
        }
        
        static {
            SIZE = Native.LONG_SIZE;
        }
    }
    
    public static class LONGByReference extends ByReference
    {
        public LONGByReference() {
            this(new LONG(0L));
        }
        
        public LONGByReference(final LONG value) {
            super(LONG.SIZE);
            this.setValue(value);
        }
        
        public void setValue(final LONG value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public LONG getValue() {
            return new LONG((long)this.getPointer().getInt(0L));
        }
    }
    
    public static class LONGLONG extends IntegerType implements Comparable<LONGLONG>
    {
        public static final int SIZE;
        
        public LONGLONG() {
            this(0L);
        }
        
        public LONGLONG(final long value) {
            super(8, value, false);
        }
        
        @Override
        public int compareTo(final LONGLONG other) {
            return IntegerType.compare(this, other);
        }
        
        static {
            SIZE = Native.LONG_SIZE * 2;
        }
    }
    
    public static class LONGLONGByReference extends ByReference
    {
        public LONGLONGByReference() {
            this(new LONGLONG(0L));
        }
        
        public LONGLONGByReference(final LONGLONG value) {
            super(LONGLONG.SIZE);
            this.setValue(value);
        }
        
        public void setValue(final LONGLONG value) {
            this.getPointer().setLong(0L, value.longValue());
        }
        
        public LONGLONG getValue() {
            return new LONGLONG(this.getPointer().getLong(0L));
        }
    }
    
    public static class HDC extends WinNT.HANDLE
    {
        public HDC() {
        }
        
        public HDC(final Pointer p) {
            super(p);
        }
    }
    
    public static class HICON extends WinNT.HANDLE
    {
        public HICON() {
        }
        
        public HICON(final WinNT.HANDLE handle) {
            this(handle.getPointer());
        }
        
        public HICON(final Pointer p) {
            super(p);
        }
    }
    
    public static class HCURSOR extends HICON
    {
        public HCURSOR() {
        }
        
        public HCURSOR(final Pointer p) {
            super(p);
        }
    }
    
    public static class HMENU extends WinNT.HANDLE
    {
        public HMENU() {
        }
        
        public HMENU(final Pointer p) {
            super(p);
        }
    }
    
    public static class HPEN extends WinNT.HANDLE
    {
        public HPEN() {
        }
        
        public HPEN(final Pointer p) {
            super(p);
        }
    }
    
    public static class HRSRC extends WinNT.HANDLE
    {
        public HRSRC() {
        }
        
        public HRSRC(final Pointer p) {
            super(p);
        }
    }
    
    public static class HPALETTE extends WinNT.HANDLE
    {
        public HPALETTE() {
        }
        
        public HPALETTE(final Pointer p) {
            super(p);
        }
    }
    
    public static class HBITMAP extends WinNT.HANDLE
    {
        public HBITMAP() {
        }
        
        public HBITMAP(final Pointer p) {
            super(p);
        }
    }
    
    public static class HRGN extends WinNT.HANDLE
    {
        public HRGN() {
        }
        
        public HRGN(final Pointer p) {
            super(p);
        }
    }
    
    public static class HWND extends WinNT.HANDLE
    {
        public HWND() {
        }
        
        public HWND(final Pointer p) {
            super(p);
        }
    }
    
    public static class HINSTANCE extends WinNT.HANDLE
    {
    }
    
    public static class HMODULE extends HINSTANCE
    {
    }
    
    public static class HFONT extends WinNT.HANDLE
    {
        public HFONT() {
        }
        
        public HFONT(final Pointer p) {
            super(p);
        }
    }
    
    public static class HKL extends WinNT.HANDLE
    {
        public HKL() {
        }
        
        public HKL(final Pointer p) {
            super(p);
        }
        
        public HKL(final int i) {
            super(Pointer.createConstant(i));
        }
        
        public int getLanguageIdentifier() {
            return (int)(Pointer.nativeValue(this.getPointer()) & 0xFFFFL);
        }
        
        public int getDeviceHandle() {
            return (int)(Pointer.nativeValue(this.getPointer()) >> 16 & 0xFFFFL);
        }
        
        @Override
        public String toString() {
            return String.format("%08x", Pointer.nativeValue(this.getPointer()));
        }
    }
    
    public static class LPARAM extends BaseTSD.LONG_PTR
    {
        public LPARAM() {
            this(0L);
        }
        
        public LPARAM(final long value) {
            super(value);
        }
    }
    
    public static class LRESULT extends BaseTSD.LONG_PTR
    {
        public LRESULT() {
            this(0L);
        }
        
        public LRESULT(final long value) {
            super(value);
        }
    }
    
    public static class INT_PTR extends IntegerType
    {
        public INT_PTR() {
            super(Native.POINTER_SIZE);
        }
        
        public INT_PTR(final long value) {
            super(Native.POINTER_SIZE, value);
        }
        
        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }
    
    public static class UINT_PTR extends IntegerType
    {
        public UINT_PTR() {
            super(Native.POINTER_SIZE);
        }
        
        public UINT_PTR(final long value) {
            super(Native.POINTER_SIZE, value, true);
        }
        
        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }
    
    public static class WPARAM extends UINT_PTR
    {
        public WPARAM() {
            this(0L);
        }
        
        public WPARAM(final long value) {
            super(value);
        }
    }
    
    @FieldOrder({ "left", "top", "right", "bottom" })
    public static class RECT extends Structure
    {
        public int left;
        public int top;
        public int right;
        public int bottom;
        
        public Rectangle toRectangle() {
            return new Rectangle(this.left, this.top, this.right - this.left, this.bottom - this.top);
        }
        
        @Override
        public String toString() {
            return "[(" + this.left + "," + this.top + ")(" + this.right + "," + this.bottom + ")]";
        }
    }
    
    public static class ULONG extends IntegerType implements Comparable<ULONG>
    {
        public static final int SIZE;
        
        public ULONG() {
            this(0L);
        }
        
        public ULONG(final long value) {
            super(ULONG.SIZE, value, true);
        }
        
        @Override
        public int compareTo(final ULONG other) {
            return IntegerType.compare(this, other);
        }
        
        static {
            SIZE = Native.LONG_SIZE;
        }
    }
    
    public static class ULONGByReference extends ByReference
    {
        public ULONGByReference() {
            this(new ULONG(0L));
        }
        
        public ULONGByReference(final ULONG value) {
            super(ULONG.SIZE);
            this.setValue(value);
        }
        
        public void setValue(final ULONG value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public ULONG getValue() {
            return new ULONG((long)this.getPointer().getInt(0L));
        }
    }
    
    public static class ULONGLONG extends IntegerType implements Comparable<ULONGLONG>
    {
        public static final int SIZE = 8;
        
        public ULONGLONG() {
            this(0L);
        }
        
        public ULONGLONG(final long value) {
            super(8, value, true);
        }
        
        @Override
        public int compareTo(final ULONGLONG other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class ULONGLONGByReference extends ByReference
    {
        public ULONGLONGByReference() {
            this(new ULONGLONG(0L));
        }
        
        public ULONGLONGByReference(final ULONGLONG value) {
            super(8);
            this.setValue(value);
        }
        
        public void setValue(final ULONGLONG value) {
            this.getPointer().setLong(0L, value.longValue());
        }
        
        public ULONGLONG getValue() {
            return new ULONGLONG(this.getPointer().getLong(0L));
        }
    }
    
    public static class DWORDLONG extends IntegerType implements Comparable<DWORDLONG>
    {
        public static final int SIZE = 8;
        
        public DWORDLONG() {
            this(0L);
        }
        
        public DWORDLONG(final long value) {
            super(8, value, true);
        }
        
        @Override
        public int compareTo(final DWORDLONG other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class HBRUSH extends WinNT.HANDLE
    {
        public HBRUSH() {
        }
        
        public HBRUSH(final Pointer p) {
            super(p);
        }
    }
    
    public static class ATOM extends WORD
    {
        public ATOM() {
            this(0L);
        }
        
        public ATOM(final long value) {
            super(value);
        }
    }
    
    public static class PVOID extends PointerType
    {
        public PVOID() {
        }
        
        public PVOID(final Pointer pointer) {
            super(pointer);
        }
    }
    
    public static class LPVOID extends PointerType
    {
        public LPVOID() {
        }
        
        public LPVOID(final Pointer p) {
            super(p);
        }
    }
    
    @FieldOrder({ "x", "y" })
    public static class POINT extends Structure
    {
        public int x;
        public int y;
        
        public POINT() {
        }
        
        public POINT(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public POINT(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        public static class ByReference extends POINT implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
            
            public ByReference(final int x, final int y) {
                super(x, y);
            }
        }
        
        public static class ByValue extends POINT implements Structure.ByValue
        {
            public ByValue() {
            }
            
            public ByValue(final Pointer memory) {
                super(memory);
            }
            
            public ByValue(final int x, final int y) {
                super(x, y);
            }
        }
    }
    
    public static class USHORT extends IntegerType implements Comparable<USHORT>
    {
        public static final int SIZE = 2;
        
        public USHORT() {
            this(0L);
        }
        
        public USHORT(final long value) {
            super(2, value, true);
        }
        
        @Override
        public int compareTo(final USHORT other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class USHORTByReference extends ByReference
    {
        public USHORTByReference() {
            this(new USHORT(0L));
        }
        
        public USHORTByReference(final USHORT value) {
            super(2);
            this.setValue(value);
        }
        
        public USHORTByReference(final short value) {
            super(2);
            this.setValue(new USHORT((long)value));
        }
        
        public void setValue(final USHORT value) {
            this.getPointer().setShort(0L, value.shortValue());
        }
        
        public USHORT getValue() {
            return new USHORT((long)this.getPointer().getShort(0L));
        }
    }
    
    public static class SHORT extends IntegerType implements Comparable<SHORT>
    {
        public static final int SIZE = 2;
        
        public SHORT() {
            this(0L);
        }
        
        public SHORT(final long value) {
            super(2, value, false);
        }
        
        @Override
        public int compareTo(final SHORT other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class UINT extends IntegerType implements Comparable<UINT>
    {
        public static final int SIZE = 4;
        
        public UINT() {
            this(0L);
        }
        
        public UINT(final long value) {
            super(4, value, true);
        }
        
        @Override
        public int compareTo(final UINT other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class UINTByReference extends ByReference
    {
        public UINTByReference() {
            this(new UINT(0L));
        }
        
        public UINTByReference(final UINT value) {
            super(4);
            this.setValue(value);
        }
        
        public void setValue(final UINT value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public UINT getValue() {
            return new UINT((long)this.getPointer().getInt(0L));
        }
    }
    
    public static class SCODE extends ULONG
    {
        public SCODE() {
            this(0L);
        }
        
        public SCODE(final long value) {
            super(value);
        }
    }
    
    public static class SCODEByReference extends ByReference
    {
        public SCODEByReference() {
            this(new SCODE(0L));
        }
        
        public SCODEByReference(final SCODE value) {
            super(SCODE.SIZE);
            this.setValue(value);
        }
        
        public void setValue(final SCODE value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public SCODE getValue() {
            return new SCODE((long)this.getPointer().getInt(0L));
        }
    }
    
    public static class LCID extends DWORD
    {
        public LCID() {
            super(0L);
        }
        
        public LCID(final long value) {
            super(value);
        }
    }
    
    public static class BOOL extends IntegerType implements Comparable<BOOL>
    {
        public static final int SIZE = 4;
        
        public BOOL() {
            this(0L);
        }
        
        public BOOL(final boolean value) {
            this((long)(value ? 1 : 0));
        }
        
        public BOOL(final long value) {
            super(4, value, false);
            assert value == 1L;
        }
        
        public boolean booleanValue() {
            return this.intValue() > 0;
        }
        
        @Override
        public String toString() {
            return Boolean.toString(this.booleanValue());
        }
        
        @Override
        public int compareTo(final BOOL other) {
            return compare(this, other);
        }
        
        public static int compare(final BOOL v1, final BOOL v2) {
            if (v1 == v2) {
                return 0;
            }
            if (v1 == null) {
                return 1;
            }
            if (v2 == null) {
                return -1;
            }
            return compare(v1.booleanValue(), v2.booleanValue());
        }
        
        public static int compare(final BOOL v1, final boolean v2) {
            if (v1 == null) {
                return 1;
            }
            return compare(v1.booleanValue(), v2);
        }
        
        public static int compare(final boolean v1, final boolean v2) {
            if (v1 == v2) {
                return 0;
            }
            if (v1) {
                return 1;
            }
            return -1;
        }
    }
    
    public static class BOOLByReference extends ByReference
    {
        public BOOLByReference() {
            this(new BOOL(0L));
        }
        
        public BOOLByReference(final BOOL value) {
            super(4);
            this.setValue(value);
        }
        
        public void setValue(final BOOL value) {
            this.getPointer().setInt(0L, value.intValue());
        }
        
        public BOOL getValue() {
            return new BOOL((long)this.getPointer().getInt(0L));
        }
    }
    
    public static class UCHAR extends IntegerType implements Comparable<UCHAR>
    {
        public static final int SIZE = 1;
        
        public UCHAR() {
            this(0L);
        }
        
        public UCHAR(final char ch) {
            this((long)(ch & '\u00ff'));
        }
        
        public UCHAR(final long value) {
            super(1, value, true);
        }
        
        @Override
        public int compareTo(final UCHAR other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class BYTE extends UCHAR
    {
        public BYTE() {
            this(0L);
        }
        
        public BYTE(final long value) {
            super(value);
        }
    }
    
    public static class CHAR extends IntegerType implements Comparable<CHAR>
    {
        public static final int SIZE = 1;
        
        public CHAR() {
            this(0L);
        }
        
        public CHAR(final byte ch) {
            this((long)(ch & 0xFF));
        }
        
        public CHAR(final long value) {
            super(1, value, false);
        }
        
        @Override
        public int compareTo(final CHAR other) {
            return IntegerType.compare(this, other);
        }
    }
    
    public static class CHARByReference extends ByReference
    {
        public CHARByReference() {
            this(new CHAR(0L));
        }
        
        public CHARByReference(final CHAR value) {
            super(1);
            this.setValue(value);
        }
        
        public void setValue(final CHAR value) {
            this.getPointer().setByte(0L, value.byteValue());
        }
        
        public CHAR getValue() {
            return new CHAR(this.getPointer().getByte(0L));
        }
    }
    
    public static class HGLRC extends WinNT.HANDLE
    {
        public HGLRC() {
        }
        
        public HGLRC(final Pointer p) {
            super(p);
        }
    }
    
    public static class HGLRCByReference extends WinNT.HANDLEByReference
    {
        public HGLRCByReference() {
        }
        
        public HGLRCByReference(final HGLRC h) {
            super(h);
        }
    }
}
