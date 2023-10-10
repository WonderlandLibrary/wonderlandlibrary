// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiQueryHandler;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32DiskDriveToDiskPartition
{
    private static final String WIN32_DISK_DRIVE_TO_DISK_PARTITION = "Win32_DiskDriveToDiskPartition";
    
    private Win32DiskDriveToDiskPartition() {
    }
    
    public static WbemcliUtil.WmiResult<DriveToPartitionProperty> queryDriveToPartition(final WmiQueryHandler h) {
        final WbemcliUtil.WmiQuery<DriveToPartitionProperty> driveToPartitionQuery = new WbemcliUtil.WmiQuery<DriveToPartitionProperty>("Win32_DiskDriveToDiskPartition", DriveToPartitionProperty.class);
        return h.queryWMI(driveToPartitionQuery, false);
    }
    
    public enum DriveToPartitionProperty
    {
        ANTECEDENT, 
        DEPENDENT;
        
        private static /* synthetic */ DriveToPartitionProperty[] $values() {
            return new DriveToPartitionProperty[] { DriveToPartitionProperty.ANTECEDENT, DriveToPartitionProperty.DEPENDENT };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
