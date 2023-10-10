// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiQueryHandler;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class OhmSensor
{
    private static final String SENSOR = "Sensor";
    
    private OhmSensor() {
    }
    
    public static WbemcliUtil.WmiResult<ValueProperty> querySensorValue(final WmiQueryHandler h, final String identifier, final String sensorType) {
        final StringBuilder sb = new StringBuilder("Sensor");
        sb.append(" WHERE Parent = \"").append(identifier);
        sb.append("\" AND SensorType=\"").append(sensorType).append('\"');
        final WbemcliUtil.WmiQuery<ValueProperty> ohmSensorQuery = new WbemcliUtil.WmiQuery<ValueProperty>("ROOT\\OpenHardwareMonitor", sb.toString(), ValueProperty.class);
        return h.queryWMI(ohmSensorQuery, false);
    }
    
    public enum ValueProperty
    {
        VALUE;
        
        private static /* synthetic */ ValueProperty[] $values() {
            return new ValueProperty[] { ValueProperty.VALUE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
