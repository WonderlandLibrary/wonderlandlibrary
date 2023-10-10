// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import oshi.util.ParseUtil;
import java.io.File;
import com.sun.jna.platform.linux.Udev;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import oshi.hardware.PowerSource;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractPowerSource;

@ThreadSafe
public final class LinuxPowerSource extends AbstractPowerSource
{
    public LinuxPowerSource(final String psName, final String psDeviceName, final double psRemainingCapacityPercent, final double psTimeRemainingEstimated, final double psTimeRemainingInstant, final double psPowerUsageRate, final double psVoltage, final double psAmperage, final boolean psPowerOnLine, final boolean psCharging, final boolean psDischarging, final PowerSource.CapacityUnits psCapacityUnits, final int psCurrentCapacity, final int psMaxCapacity, final int psDesignCapacity, final int psCycleCount, final String psChemistry, final LocalDate psManufactureDate, final String psManufacturer, final String psSerialNumber, final double psTemperature) {
        super(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    public static List<PowerSource> getPowerSources() {
        double psRemainingCapacityPercent = -1.0;
        final double psTimeRemainingEstimated = -1.0;
        final double psTimeRemainingInstant = -1.0;
        double psPowerUsageRate = 0.0;
        double psVoltage = -1.0;
        double psAmperage = 0.0;
        final boolean psPowerOnLine = false;
        boolean psCharging = false;
        boolean psDischarging = false;
        final PowerSource.CapacityUnits psCapacityUnits = PowerSource.CapacityUnits.RELATIVE;
        int psCurrentCapacity = -1;
        int psMaxCapacity = -1;
        int psDesignCapacity = -1;
        int psCycleCount = -1;
        final LocalDate psManufactureDate = null;
        final double psTemperature = 0.0;
        final List<PowerSource> psList = new ArrayList<PowerSource>();
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        try {
            final Udev.UdevEnumerate enumerate = udev.enumerateNew();
            try {
                enumerate.addMatchSubsystem("power_supply");
                enumerate.scanDevices();
                for (Udev.UdevListEntry entry = enumerate.getListEntry(); entry != null; entry = entry.getNext()) {
                    final String syspath = entry.getName();
                    final String name = syspath.substring(syspath.lastIndexOf(File.separatorChar) + 1);
                    if (!name.startsWith("ADP") && !name.startsWith("AC")) {
                        final Udev.UdevDevice device = udev.deviceNewFromSyspath(syspath);
                        if (device != null) {
                            try {
                                if (ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_PRESENT"), 1) > 0) {
                                    final String psName = getOrDefault(device, "POWER_SUPPLY_NAME", name);
                                    final String status = device.getPropertyValue("POWER_SUPPLY_STATUS");
                                    psCharging = "Charging".equals(status);
                                    psDischarging = "Discharging".equals(status);
                                    psRemainingCapacityPercent = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_CAPACITY"), -100) / 100.0;
                                    psCurrentCapacity = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_ENERGY_NOW"), -1);
                                    if (psCurrentCapacity < 0) {
                                        psCurrentCapacity = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_CHARGE_NOW"), -1);
                                    }
                                    psMaxCapacity = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_ENERGY_FULL"), 1);
                                    if (psMaxCapacity < 0) {
                                        psMaxCapacity = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_CHARGE_FULL"), 1);
                                    }
                                    psDesignCapacity = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_ENERGY_FULL_DESIGN"), 1);
                                    if (psDesignCapacity < 0) {
                                        psDesignCapacity = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_CHARGE_FULL_DESIGN"), 1);
                                    }
                                    psVoltage = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_VOLTAGE_NOW"), -1);
                                    if (psVoltage > 0.0) {
                                        final String power = device.getPropertyValue("POWER_SUPPLY_POWER_NOW");
                                        final String current = device.getPropertyValue("POWER_SUPPLY_CURRENT_NOW");
                                        if (power == null) {
                                            psAmperage = ParseUtil.parseIntOrDefault(current, 0);
                                            psPowerUsageRate = psAmperage * psVoltage;
                                        }
                                        else if (current == null) {
                                            psPowerUsageRate = ParseUtil.parseIntOrDefault(power, 0);
                                            psAmperage = psPowerUsageRate / psVoltage;
                                        }
                                        else {
                                            psAmperage = ParseUtil.parseIntOrDefault(current, 0);
                                            psPowerUsageRate = ParseUtil.parseIntOrDefault(power, 0);
                                        }
                                    }
                                    psCycleCount = ParseUtil.parseIntOrDefault(device.getPropertyValue("POWER_SUPPLY_CYCLE_COUNT"), -1);
                                    final String psChemistry = getOrDefault(device, "POWER_SUPPLY_TECHNOLOGY", "unknown");
                                    final String psDeviceName = getOrDefault(device, "POWER_SUPPLY_MODEL_NAME", "unknown");
                                    final String psManufacturer = getOrDefault(device, "POWER_SUPPLY_MANUFACTURER", "unknown");
                                    final String psSerialNumber = getOrDefault(device, "POWER_SUPPLY_SERIAL_NUMBER", "unknown");
                                    psList.add(new LinuxPowerSource(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature));
                                }
                            }
                            finally {
                                device.unref();
                            }
                        }
                    }
                }
            }
            finally {
                enumerate.unref();
            }
        }
        finally {
            udev.unref();
        }
        return psList;
    }
    
    private static String getOrDefault(final Udev.UdevDevice device, final String property, final String def) {
        final String value = device.getPropertyValue(property);
        return (value == null) ? def : value;
    }
}
