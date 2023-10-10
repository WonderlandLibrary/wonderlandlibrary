// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.ptr.ByReference;
import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.IntegerType;

public interface BaseTSD
{
    public static class LONG_PTR extends IntegerType
    {
        public LONG_PTR() {
            this(0L);
        }
        
        public LONG_PTR(final long value) {
            super(Native.POINTER_SIZE, value);
        }
        
        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }
    
    public static class SSIZE_T extends LONG_PTR
    {
        public SSIZE_T() {
            this(0L);
        }
        
        public SSIZE_T(final long value) {
            super(value);
        }
    }
    
    public static class ULONG_PTR extends IntegerType
    {
        public ULONG_PTR() {
            this(0L);
        }
        
        public ULONG_PTR(final long value) {
            super(Native.POINTER_SIZE, value, true);
        }
        
        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }
    
    public static class ULONG_PTRByReference extends ByReference
    {
        public ULONG_PTRByReference() {
            this(new ULONG_PTR(0L));
        }
        
        public ULONG_PTRByReference(final ULONG_PTR value) {
            super(Native.POINTER_SIZE);
            this.setValue(value);
        }
        
        public void setValue(final ULONG_PTR value) {
            if (Native.POINTER_SIZE == 4) {
                this.getPointer().setInt(0L, value.intValue());
            }
            else {
                this.getPointer().setLong(0L, value.longValue());
            }
        }
        
        public ULONG_PTR getValue() {
            return new ULONG_PTR((Native.POINTER_SIZE == 4) ? this.getPointer().getInt(0L) : this.getPointer().getLong(0L));
        }
    }
    
    public static class DWORD_PTR extends IntegerType
    {
        public DWORD_PTR() {
            this(0L);
        }
        
        public DWORD_PTR(final long value) {
            super(Native.POINTER_SIZE, value);
        }
    }
    
    public static class SIZE_T extends ULONG_PTR
    {
        public SIZE_T() {
            this(0L);
        }
        
        public SIZE_T(final long value) {
            super(value);
        }
    }
}
