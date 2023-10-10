// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32PhysicalMemory
{
    private static final String WIN32_PHYSICAL_MEMORY = "Win32_PhysicalMemory";
    
    private Win32PhysicalMemory() {
    }
    
    public static WbemcliUtil.WmiResult<PhysicalMemoryProperty> queryphysicalMemory() {
        final WbemcliUtil.WmiQuery<PhysicalMemoryProperty> physicalMemoryQuery = new WbemcliUtil.WmiQuery<PhysicalMemoryProperty>("Win32_PhysicalMemory", PhysicalMemoryProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(physicalMemoryQuery);
    }
    
    public static WbemcliUtil.WmiResult<PhysicalMemoryPropertyWin8> queryphysicalMemoryWin8() {
        final WbemcliUtil.WmiQuery<PhysicalMemoryPropertyWin8> physicalMemoryQuery = new WbemcliUtil.WmiQuery<PhysicalMemoryPropertyWin8>("Win32_PhysicalMemory", PhysicalMemoryPropertyWin8.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(physicalMemoryQuery);
    }
    
    public enum PhysicalMemoryProperty
    {
        BANKLABEL, 
        CAPACITY, 
        SPEED, 
        MANUFACTURER, 
        SMBIOSMEMORYTYPE;
        
        private static /* synthetic */ PhysicalMemoryProperty[] $values() {
            return new PhysicalMemoryProperty[] { PhysicalMemoryProperty.BANKLABEL, PhysicalMemoryProperty.CAPACITY, PhysicalMemoryProperty.SPEED, PhysicalMemoryProperty.MANUFACTURER, PhysicalMemoryProperty.SMBIOSMEMORYTYPE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum PhysicalMemoryPropertyWin8
    {
        BANKLABEL, 
        CAPACITY, 
        SPEED, 
        MANUFACTURER, 
        MEMORYTYPE;
        
        private static /* synthetic */ PhysicalMemoryPropertyWin8[] $values() {
            return new PhysicalMemoryPropertyWin8[] { PhysicalMemoryPropertyWin8.BANKLABEL, PhysicalMemoryPropertyWin8.CAPACITY, PhysicalMemoryPropertyWin8.SPEED, PhysicalMemoryPropertyWin8.MANUFACTURER, PhysicalMemoryPropertyWin8.MEMORYTYPE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
