// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

import java.util.Iterator;
import java.util.Map;
import oshi.util.ParseUtil;
import java.util.HashMap;
import oshi.util.ExecutingCommand;
import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import oshi.hardware.PowerSource;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractPowerSource;

@ThreadSafe
public final class FreeBsdPowerSource extends AbstractPowerSource
{
    public FreeBsdPowerSource(final String psName, final String psDeviceName, final double psRemainingCapacityPercent, final double psTimeRemainingEstimated, final double psTimeRemainingInstant, final double psPowerUsageRate, final double psVoltage, final double psAmperage, final boolean psPowerOnLine, final boolean psCharging, final boolean psDischarging, final PowerSource.CapacityUnits psCapacityUnits, final int psCurrentCapacity, final int psMaxCapacity, final int psDesignCapacity, final int psCycleCount, final String psChemistry, final LocalDate psManufactureDate, final String psManufacturer, final String psSerialNumber, final double psTemperature) {
        super(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    public static List<PowerSource> getPowerSources() {
        return Arrays.asList(getPowerSource("BAT0"));
    }
    
    private static FreeBsdPowerSource getPowerSource(final String name) {
        final String psName = name;
        double psRemainingCapacityPercent = 1.0;
        double psTimeRemainingEstimated = -1.0;
        double psPowerUsageRate = 0.0;
        int psVoltage = -1;
        double psAmperage = 0.0;
        final boolean psPowerOnLine = false;
        boolean psCharging = false;
        boolean psDischarging = false;
        PowerSource.CapacityUnits psCapacityUnits = PowerSource.CapacityUnits.RELATIVE;
        final int psCurrentCapacity = 0;
        int psMaxCapacity = 1;
        int psDesignCapacity = 1;
        final int psCycleCount = -1;
        final LocalDate psManufactureDate = null;
        final double psTemperature = 0.0;
        final int state = BsdSysctlUtil.sysctl("hw.acpi.battery.state", 0);
        if (state == 2) {
            psCharging = true;
        }
        else {
            final int time = BsdSysctlUtil.sysctl("hw.acpi.battery.time", -1);
            psTimeRemainingEstimated = ((time < 0) ? -1.0 : (60.0 * time));
            if (state == 1) {
                psDischarging = true;
            }
        }
        final int life = BsdSysctlUtil.sysctl("hw.acpi.battery.life", -1);
        if (life > 0) {
            psRemainingCapacityPercent = life / 100.0;
        }
        final List<String> acpiconf = ExecutingCommand.runNative("acpiconf -i 0");
        final Map<String, String> psMap = new HashMap<String, String>();
        for (final String line : acpiconf) {
            final String[] split = line.split(":", 2);
            if (split.length > 1) {
                final String value = split[1].trim();
                if (value.isEmpty()) {
                    continue;
                }
                psMap.put(split[0], value);
            }
        }
        final String psDeviceName = psMap.getOrDefault("Model number", "unknown");
        final String psSerialNumber = psMap.getOrDefault("Serial number", "unknown");
        final String psChemistry = psMap.getOrDefault("Type", "unknown");
        final String psManufacturer = psMap.getOrDefault("OEM info", "unknown");
        String cap = psMap.get("Design capacity");
        if (cap != null) {
            psDesignCapacity = ParseUtil.getFirstIntValue(cap);
            if (cap.toLowerCase().contains("mah")) {
                psCapacityUnits = PowerSource.CapacityUnits.MAH;
            }
            else if (cap.toLowerCase().contains("mwh")) {
                psCapacityUnits = PowerSource.CapacityUnits.MWH;
            }
        }
        cap = psMap.get("Last full capacity");
        if (cap != null) {
            psMaxCapacity = ParseUtil.getFirstIntValue(cap);
        }
        else {
            psMaxCapacity = psDesignCapacity;
        }
        double psTimeRemainingInstant = psTimeRemainingEstimated;
        final String time2 = psMap.get("Remaining time");
        if (time2 != null) {
            final String[] hhmm = time2.split(":");
            if (hhmm.length == 2) {
                psTimeRemainingInstant = 3600.0 * ParseUtil.parseIntOrDefault(hhmm[0], 0) + 60.0 * ParseUtil.parseIntOrDefault(hhmm[1], 0);
            }
        }
        final String rate = psMap.get("Present rate");
        if (rate != null) {
            psPowerUsageRate = ParseUtil.getFirstIntValue(rate);
        }
        final String volts = psMap.get("Present voltage");
        if (volts != null) {
            psVoltage = ParseUtil.getFirstIntValue(volts);
            if (psVoltage != 0) {
                psAmperage = psPowerUsageRate / psVoltage;
            }
        }
        return new FreeBsdPowerSource(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
}
