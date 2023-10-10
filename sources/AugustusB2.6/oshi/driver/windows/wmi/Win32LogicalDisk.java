// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32LogicalDisk
{
    private static final String WIN32_LOGICAL_DISK = "Win32_LogicalDisk";
    
    private Win32LogicalDisk() {
    }
    
    public static WbemcliUtil.WmiResult<LogicalDiskProperty> queryLogicalDisk(final String nameToMatch, final boolean localOnly) {
        final StringBuilder wmiClassName = new StringBuilder("Win32_LogicalDisk");
        boolean where = false;
        if (localOnly) {
            wmiClassName.append(" WHERE DriveType != 4");
            where = true;
        }
        if (nameToMatch != null) {
            wmiClassName.append(where ? " AND" : " WHERE").append(" Name=\"").append(nameToMatch).append('\"');
        }
        final WbemcliUtil.WmiQuery<LogicalDiskProperty> logicalDiskQuery = new WbemcliUtil.WmiQuery<LogicalDiskProperty>(wmiClassName.toString(), LogicalDiskProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(logicalDiskQuery);
    }
    
    public enum LogicalDiskProperty
    {
        ACCESS, 
        DESCRIPTION, 
        DRIVETYPE, 
        FILESYSTEM, 
        FREESPACE, 
        NAME, 
        PROVIDERNAME, 
        SIZE, 
        VOLUMENAME;
        
        private static /* synthetic */ LogicalDiskProperty[] $values() {
            return new LogicalDiskProperty[] { LogicalDiskProperty.ACCESS, LogicalDiskProperty.DESCRIPTION, LogicalDiskProperty.DRIVETYPE, LogicalDiskProperty.FILESYSTEM, LogicalDiskProperty.FREESPACE, LogicalDiskProperty.NAME, LogicalDiskProperty.PROVIDERNAME, LogicalDiskProperty.SIZE, LogicalDiskProperty.VOLUMENAME };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
