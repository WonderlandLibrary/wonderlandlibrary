// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import oshi.util.ParseUtil;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import oshi.util.ExecutingCommand;
import java.util.HashSet;
import java.util.List;
import java.time.LocalDate;
import oshi.hardware.PowerSource;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractPowerSource;

@ThreadSafe
public final class OpenBsdPowerSource extends AbstractPowerSource
{
    public OpenBsdPowerSource(final String psName, final String psDeviceName, final double psRemainingCapacityPercent, final double psTimeRemainingEstimated, final double psTimeRemainingInstant, final double psPowerUsageRate, final double psVoltage, final double psAmperage, final boolean psPowerOnLine, final boolean psCharging, final boolean psDischarging, final PowerSource.CapacityUnits psCapacityUnits, final int psCurrentCapacity, final int psMaxCapacity, final int psDesignCapacity, final int psCycleCount, final String psChemistry, final LocalDate psManufactureDate, final String psManufacturer, final String psSerialNumber, final double psTemperature) {
        super(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    public static List<PowerSource> getPowerSources() {
        final Set<String> psNames = new HashSet<String>();
        for (final String line : ExecutingCommand.runNative("systat -ab sensors")) {
            if (line.contains(".amphour") || line.contains(".watthour")) {
                final int dot = line.indexOf(46);
                psNames.add(line.substring(0, dot));
            }
        }
        final List<PowerSource> psList = new ArrayList<PowerSource>();
        for (final String name : psNames) {
            psList.add(getPowerSource(name));
        }
        return psList;
    }
    
    private static OpenBsdPowerSource getPowerSource(final String name) {
        final String psName = name.startsWith("acpi") ? name.substring(4) : name;
        double psRemainingCapacityPercent = 1.0;
        double psTimeRemainingEstimated = -1.0;
        double psPowerUsageRate = 0.0;
        double psVoltage = -1.0;
        double psAmperage = 0.0;
        boolean psPowerOnLine = false;
        boolean psCharging = false;
        boolean psDischarging = false;
        PowerSource.CapacityUnits psCapacityUnits = PowerSource.CapacityUnits.RELATIVE;
        int psCurrentCapacity = 0;
        int psMaxCapacity = 1;
        int psDesignCapacity = 1;
        final int psCycleCount = -1;
        final LocalDate psManufactureDate = null;
        double psTemperature = 0.0;
        for (final String line : ExecutingCommand.runNative("systat -ab sensors")) {
            final String[] split = ParseUtil.whitespaces.split(line);
            if (split.length > 1 && split[0].startsWith(name)) {
                if (split[0].contains("volt0") || (split[0].contains("volt") && line.contains("current"))) {
                    psVoltage = ParseUtil.parseDoubleOrDefault(split[1], -1.0);
                }
                else if (split[0].contains("current0")) {
                    psAmperage = ParseUtil.parseDoubleOrDefault(split[1], 0.0);
                }
                else if (split[0].contains("temp0")) {
                    psTemperature = ParseUtil.parseDoubleOrDefault(split[1], 0.0);
                }
                else {
                    if (!split[0].contains("watthour") && !split[0].contains("amphour")) {
                        continue;
                    }
                    psCapacityUnits = (split[0].contains("watthour") ? PowerSource.CapacityUnits.MWH : PowerSource.CapacityUnits.MAH);
                    if (line.contains("remaining")) {
                        psCurrentCapacity = (int)(1000.0 * ParseUtil.parseDoubleOrDefault(split[1], 0.0));
                    }
                    else if (line.contains("full")) {
                        psMaxCapacity = (int)(1000.0 * ParseUtil.parseDoubleOrDefault(split[1], 0.0));
                    }
                    else {
                        if (!line.contains("new") && !line.contains("design")) {
                            continue;
                        }
                        psDesignCapacity = (int)(1000.0 * ParseUtil.parseDoubleOrDefault(split[1], 0.0));
                    }
                }
            }
        }
        final int state = ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("apm -b"), 255);
        if (state < 4) {
            psPowerOnLine = true;
            if (state == 3) {
                psCharging = true;
            }
            else {
                final int time = ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("apm -m"), -1);
                psTimeRemainingEstimated = ((time < 0) ? -1.0 : (60.0 * time));
                psDischarging = true;
            }
        }
        final int life = ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("apm -l"), -1);
        if (life > 0) {
            psRemainingCapacityPercent = life / 100.0;
        }
        if (psMaxCapacity < psDesignCapacity && psMaxCapacity < psCurrentCapacity) {
            psMaxCapacity = psDesignCapacity;
        }
        else if (psDesignCapacity < psMaxCapacity && psDesignCapacity < psCurrentCapacity) {
            psDesignCapacity = psMaxCapacity;
        }
        final String psDeviceName = "unknown";
        final String psSerialNumber = "unknown";
        final String psChemistry = "unknown";
        final String psManufacturer = "unknown";
        final double psTimeRemainingInstant = psTimeRemainingEstimated;
        if (psVoltage > 0.0) {
            if (psAmperage > 0.0 && psPowerUsageRate == 0.0) {
                psPowerUsageRate = psAmperage * psVoltage;
            }
            else if (psAmperage == 0.0 && psPowerUsageRate > 0.0) {
                psAmperage = psPowerUsageRate / psVoltage;
            }
        }
        return new OpenBsdPowerSource(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
}
