// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import oshi.util.platform.mac.CFUtil;
import com.sun.jna.PointerType;
import java.util.ArrayList;
import com.sun.jna.platform.mac.IOKitUtil;
import java.util.List;
import java.time.LocalDate;
import oshi.hardware.PowerSource;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.CoreFoundation;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractPowerSource;

@ThreadSafe
public final class MacPowerSource extends AbstractPowerSource
{
    private static final CoreFoundation CF;
    private static final IOKit IO;
    
    public MacPowerSource(final String psName, final String psDeviceName, final double psRemainingCapacityPercent, final double psTimeRemainingEstimated, final double psTimeRemainingInstant, final double psPowerUsageRate, final double psVoltage, final double psAmperage, final boolean psPowerOnLine, final boolean psCharging, final boolean psDischarging, final PowerSource.CapacityUnits psCapacityUnits, final int psCurrentCapacity, final int psMaxCapacity, final int psDesignCapacity, final int psCycleCount, final String psChemistry, final LocalDate psManufactureDate, final String psManufacturer, final String psSerialNumber, final double psTemperature) {
        super(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    public static List<PowerSource> getPowerSources() {
        String psDeviceName = "unknown";
        double psTimeRemainingInstant = 0.0;
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
        int psCycleCount = -1;
        final String psChemistry = "unknown";
        LocalDate psManufactureDate = null;
        String psManufacturer = "unknown";
        String psSerialNumber = "unknown";
        double psTemperature = 0.0;
        final IOKit.IORegistryEntry smartBattery = IOKitUtil.getMatchingService("AppleSmartBattery");
        if (smartBattery != null) {
            String s = smartBattery.getStringProperty("DeviceName");
            if (s != null) {
                psDeviceName = s;
            }
            s = smartBattery.getStringProperty("Manufacturer");
            if (s != null) {
                psManufacturer = s;
            }
            s = smartBattery.getStringProperty("BatterySerialNumber");
            if (s != null) {
                psSerialNumber = s;
            }
            Integer temp = smartBattery.getIntegerProperty("ManufactureDate");
            if (temp != null) {
                final int day = temp & 0x1F;
                final int month = temp >> 5 & 0xF;
                final int year80 = temp >> 9 & 0x7F;
                psManufactureDate = LocalDate.of(1980 + year80, month, day);
            }
            temp = smartBattery.getIntegerProperty("DesignCapacity");
            if (temp != null) {
                psDesignCapacity = temp;
            }
            temp = smartBattery.getIntegerProperty("MaxCapacity");
            if (temp != null) {
                psMaxCapacity = temp;
            }
            temp = smartBattery.getIntegerProperty("CurrentCapacity");
            if (temp != null) {
                psCurrentCapacity = temp;
            }
            psCapacityUnits = PowerSource.CapacityUnits.MAH;
            temp = smartBattery.getIntegerProperty("TimeRemaining");
            if (temp != null) {
                psTimeRemainingInstant = temp * 60.0;
            }
            temp = smartBattery.getIntegerProperty("CycleCount");
            if (temp != null) {
                psCycleCount = temp;
            }
            temp = smartBattery.getIntegerProperty("Temperature");
            if (temp != null) {
                psTemperature = temp / 100.0;
            }
            temp = smartBattery.getIntegerProperty("Voltage");
            if (temp != null) {
                psVoltage = temp / 1000.0;
            }
            temp = smartBattery.getIntegerProperty("Amperage");
            if (temp != null) {
                psAmperage = temp;
            }
            psPowerUsageRate = psVoltage * psAmperage;
            Boolean bool = smartBattery.getBooleanProperty("ExternalConnected");
            if (bool != null) {
                psPowerOnLine = bool;
            }
            bool = smartBattery.getBooleanProperty("IsCharging");
            if (bool != null) {
                psCharging = bool;
            }
            psDischarging = !psCharging;
            smartBattery.release();
        }
        final CoreFoundation.CFTypeRef powerSourcesInfo = MacPowerSource.IO.IOPSCopyPowerSourcesInfo();
        final CoreFoundation.CFArrayRef powerSourcesList = MacPowerSource.IO.IOPSCopyPowerSourcesList(powerSourcesInfo);
        final int powerSourcesCount = powerSourcesList.getCount();
        final double psTimeRemainingEstimated = MacPowerSource.IO.IOPSGetTimeRemainingEstimate();
        final CoreFoundation.CFStringRef nameKey = CoreFoundation.CFStringRef.createCFString("Name");
        final CoreFoundation.CFStringRef isPresentKey = CoreFoundation.CFStringRef.createCFString("Is Present");
        final CoreFoundation.CFStringRef currentCapacityKey = CoreFoundation.CFStringRef.createCFString("Current Capacity");
        final CoreFoundation.CFStringRef maxCapacityKey = CoreFoundation.CFStringRef.createCFString("Max Capacity");
        final List<PowerSource> psList = new ArrayList<PowerSource>(powerSourcesCount);
        for (int ps = 0; ps < powerSourcesCount; ++ps) {
            final Pointer pwrSrcPtr = powerSourcesList.getValueAtIndex(ps);
            final CoreFoundation.CFTypeRef powerSource = new CoreFoundation.CFTypeRef();
            powerSource.setPointer(pwrSrcPtr);
            final CoreFoundation.CFDictionaryRef dictionary = MacPowerSource.IO.IOPSGetPowerSourceDescription(powerSourcesInfo, powerSource);
            Pointer result = dictionary.getValue(isPresentKey);
            if (result != null) {
                final CoreFoundation.CFBooleanRef isPresentRef = new CoreFoundation.CFBooleanRef(result);
                if (0 != MacPowerSource.CF.CFBooleanGetValue(isPresentRef)) {
                    result = dictionary.getValue(nameKey);
                    final String psName = CFUtil.cfPointerToString(result);
                    double currentCapacity = 0.0;
                    if (dictionary.getValueIfPresent(currentCapacityKey, null)) {
                        result = dictionary.getValue(currentCapacityKey);
                        final CoreFoundation.CFNumberRef cap = new CoreFoundation.CFNumberRef(result);
                        currentCapacity = cap.intValue();
                    }
                    double maxCapacity = 1.0;
                    if (dictionary.getValueIfPresent(maxCapacityKey, null)) {
                        result = dictionary.getValue(maxCapacityKey);
                        final CoreFoundation.CFNumberRef cap2 = new CoreFoundation.CFNumberRef(result);
                        maxCapacity = cap2.intValue();
                    }
                    final double psRemainingCapacityPercent = Math.min(1.0, currentCapacity / maxCapacity);
                    psList.add(new MacPowerSource(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature));
                }
            }
        }
        isPresentKey.release();
        nameKey.release();
        currentCapacityKey.release();
        maxCapacityKey.release();
        powerSourcesList.release();
        powerSourcesInfo.release();
        return psList;
    }
    
    static {
        CF = CoreFoundation.INSTANCE;
        IO = IOKit.INSTANCE;
    }
}
