// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.linux;

import com.sun.jna.PointerType;
import com.sun.jna.Native;
import com.sun.jna.Library;

public interface Udev extends Library
{
    public static final Udev INSTANCE = Native.load("udev", Udev.class);
    
    UdevContext udev_new();
    
    UdevContext udev_ref(final UdevContext p0);
    
    UdevContext udev_unref(final UdevContext p0);
    
    UdevDevice udev_device_new_from_syspath(final UdevContext p0, final String p1);
    
    UdevEnumerate udev_enumerate_new(final UdevContext p0);
    
    UdevEnumerate udev_enumerate_ref(final UdevEnumerate p0);
    
    UdevEnumerate udev_enumerate_unref(final UdevEnumerate p0);
    
    int udev_enumerate_add_match_subsystem(final UdevEnumerate p0, final String p1);
    
    int udev_enumerate_scan_devices(final UdevEnumerate p0);
    
    UdevListEntry udev_enumerate_get_list_entry(final UdevEnumerate p0);
    
    UdevListEntry udev_list_entry_get_next(final UdevListEntry p0);
    
    String udev_list_entry_get_name(final UdevListEntry p0);
    
    UdevDevice udev_device_ref(final UdevDevice p0);
    
    UdevDevice udev_device_unref(final UdevDevice p0);
    
    UdevDevice udev_device_get_parent(final UdevDevice p0);
    
    UdevDevice udev_device_get_parent_with_subsystem_devtype(final UdevDevice p0, final String p1, final String p2);
    
    String udev_device_get_syspath(final UdevDevice p0);
    
    String udev_device_get_sysname(final UdevDevice p0);
    
    String udev_device_get_devnode(final UdevDevice p0);
    
    String udev_device_get_devtype(final UdevDevice p0);
    
    String udev_device_get_subsystem(final UdevDevice p0);
    
    String udev_device_get_sysattr_value(final UdevDevice p0, final String p1);
    
    String udev_device_get_property_value(final UdevDevice p0, final String p1);
    
    public static class UdevContext extends PointerType
    {
        public UdevContext ref() {
            return Udev.INSTANCE.udev_ref(this);
        }
        
        public void unref() {
            Udev.INSTANCE.udev_unref(this);
        }
        
        public UdevEnumerate enumerateNew() {
            return Udev.INSTANCE.udev_enumerate_new(this);
        }
        
        public UdevDevice deviceNewFromSyspath(final String syspath) {
            return Udev.INSTANCE.udev_device_new_from_syspath(this, syspath);
        }
    }
    
    public static class UdevEnumerate extends PointerType
    {
        public UdevEnumerate ref() {
            return Udev.INSTANCE.udev_enumerate_ref(this);
        }
        
        public void unref() {
            Udev.INSTANCE.udev_enumerate_unref(this);
        }
        
        public int addMatchSubsystem(final String subsystem) {
            return Udev.INSTANCE.udev_enumerate_add_match_subsystem(this, subsystem);
        }
        
        public int scanDevices() {
            return Udev.INSTANCE.udev_enumerate_scan_devices(this);
        }
        
        public UdevListEntry getListEntry() {
            return Udev.INSTANCE.udev_enumerate_get_list_entry(this);
        }
    }
    
    public static class UdevListEntry extends PointerType
    {
        public UdevListEntry getNext() {
            return Udev.INSTANCE.udev_list_entry_get_next(this);
        }
        
        public String getName() {
            return Udev.INSTANCE.udev_list_entry_get_name(this);
        }
    }
    
    public static class UdevDevice extends PointerType
    {
        public UdevDevice ref() {
            return Udev.INSTANCE.udev_device_ref(this);
        }
        
        public void unref() {
            Udev.INSTANCE.udev_device_unref(this);
        }
        
        public UdevDevice getParent() {
            return Udev.INSTANCE.udev_device_get_parent(this);
        }
        
        public UdevDevice getParentWithSubsystemDevtype(final String subsystem, final String devtype) {
            return Udev.INSTANCE.udev_device_get_parent_with_subsystem_devtype(this, subsystem, devtype);
        }
        
        public String getSyspath() {
            return Udev.INSTANCE.udev_device_get_syspath(this);
        }
        
        public String getSysname() {
            return Udev.INSTANCE.udev_device_get_syspath(this);
        }
        
        public String getDevnode() {
            return Udev.INSTANCE.udev_device_get_devnode(this);
        }
        
        public String getDevtype() {
            return Udev.INSTANCE.udev_device_get_devtype(this);
        }
        
        public String getSubsystem() {
            return Udev.INSTANCE.udev_device_get_subsystem(this);
        }
        
        public String getSysattrValue(final String sysattr) {
            return Udev.INSTANCE.udev_device_get_sysattr_value(this, sysattr);
        }
        
        public String getPropertyValue(final String key) {
            return Udev.INSTANCE.udev_device_get_property_value(this, key);
        }
    }
}
