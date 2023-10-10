// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class MSAcpiThermalZoneTemperature
{
    public static final String WMI_NAMESPACE = "ROOT\\WMI";
    private static final String MS_ACPI_THERMAL_ZONE_TEMPERATURE = "MSAcpi_ThermalZoneTemperature";
    
    private MSAcpiThermalZoneTemperature() {
    }
    
    public static WbemcliUtil.WmiResult<TemperatureProperty> queryCurrentTemperature() {
        final WbemcliUtil.WmiQuery<TemperatureProperty> curTempQuery = new WbemcliUtil.WmiQuery<TemperatureProperty>("ROOT\\WMI", "MSAcpi_ThermalZoneTemperature", TemperatureProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(curTempQuery);
    }
    
    public enum TemperatureProperty
    {
        CURRENTTEMPERATURE;
        
        private static /* synthetic */ TemperatureProperty[] $values() {
            return new TemperatureProperty[] { TemperatureProperty.CURRENTTEMPERATURE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
