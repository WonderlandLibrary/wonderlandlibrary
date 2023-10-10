// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import com.sun.jna.platform.unix.solaris.LibKstat;
import oshi.util.platform.unix.solaris.KstatUtil;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import oshi.hardware.PowerSource;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractPowerSource;

@ThreadSafe
public final class SolarisPowerSource extends AbstractPowerSource
{
    private static final String[] KSTAT_BATT_MOD;
    private static final int KSTAT_BATT_IDX;
    
    public SolarisPowerSource(final String psName, final String psDeviceName, final double psRemainingCapacityPercent, final double psTimeRemainingEstimated, final double psTimeRemainingInstant, final double psPowerUsageRate, final double psVoltage, final double psAmperage, final boolean psPowerOnLine, final boolean psCharging, final boolean psDischarging, final PowerSource.CapacityUnits psCapacityUnits, final int psCurrentCapacity, final int psMaxCapacity, final int psDesignCapacity, final int psCycleCount, final String psChemistry, final LocalDate psManufactureDate, final String psManufacturer, final String psSerialNumber, final double psTemperature) {
        super(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    public static List<PowerSource> getPowerSources() {
        return Arrays.asList(getPowerSource("BAT0"));
    }
    
    private static SolarisPowerSource getPowerSource(final String name) {
        final String psName = name;
        String psDeviceName = "unknown";
        final double psRemainingCapacityPercent = 1.0;
        double psTimeRemainingEstimated = -1.0;
        final double psTimeRemainingInstant = 0.0;
        final double psPowerUsageRate = 0.0;
        double psVoltage = -1.0;
        double psAmperage = 0.0;
        final boolean psPowerOnLine = false;
        final boolean psCharging = false;
        final boolean psDischarging = false;
        PowerSource.CapacityUnits psCapacityUnits = PowerSource.CapacityUnits.RELATIVE;
        int psCurrentCapacity = 0;
        int psMaxCapacity = 1;
        final int psDesignCapacity = 1;
        final int psCycleCount = -1;
        String psChemistry = "unknown";
        final LocalDate psManufactureDate = null;
        String psManufacturer = "unknown";
        String psSerialNumber = "unknown";
        final double psTemperature = 0.0;
        if (SolarisPowerSource.KSTAT_BATT_IDX > 0) {
            final KstatUtil.KstatChain kc = KstatUtil.openChain();
            try {
                LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup(SolarisPowerSource.KSTAT_BATT_MOD[SolarisPowerSource.KSTAT_BATT_IDX], 0, "battery BIF0");
                if (ksp != null) {
                    long energyFull = KstatUtil.dataLookupLong(ksp, "bif_last_cap");
                    if (energyFull == -1L || energyFull <= 0L) {
                        energyFull = KstatUtil.dataLookupLong(ksp, "bif_design_cap");
                    }
                    if (energyFull != -1L && energyFull > 0L) {
                        psMaxCapacity = (int)energyFull;
                    }
                    final long unit = KstatUtil.dataLookupLong(ksp, "bif_unit");
                    if (unit == 0L) {
                        psCapacityUnits = PowerSource.CapacityUnits.MWH;
                    }
                    else if (unit == 1L) {
                        psCapacityUnits = PowerSource.CapacityUnits.MAH;
                    }
                    psDeviceName = KstatUtil.dataLookupString(ksp, "bif_model");
                    psSerialNumber = KstatUtil.dataLookupString(ksp, "bif_serial");
                    psChemistry = KstatUtil.dataLookupString(ksp, "bif_type");
                    psManufacturer = KstatUtil.dataLookupString(ksp, "bif_oem_info");
                }
                ksp = KstatUtil.KstatChain.lookup(SolarisPowerSource.KSTAT_BATT_MOD[SolarisPowerSource.KSTAT_BATT_IDX], 0, "battery BST0");
                if (ksp != null) {
                    final long energyNow = KstatUtil.dataLookupLong(ksp, "bst_rem_cap");
                    if (energyNow >= 0L) {
                        psCurrentCapacity = (int)energyNow;
                    }
                    long powerNow = KstatUtil.dataLookupLong(ksp, "bst_rate");
                    if (powerNow == -1L) {
                        powerNow = 0L;
                    }
                    final boolean isCharging = (KstatUtil.dataLookupLong(ksp, "bst_state") & 0x10L) > 0L;
                    if (!isCharging) {
                        psTimeRemainingEstimated = ((powerNow > 0L) ? (3600.0 * energyNow / powerNow) : -1.0);
                    }
                    final long voltageNow = KstatUtil.dataLookupLong(ksp, "bst_voltage");
                    if (voltageNow > 0L) {
                        psVoltage = voltageNow / 1000.0;
                        psAmperage = psPowerUsageRate * 1000.0 / voltageNow;
                    }
                }
                if (kc != null) {
                    kc.close();
                }
            }
            catch (Throwable t) {
                if (kc != null) {
                    try {
                        kc.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        return new SolarisPowerSource(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    static {
        KSTAT_BATT_MOD = new String[] { null, "battery", "acpi_drv" };
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            if (KstatUtil.KstatChain.lookup(SolarisPowerSource.KSTAT_BATT_MOD[1], 0, null) != null) {
                KSTAT_BATT_IDX = 1;
            }
            else if (KstatUtil.KstatChain.lookup(SolarisPowerSource.KSTAT_BATT_MOD[2], 0, null) != null) {
                KSTAT_BATT_IDX = 2;
            }
            else {
                KSTAT_BATT_IDX = 0;
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
}
