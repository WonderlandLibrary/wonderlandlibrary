// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiQueryHandler;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class OhmHardware
{
    private static final String HARDWARE = "Hardware";
    
    private OhmHardware() {
    }
    
    public static WbemcliUtil.WmiResult<IdentifierProperty> queryHwIdentifier(final WmiQueryHandler h, final String typeToQuery, final String typeName) {
        final StringBuilder sb = new StringBuilder("Hardware");
        sb.append(" WHERE ").append(typeToQuery).append("Type=\"").append(typeName).append('\"');
        final WbemcliUtil.WmiQuery<IdentifierProperty> cpuIdentifierQuery = new WbemcliUtil.WmiQuery<IdentifierProperty>("ROOT\\OpenHardwareMonitor", sb.toString(), IdentifierProperty.class);
        return h.queryWMI(cpuIdentifierQuery, false);
    }
    
    public enum IdentifierProperty
    {
        IDENTIFIER;
        
        private static /* synthetic */ IdentifierProperty[] $values() {
            return new IdentifierProperty[] { IdentifierProperty.IDENTIFIER };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
