// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import oshi.hardware.PowerSource;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractPowerSource;

@ThreadSafe
public final class AixPowerSource extends AbstractPowerSource
{
    public AixPowerSource(final String name, final String deviceName, final double remainingCapacityPercent, final double timeRemainingEstimated, final double timeRemainingInstant, final double powerUsageRate, final double voltage, final double amperage, final boolean powerOnLine, final boolean charging, final boolean discharging, final PowerSource.CapacityUnits capacityUnits, final int currentCapacity, final int maxCapacity, final int designCapacity, final int cycleCount, final String chemistry, final LocalDate manufactureDate, final String manufacturer, final String serialNumber, final double temperature) {
        super(name, deviceName, remainingCapacityPercent, timeRemainingEstimated, timeRemainingInstant, powerUsageRate, voltage, amperage, powerOnLine, charging, discharging, capacityUnits, currentCapacity, maxCapacity, designCapacity, cycleCount, chemistry, manufactureDate, manufacturer, serialNumber, temperature);
    }
    
    public static List<PowerSource> getPowerSources() {
        return Collections.emptyList();
    }
}
