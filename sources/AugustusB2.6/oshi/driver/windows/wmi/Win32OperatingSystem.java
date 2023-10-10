// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32OperatingSystem
{
    private static final String WIN32_OPERATING_SYSTEM = "Win32_OperatingSystem";
    
    private Win32OperatingSystem() {
    }
    
    public static WbemcliUtil.WmiResult<OSVersionProperty> queryOsVersion() {
        final WbemcliUtil.WmiQuery<OSVersionProperty> osVersionQuery = new WbemcliUtil.WmiQuery<OSVersionProperty>("Win32_OperatingSystem", OSVersionProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(osVersionQuery);
    }
    
    public enum OSVersionProperty
    {
        VERSION, 
        PRODUCTTYPE, 
        BUILDNUMBER, 
        CSDVERSION, 
        SUITEMASK;
        
        private static /* synthetic */ OSVersionProperty[] $values() {
            return new OSVersionProperty[] { OSVersionProperty.VERSION, OSVersionProperty.PRODUCTTYPE, OSVersionProperty.BUILDNUMBER, OSVersionProperty.CSDVERSION, OSVersionProperty.SUITEMASK };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
