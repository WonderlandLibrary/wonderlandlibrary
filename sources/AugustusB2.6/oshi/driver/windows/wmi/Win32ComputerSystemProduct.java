// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32ComputerSystemProduct
{
    private static final String WIN32_COMPUTER_SYSTEM_PRODUCT = "Win32_ComputerSystemProduct";
    
    private Win32ComputerSystemProduct() {
    }
    
    public static WbemcliUtil.WmiResult<ComputerSystemProductProperty> queryIdentifyingNumberUUID() {
        final WbemcliUtil.WmiQuery<ComputerSystemProductProperty> identifyingNumberQuery = new WbemcliUtil.WmiQuery<ComputerSystemProductProperty>("Win32_ComputerSystemProduct", ComputerSystemProductProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(identifyingNumberQuery);
    }
    
    public enum ComputerSystemProductProperty
    {
        IDENTIFYINGNUMBER, 
        UUID;
        
        private static /* synthetic */ ComputerSystemProductProperty[] $values() {
            return new ComputerSystemProductProperty[] { ComputerSystemProductProperty.IDENTIFYINGNUMBER, ComputerSystemProductProperty.UUID };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
