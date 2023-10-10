// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import com.sun.jna.NativeLong;
import com.sun.jna.Memory;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.ShortByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.PointerType;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.Library;

public interface CoreFoundation extends Library
{
    public static final CoreFoundation INSTANCE = Native.load("CoreFoundation", CoreFoundation.class);
    public static final int kCFNotFound = -1;
    public static final int kCFStringEncodingASCII = 1536;
    public static final int kCFStringEncodingUTF8 = 134217984;
    public static final CFTypeID ARRAY_TYPE_ID = CoreFoundation.INSTANCE.CFArrayGetTypeID();
    public static final CFTypeID BOOLEAN_TYPE_ID = CoreFoundation.INSTANCE.CFBooleanGetTypeID();
    public static final CFTypeID DATA_TYPE_ID = CoreFoundation.INSTANCE.CFDataGetTypeID();
    public static final CFTypeID DATE_TYPE_ID = CoreFoundation.INSTANCE.CFDateGetTypeID();
    public static final CFTypeID DICTIONARY_TYPE_ID = CoreFoundation.INSTANCE.CFDictionaryGetTypeID();
    public static final CFTypeID NUMBER_TYPE_ID = CoreFoundation.INSTANCE.CFNumberGetTypeID();
    public static final CFTypeID STRING_TYPE_ID = CoreFoundation.INSTANCE.CFStringGetTypeID();
    
    CFStringRef CFStringCreateWithCharacters(final CFAllocatorRef p0, final char[] p1, final CFIndex p2);
    
    CFNumberRef CFNumberCreate(final CFAllocatorRef p0, final CFIndex p1, final ByReference p2);
    
    CFArrayRef CFArrayCreate(final CFAllocatorRef p0, final Pointer p1, final CFIndex p2, final Pointer p3);
    
    CFDataRef CFDataCreate(final CFAllocatorRef p0, final Pointer p1, final CFIndex p2);
    
    CFMutableDictionaryRef CFDictionaryCreateMutable(final CFAllocatorRef p0, final CFIndex p1, final Pointer p2, final Pointer p3);
    
    CFStringRef CFCopyDescription(final CFTypeRef p0);
    
    void CFRelease(final CFTypeRef p0);
    
    CFTypeRef CFRetain(final CFTypeRef p0);
    
    CFIndex CFGetRetainCount(final CFTypeRef p0);
    
    Pointer CFDictionaryGetValue(final CFDictionaryRef p0, final PointerType p1);
    
    byte CFDictionaryGetValueIfPresent(final CFDictionaryRef p0, final PointerType p1, final PointerByReference p2);
    
    void CFDictionarySetValue(final CFMutableDictionaryRef p0, final PointerType p1, final PointerType p2);
    
    byte CFStringGetCString(final CFStringRef p0, final Pointer p1, final CFIndex p2, final int p3);
    
    byte CFBooleanGetValue(final CFBooleanRef p0);
    
    CFIndex CFArrayGetCount(final CFArrayRef p0);
    
    Pointer CFArrayGetValueAtIndex(final CFArrayRef p0, final CFIndex p1);
    
    CFIndex CFNumberGetType(final CFNumberRef p0);
    
    byte CFNumberGetValue(final CFNumberRef p0, final CFIndex p1, final ByReference p2);
    
    CFIndex CFStringGetLength(final CFStringRef p0);
    
    CFIndex CFStringGetMaximumSizeForEncoding(final CFIndex p0, final int p1);
    
    CFAllocatorRef CFAllocatorGetDefault();
    
    CFIndex CFDataGetLength(final CFDataRef p0);
    
    Pointer CFDataGetBytePtr(final CFDataRef p0);
    
    CFTypeID CFGetTypeID(final CFTypeRef p0);
    
    CFTypeID CFArrayGetTypeID();
    
    CFTypeID CFBooleanGetTypeID();
    
    CFTypeID CFDateGetTypeID();
    
    CFTypeID CFDataGetTypeID();
    
    CFTypeID CFDictionaryGetTypeID();
    
    CFTypeID CFNumberGetTypeID();
    
    CFTypeID CFStringGetTypeID();
    
    public static class CFTypeRef extends PointerType
    {
        public CFTypeRef() {
        }
        
        public CFTypeRef(final Pointer p) {
            super(p);
        }
        
        public CFTypeID getTypeID() {
            if (this.getPointer() == null) {
                return new CFTypeID(0L);
            }
            return CoreFoundation.INSTANCE.CFGetTypeID(this);
        }
        
        public boolean isTypeID(final CFTypeID typeID) {
            return this.getTypeID().equals(typeID);
        }
        
        public void retain() {
            CoreFoundation.INSTANCE.CFRetain(this);
        }
        
        public void release() {
            CoreFoundation.INSTANCE.CFRelease(this);
        }
    }
    
    public static class CFAllocatorRef extends CFTypeRef
    {
    }
    
    public static class CFNumberRef extends CFTypeRef
    {
        public CFNumberRef() {
        }
        
        public CFNumberRef(final Pointer p) {
            super(p);
            if (!this.isTypeID(CoreFoundation.NUMBER_TYPE_ID)) {
                throw new ClassCastException("Unable to cast to CFNumber. Type ID: " + this.getTypeID());
            }
        }
        
        public long longValue() {
            final LongByReference lbr = new LongByReference();
            CoreFoundation.INSTANCE.CFNumberGetValue(this, CFNumberType.kCFNumberLongLongType.typeIndex(), lbr);
            return lbr.getValue();
        }
        
        public int intValue() {
            final IntByReference ibr = new IntByReference();
            CoreFoundation.INSTANCE.CFNumberGetValue(this, CFNumberType.kCFNumberIntType.typeIndex(), ibr);
            return ibr.getValue();
        }
        
        public short shortValue() {
            final ShortByReference sbr = new ShortByReference();
            CoreFoundation.INSTANCE.CFNumberGetValue(this, CFNumberType.kCFNumberShortType.typeIndex(), sbr);
            return sbr.getValue();
        }
        
        public byte byteValue() {
            final ByteByReference bbr = new ByteByReference();
            CoreFoundation.INSTANCE.CFNumberGetValue(this, CFNumberType.kCFNumberCharType.typeIndex(), bbr);
            return bbr.getValue();
        }
        
        public double doubleValue() {
            final DoubleByReference dbr = new DoubleByReference();
            CoreFoundation.INSTANCE.CFNumberGetValue(this, CFNumberType.kCFNumberDoubleType.typeIndex(), dbr);
            return dbr.getValue();
        }
        
        public float floatValue() {
            final FloatByReference fbr = new FloatByReference();
            CoreFoundation.INSTANCE.CFNumberGetValue(this, CFNumberType.kCFNumberFloatType.typeIndex(), fbr);
            return fbr.getValue();
        }
    }
    
    public enum CFNumberType
    {
        unusedZero, 
        kCFNumberSInt8Type, 
        kCFNumberSInt16Type, 
        kCFNumberSInt32Type, 
        kCFNumberSInt64Type, 
        kCFNumberFloat32Type, 
        kCFNumberFloat64Type, 
        kCFNumberCharType, 
        kCFNumberShortType, 
        kCFNumberIntType, 
        kCFNumberLongType, 
        kCFNumberLongLongType, 
        kCFNumberFloatType, 
        kCFNumberDoubleType, 
        kCFNumberCFIndexType, 
        kCFNumberNSIntegerType, 
        kCFNumberCGFloatType, 
        kCFNumberMaxType;
        
        public CFIndex typeIndex() {
            return new CFIndex((long)this.ordinal());
        }
    }
    
    public static class CFBooleanRef extends CFTypeRef
    {
        public CFBooleanRef() {
        }
        
        public CFBooleanRef(final Pointer p) {
            super(p);
            if (!this.isTypeID(CoreFoundation.BOOLEAN_TYPE_ID)) {
                throw new ClassCastException("Unable to cast to CFBoolean. Type ID: " + this.getTypeID());
            }
        }
        
        public boolean booleanValue() {
            return 0 != CoreFoundation.INSTANCE.CFBooleanGetValue(this);
        }
    }
    
    public static class CFArrayRef extends CFTypeRef
    {
        public CFArrayRef() {
        }
        
        public CFArrayRef(final Pointer p) {
            super(p);
            if (!this.isTypeID(CoreFoundation.ARRAY_TYPE_ID)) {
                throw new ClassCastException("Unable to cast to CFArray. Type ID: " + this.getTypeID());
            }
        }
        
        public int getCount() {
            return CoreFoundation.INSTANCE.CFArrayGetCount(this).intValue();
        }
        
        public Pointer getValueAtIndex(final int idx) {
            return CoreFoundation.INSTANCE.CFArrayGetValueAtIndex(this, new CFIndex((long)idx));
        }
    }
    
    public static class CFDataRef extends CFTypeRef
    {
        public CFDataRef() {
        }
        
        public CFDataRef(final Pointer p) {
            super(p);
            if (!this.isTypeID(CoreFoundation.DATA_TYPE_ID)) {
                throw new ClassCastException("Unable to cast to CFData. Type ID: " + this.getTypeID());
            }
        }
        
        public int getLength() {
            return CoreFoundation.INSTANCE.CFDataGetLength(this).intValue();
        }
        
        public Pointer getBytePtr() {
            return CoreFoundation.INSTANCE.CFDataGetBytePtr(this);
        }
    }
    
    public static class CFDictionaryRef extends CFTypeRef
    {
        public CFDictionaryRef() {
        }
        
        public CFDictionaryRef(final Pointer p) {
            super(p);
            if (!this.isTypeID(CoreFoundation.DICTIONARY_TYPE_ID)) {
                throw new ClassCastException("Unable to cast to CFDictionary. Type ID: " + this.getTypeID());
            }
        }
        
        public Pointer getValue(final PointerType key) {
            return CoreFoundation.INSTANCE.CFDictionaryGetValue(this, key);
        }
        
        public boolean getValueIfPresent(final PointerType key, final PointerByReference value) {
            return CoreFoundation.INSTANCE.CFDictionaryGetValueIfPresent(this, key, value) > 0;
        }
    }
    
    public static class CFMutableDictionaryRef extends CFDictionaryRef
    {
        public CFMutableDictionaryRef() {
        }
        
        public CFMutableDictionaryRef(final Pointer p) {
            super(p);
        }
        
        public void setValue(final PointerType key, final PointerType value) {
            CoreFoundation.INSTANCE.CFDictionarySetValue(this, key, value);
        }
    }
    
    public static class CFStringRef extends CFTypeRef
    {
        public CFStringRef() {
        }
        
        public CFStringRef(final Pointer p) {
            super(p);
            if (!this.isTypeID(CoreFoundation.STRING_TYPE_ID)) {
                throw new ClassCastException("Unable to cast to CFString. Type ID: " + this.getTypeID());
            }
        }
        
        public static CFStringRef createCFString(final String s) {
            final char[] chars = s.toCharArray();
            return CoreFoundation.INSTANCE.CFStringCreateWithCharacters(null, chars, new CFIndex((long)chars.length));
        }
        
        public String stringValue() {
            final CFIndex length = CoreFoundation.INSTANCE.CFStringGetLength(this);
            if (length.longValue() == 0L) {
                return "";
            }
            final CFIndex maxSize = CoreFoundation.INSTANCE.CFStringGetMaximumSizeForEncoding(length, 134217984);
            if (maxSize.intValue() == -1) {
                throw new StringIndexOutOfBoundsException("CFString maximum number of bytes exceeds LONG_MAX.");
            }
            maxSize.setValue(maxSize.longValue() + 1L);
            final Memory buf = new Memory(maxSize.longValue());
            if (0 != CoreFoundation.INSTANCE.CFStringGetCString(this, buf, maxSize, 134217984)) {
                return buf.getString(0L, "UTF8");
            }
            throw new IllegalArgumentException("CFString conversion fails or the provided buffer is too small.");
        }
    }
    
    public static class CFIndex extends NativeLong
    {
        private static final long serialVersionUID = 1L;
        
        public CFIndex() {
        }
        
        public CFIndex(final long value) {
            super(value);
        }
    }
    
    public static class CFTypeID extends NativeLong
    {
        private static final long serialVersionUID = 1L;
        
        public CFTypeID() {
        }
        
        public CFTypeID(final long value) {
            super(value);
        }
        
        @Override
        public String toString() {
            if (this.equals(CoreFoundation.ARRAY_TYPE_ID)) {
                return "CFArray";
            }
            if (this.equals(CoreFoundation.BOOLEAN_TYPE_ID)) {
                return "CFBoolean";
            }
            if (this.equals(CoreFoundation.DATA_TYPE_ID)) {
                return "CFData";
            }
            if (this.equals(CoreFoundation.DATE_TYPE_ID)) {
                return "CFDate";
            }
            if (this.equals(CoreFoundation.DICTIONARY_TYPE_ID)) {
                return "CFDictionary";
            }
            if (this.equals(CoreFoundation.NUMBER_TYPE_ID)) {
                return "CFNumber";
            }
            if (this.equals(CoreFoundation.STRING_TYPE_ID)) {
                return "CFString";
            }
            return super.toString();
        }
    }
}
