// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32Fan
{
    private static final String WIN32_FAN = "Win32_Fan";
    
    private Win32Fan() {
    }
    
    public static WbemcliUtil.WmiResult<SpeedProperty> querySpeed() {
        final WbemcliUtil.WmiQuery<SpeedProperty> fanQuery = new WbemcliUtil.WmiQuery<SpeedProperty>("Win32_Fan", SpeedProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(fanQuery);
    }
    
    public enum SpeedProperty
    {
        DESIREDSPEED;
        
        private static /* synthetic */ SpeedProperty[] $values() {
            return new SpeedProperty[] { SpeedProperty.DESIREDSPEED };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
