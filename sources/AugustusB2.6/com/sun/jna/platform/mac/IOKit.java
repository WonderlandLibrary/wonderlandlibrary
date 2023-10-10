// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import com.sun.jna.Memory;
import com.sun.jna.PointerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Library;

public interface IOKit extends Library
{
    public static final IOKit INSTANCE = Native.load("IOKit", IOKit.class);
    public static final int kIORegistryIterateRecursively = 1;
    public static final int kIORegistryIterateParents = 2;
    public static final int kIOReturnNoDevice = -536870208;
    public static final double kIOPSTimeRemainingUnlimited = -2.0;
    public static final double kIOPSTimeRemainingUnknown = -1.0;
    
    int IOMasterPort(final int p0, final IntByReference p1);
    
    CoreFoundation.CFMutableDictionaryRef IOServiceMatching(final String p0);
    
    CoreFoundation.CFMutableDictionaryRef IOServiceNameMatching(final String p0);
    
    CoreFoundation.CFMutableDictionaryRef IOBSDNameMatching(final int p0, final int p1, final String p2);
    
    IOService IOServiceGetMatchingService(final int p0, final CoreFoundation.CFDictionaryRef p1);
    
    int IOServiceGetMatchingServices(final int p0, final CoreFoundation.CFDictionaryRef p1, final PointerByReference p2);
    
    IORegistryEntry IOIteratorNext(final IOIterator p0);
    
    CoreFoundation.CFTypeRef IORegistryEntryCreateCFProperty(final IORegistryEntry p0, final CoreFoundation.CFStringRef p1, final CoreFoundation.CFAllocatorRef p2, final int p3);
    
    int IORegistryEntryCreateCFProperties(final IORegistryEntry p0, final PointerByReference p1, final CoreFoundation.CFAllocatorRef p2, final int p3);
    
    CoreFoundation.CFTypeRef IORegistryEntrySearchCFProperty(final IORegistryEntry p0, final String p1, final CoreFoundation.CFStringRef p2, final CoreFoundation.CFAllocatorRef p3, final int p4);
    
    int IORegistryEntryGetRegistryEntryID(final IORegistryEntry p0, final LongByReference p1);
    
    int IORegistryEntryGetName(final IORegistryEntry p0, final Pointer p1);
    
    int IORegistryEntryGetChildIterator(final IORegistryEntry p0, final String p1, final PointerByReference p2);
    
    int IORegistryEntryGetChildEntry(final IORegistryEntry p0, final String p1, final PointerByReference p2);
    
    int IORegistryEntryGetParentEntry(final IORegistryEntry p0, final String p1, final PointerByReference p2);
    
    IORegistryEntry IORegistryGetRootEntry(final int p0);
    
    boolean IOObjectConformsTo(final IOObject p0, final String p1);
    
    int IOObjectRelease(final IOObject p0);
    
    int IOServiceOpen(final IOService p0, final int p1, final int p2, final PointerByReference p3);
    
    int IOServiceGetBusyState(final IOService p0, final IntByReference p1);
    
    int IOServiceClose(final IOConnect p0);
    
    CoreFoundation.CFTypeRef IOPSCopyPowerSourcesInfo();
    
    CoreFoundation.CFArrayRef IOPSCopyPowerSourcesList(final CoreFoundation.CFTypeRef p0);
    
    CoreFoundation.CFDictionaryRef IOPSGetPowerSourceDescription(final CoreFoundation.CFTypeRef p0, final CoreFoundation.CFTypeRef p1);
    
    double IOPSGetTimeRemainingEstimate();
    
    public static class IOObject extends PointerType
    {
        public IOObject() {
        }
        
        public IOObject(final Pointer p) {
            super(p);
        }
        
        public boolean conformsTo(final String className) {
            return IOKit.INSTANCE.IOObjectConformsTo(this, className);
        }
        
        public int release() {
            return IOKit.INSTANCE.IOObjectRelease(this);
        }
    }
    
    public static class IOIterator extends IOObject
    {
        public IOIterator() {
        }
        
        public IOIterator(final Pointer p) {
            super(p);
        }
        
        public IORegistryEntry next() {
            return IOKit.INSTANCE.IOIteratorNext(this);
        }
    }
    
    public static class IORegistryEntry extends IOObject
    {
        public IORegistryEntry() {
        }
        
        public IORegistryEntry(final Pointer p) {
            super(p);
        }
        
        public long getRegistryEntryID() {
            final LongByReference id = new LongByReference();
            final int kr = IOKit.INSTANCE.IORegistryEntryGetRegistryEntryID(this, id);
            if (kr != 0) {
                throw new IOReturnException(kr);
            }
            return id.getValue();
        }
        
        public String getName() {
            final Memory name = new Memory(128L);
            final int kr = IOKit.INSTANCE.IORegistryEntryGetName(this, name);
            if (kr != 0) {
                throw new IOReturnException(kr);
            }
            return name.getString(0L);
        }
        
        public IOIterator getChildIterator(final String plane) {
            final PointerByReference iter = new PointerByReference();
            final int kr = IOKit.INSTANCE.IORegistryEntryGetChildIterator(this, plane, iter);
            if (kr != 0) {
                throw new IOReturnException(kr);
            }
            return new IOIterator(iter.getValue());
        }
        
        public IORegistryEntry getChildEntry(final String plane) {
            final PointerByReference child = new PointerByReference();
            final int kr = IOKit.INSTANCE.IORegistryEntryGetChildEntry(this, plane, child);
            if (kr == -536870208) {
                return null;
            }
            if (kr != 0) {
                throw new IOReturnException(kr);
            }
            return new IORegistryEntry(child.getValue());
        }
        
        public IORegistryEntry getParentEntry(final String plane) {
            final PointerByReference parent = new PointerByReference();
            final int kr = IOKit.INSTANCE.IORegistryEntryGetParentEntry(this, plane, parent);
            if (kr == -536870208) {
                return null;
            }
            if (kr != 0) {
                throw new IOReturnException(kr);
            }
            return new IORegistryEntry(parent.getValue());
        }
        
        public CoreFoundation.CFTypeRef createCFProperty(final CoreFoundation.CFStringRef key) {
            return IOKit.INSTANCE.IORegistryEntryCreateCFProperty(this, key, CoreFoundation.INSTANCE.CFAllocatorGetDefault(), 0);
        }
        
        public CoreFoundation.CFMutableDictionaryRef createCFProperties() {
            final PointerByReference properties = new PointerByReference();
            final int kr = IOKit.INSTANCE.IORegistryEntryCreateCFProperties(this, properties, CoreFoundation.INSTANCE.CFAllocatorGetDefault(), 0);
            if (kr != 0) {
                throw new IOReturnException(kr);
            }
            return new CoreFoundation.CFMutableDictionaryRef(properties.getValue());
        }
        
        CoreFoundation.CFTypeRef searchCFProperty(final String plane, final CoreFoundation.CFStringRef key, final int options) {
            return IOKit.INSTANCE.IORegistryEntrySearchCFProperty(this, plane, key, CoreFoundation.INSTANCE.CFAllocatorGetDefault(), options);
        }
        
        public String getStringProperty(final String key) {
            String value = null;
            final CoreFoundation.CFStringRef keyAsCFString = CoreFoundation.CFStringRef.createCFString(key);
            final CoreFoundation.CFTypeRef valueAsCFType = this.createCFProperty(keyAsCFString);
            keyAsCFString.release();
            if (valueAsCFType != null) {
                final CoreFoundation.CFStringRef valueAsCFString = new CoreFoundation.CFStringRef(valueAsCFType.getPointer());
                value = valueAsCFString.stringValue();
                valueAsCFType.release();
            }
            return value;
        }
        
        public Long getLongProperty(final String key) {
            Long value = null;
            final CoreFoundation.CFStringRef keyAsCFString = CoreFoundation.CFStringRef.createCFString(key);
            final CoreFoundation.CFTypeRef valueAsCFType = this.createCFProperty(keyAsCFString);
            keyAsCFString.release();
            if (valueAsCFType != null) {
                final CoreFoundation.CFNumberRef valueAsCFNumber = new CoreFoundation.CFNumberRef(valueAsCFType.getPointer());
                value = valueAsCFNumber.longValue();
                valueAsCFType.release();
            }
            return value;
        }
        
        public Integer getIntegerProperty(final String key) {
            Integer value = null;
            final CoreFoundation.CFStringRef keyAsCFString = CoreFoundation.CFStringRef.createCFString(key);
            final CoreFoundation.CFTypeRef valueAsCFType = this.createCFProperty(keyAsCFString);
            keyAsCFString.release();
            if (valueAsCFType != null) {
                final CoreFoundation.CFNumberRef valueAsCFNumber = new CoreFoundation.CFNumberRef(valueAsCFType.getPointer());
                value = valueAsCFNumber.intValue();
                valueAsCFType.release();
            }
            return value;
        }
        
        public Double getDoubleProperty(final String key) {
            Double value = null;
            final CoreFoundation.CFStringRef keyAsCFString = CoreFoundation.CFStringRef.createCFString(key);
            final CoreFoundation.CFTypeRef valueAsCFType = this.createCFProperty(keyAsCFString);
            keyAsCFString.release();
            if (valueAsCFType != null) {
                final CoreFoundation.CFNumberRef valueAsCFNumber = new CoreFoundation.CFNumberRef(valueAsCFType.getPointer());
                value = valueAsCFNumber.doubleValue();
                valueAsCFType.release();
            }
            return value;
        }
        
        public Boolean getBooleanProperty(final String key) {
            Boolean value = null;
            final CoreFoundation.CFStringRef keyAsCFString = CoreFoundation.CFStringRef.createCFString(key);
            final CoreFoundation.CFTypeRef valueAsCFType = this.createCFProperty(keyAsCFString);
            keyAsCFString.release();
            if (valueAsCFType != null) {
                final CoreFoundation.CFBooleanRef valueAsCFBoolean = new CoreFoundation.CFBooleanRef(valueAsCFType.getPointer());
                value = valueAsCFBoolean.booleanValue();
                valueAsCFType.release();
            }
            return value;
        }
        
        public byte[] getByteArrayProperty(final String key) {
            byte[] value = null;
            final CoreFoundation.CFStringRef keyAsCFString = CoreFoundation.CFStringRef.createCFString(key);
            final CoreFoundation.CFTypeRef valueAsCFType = this.createCFProperty(keyAsCFString);
            keyAsCFString.release();
            if (valueAsCFType != null) {
                final CoreFoundation.CFDataRef valueAsCFData = new CoreFoundation.CFDataRef(valueAsCFType.getPointer());
                final int length = valueAsCFData.getLength();
                final Pointer p = valueAsCFData.getBytePtr();
                value = p.getByteArray(0L, length);
                valueAsCFType.release();
            }
            return value;
        }
    }
    
    public static class IOService extends IORegistryEntry
    {
        public IOService() {
        }
        
        public IOService(final Pointer p) {
            super(p);
        }
    }
    
    public static class IOConnect extends IOService
    {
        public IOConnect() {
        }
        
        public IOConnect(final Pointer p) {
            super(p);
        }
    }
}
