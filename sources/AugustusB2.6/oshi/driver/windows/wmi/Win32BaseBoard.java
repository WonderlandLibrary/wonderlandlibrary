// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32BaseBoard
{
    private static final String WIN32_BASEBOARD = "Win32_BaseBoard";
    
    private Win32BaseBoard() {
    }
    
    public static WbemcliUtil.WmiResult<BaseBoardProperty> queryBaseboardInfo() {
        final WbemcliUtil.WmiQuery<BaseBoardProperty> baseboardQuery = new WbemcliUtil.WmiQuery<BaseBoardProperty>("Win32_BaseBoard", BaseBoardProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(baseboardQuery);
    }
    
    public enum BaseBoardProperty
    {
        MANUFACTURER, 
        MODEL, 
        VERSION, 
        SERIALNUMBER;
        
        private static /* synthetic */ BaseBoardProperty[] $values() {
            return new BaseBoardProperty[] { BaseBoardProperty.MANUFACTURER, BaseBoardProperty.MODEL, BaseBoardProperty.VERSION, BaseBoardProperty.SERIALNUMBER };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
