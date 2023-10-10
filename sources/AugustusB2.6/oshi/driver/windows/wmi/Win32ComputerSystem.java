// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32ComputerSystem
{
    private static final String WIN32_COMPUTER_SYSTEM = "Win32_ComputerSystem";
    
    private Win32ComputerSystem() {
    }
    
    public static WbemcliUtil.WmiResult<ComputerSystemProperty> queryComputerSystem() {
        final WbemcliUtil.WmiQuery<ComputerSystemProperty> computerSystemQuery = new WbemcliUtil.WmiQuery<ComputerSystemProperty>("Win32_ComputerSystem", ComputerSystemProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(computerSystemQuery);
    }
    
    public enum ComputerSystemProperty
    {
        MANUFACTURER, 
        MODEL;
        
        private static /* synthetic */ ComputerSystemProperty[] $values() {
            return new ComputerSystemProperty[] { ComputerSystemProperty.MANUFACTURER, ComputerSystemProperty.MODEL };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
