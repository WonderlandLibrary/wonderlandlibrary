// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32Bios
{
    private static final String WIN32_BIOS_WHERE_PRIMARY_BIOS_TRUE = "Win32_BIOS where PrimaryBIOS=true";
    
    private Win32Bios() {
    }
    
    public static WbemcliUtil.WmiResult<BiosSerialProperty> querySerialNumber() {
        final WbemcliUtil.WmiQuery<BiosSerialProperty> serialNumQuery = new WbemcliUtil.WmiQuery<BiosSerialProperty>("Win32_BIOS where PrimaryBIOS=true", BiosSerialProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(serialNumQuery);
    }
    
    public static WbemcliUtil.WmiResult<BiosProperty> queryBiosInfo() {
        final WbemcliUtil.WmiQuery<BiosProperty> biosQuery = new WbemcliUtil.WmiQuery<BiosProperty>("Win32_BIOS where PrimaryBIOS=true", BiosProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(biosQuery);
    }
    
    public enum BiosSerialProperty
    {
        SERIALNUMBER;
        
        private static /* synthetic */ BiosSerialProperty[] $values() {
            return new BiosSerialProperty[] { BiosSerialProperty.SERIALNUMBER };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum BiosProperty
    {
        MANUFACTURER, 
        NAME, 
        DESCRIPTION, 
        VERSION, 
        RELEASEDATE;
        
        private static /* synthetic */ BiosProperty[] $values() {
            return new BiosProperty[] { BiosProperty.MANUFACTURER, BiosProperty.NAME, BiosProperty.DESCRIPTION, BiosProperty.VERSION, BiosProperty.RELEASEDATE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
