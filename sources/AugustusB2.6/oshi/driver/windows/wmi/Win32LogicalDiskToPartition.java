// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiQueryHandler;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32LogicalDiskToPartition
{
    private static final String WIN32_LOGICAL_DISK_TO_PARTITION = "Win32_LogicalDiskToPartition";
    
    private Win32LogicalDiskToPartition() {
    }
    
    public static WbemcliUtil.WmiResult<DiskToPartitionProperty> queryDiskToPartition(final WmiQueryHandler h) {
        final WbemcliUtil.WmiQuery<DiskToPartitionProperty> diskToPartitionQuery = new WbemcliUtil.WmiQuery<DiskToPartitionProperty>("Win32_LogicalDiskToPartition", DiskToPartitionProperty.class);
        return h.queryWMI(diskToPartitionQuery, false);
    }
    
    public enum DiskToPartitionProperty
    {
        ANTECEDENT, 
        DEPENDENT, 
        ENDINGADDRESS, 
        STARTINGADDRESS;
        
        private static /* synthetic */ DiskToPartitionProperty[] $values() {
            return new DiskToPartitionProperty[] { DiskToPartitionProperty.ANTECEDENT, DiskToPartitionProperty.DEPENDENT, DiskToPartitionProperty.ENDINGADDRESS, DiskToPartitionProperty.STARTINGADDRESS };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
