// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiQueryHandler;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class MSFTStorage
{
    private static final String STORAGE_NAMESPACE = "ROOT\\Microsoft\\Windows\\Storage";
    private static final String MSFT_STORAGE_POOL_WHERE_IS_PRIMORDIAL_FALSE = "MSFT_StoragePool WHERE IsPrimordial=FALSE";
    private static final String MSFT_STORAGE_POOL_TO_PHYSICAL_DISK = "MSFT_StoragePoolToPhysicalDisk";
    private static final String MSFT_PHYSICAL_DISK = "MSFT_PhysicalDisk";
    private static final String MSFT_VIRTUAL_DISK = "MSFT_VirtualDisk";
    
    private MSFTStorage() {
    }
    
    public static WbemcliUtil.WmiResult<StoragePoolProperty> queryStoragePools(final WmiQueryHandler h) {
        final WbemcliUtil.WmiQuery<StoragePoolProperty> storagePoolQuery = new WbemcliUtil.WmiQuery<StoragePoolProperty>("ROOT\\Microsoft\\Windows\\Storage", "MSFT_StoragePool WHERE IsPrimordial=FALSE", StoragePoolProperty.class);
        return h.queryWMI(storagePoolQuery, false);
    }
    
    public static WbemcliUtil.WmiResult<StoragePoolToPhysicalDiskProperty> queryStoragePoolPhysicalDisks(final WmiQueryHandler h) {
        final WbemcliUtil.WmiQuery<StoragePoolToPhysicalDiskProperty> storagePoolToPhysicalDiskQuery = new WbemcliUtil.WmiQuery<StoragePoolToPhysicalDiskProperty>("ROOT\\Microsoft\\Windows\\Storage", "MSFT_StoragePoolToPhysicalDisk", StoragePoolToPhysicalDiskProperty.class);
        return h.queryWMI(storagePoolToPhysicalDiskQuery, false);
    }
    
    public static WbemcliUtil.WmiResult<PhysicalDiskProperty> queryPhysicalDisks(final WmiQueryHandler h) {
        final WbemcliUtil.WmiQuery<PhysicalDiskProperty> physicalDiskQuery = new WbemcliUtil.WmiQuery<PhysicalDiskProperty>("ROOT\\Microsoft\\Windows\\Storage", "MSFT_PhysicalDisk", PhysicalDiskProperty.class);
        return h.queryWMI(physicalDiskQuery, false);
    }
    
    public static WbemcliUtil.WmiResult<VirtualDiskProperty> queryVirtualDisks(final WmiQueryHandler h) {
        final WbemcliUtil.WmiQuery<VirtualDiskProperty> virtualDiskQuery = new WbemcliUtil.WmiQuery<VirtualDiskProperty>("ROOT\\Microsoft\\Windows\\Storage", "MSFT_VirtualDisk", VirtualDiskProperty.class);
        return h.queryWMI(virtualDiskQuery, false);
    }
    
    public enum StoragePoolProperty
    {
        FRIENDLYNAME, 
        OBJECTID;
        
        private static /* synthetic */ StoragePoolProperty[] $values() {
            return new StoragePoolProperty[] { StoragePoolProperty.FRIENDLYNAME, StoragePoolProperty.OBJECTID };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum StoragePoolToPhysicalDiskProperty
    {
        STORAGEPOOL, 
        PHYSICALDISK;
        
        private static /* synthetic */ StoragePoolToPhysicalDiskProperty[] $values() {
            return new StoragePoolToPhysicalDiskProperty[] { StoragePoolToPhysicalDiskProperty.STORAGEPOOL, StoragePoolToPhysicalDiskProperty.PHYSICALDISK };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum PhysicalDiskProperty
    {
        FRIENDLYNAME, 
        PHYSICALLOCATION, 
        OBJECTID;
        
        private static /* synthetic */ PhysicalDiskProperty[] $values() {
            return new PhysicalDiskProperty[] { PhysicalDiskProperty.FRIENDLYNAME, PhysicalDiskProperty.PHYSICALLOCATION, PhysicalDiskProperty.OBJECTID };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum VirtualDiskProperty
    {
        FRIENDLYNAME, 
        OBJECTID;
        
        private static /* synthetic */ VirtualDiskProperty[] $values() {
            return new VirtualDiskProperty[] { VirtualDiskProperty.FRIENDLYNAME, VirtualDiskProperty.OBJECTID };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
