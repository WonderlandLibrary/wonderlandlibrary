// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import com.sun.jna.Platform;
import oshi.hardware.platform.unix.aix.AixPowerSource;
import oshi.hardware.platform.unix.freebsd.FreeBsdPowerSource;
import oshi.hardware.platform.unix.solaris.SolarisPowerSource;
import oshi.hardware.platform.linux.LinuxPowerSource;
import oshi.hardware.platform.mac.MacPowerSource;
import oshi.hardware.platform.windows.WindowsPowerSource;
import oshi.SystemInfo;
import java.util.Iterator;
import java.util.List;
import java.time.LocalDate;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.PowerSource;

@ThreadSafe
public abstract class AbstractPowerSource implements PowerSource
{
    private String name;
    private String deviceName;
    private double remainingCapacityPercent;
    private double timeRemainingEstimated;
    private double timeRemainingInstant;
    private double powerUsageRate;
    private double voltage;
    private double amperage;
    private boolean powerOnLine;
    private boolean charging;
    private boolean discharging;
    private CapacityUnits capacityUnits;
    private int currentCapacity;
    private int maxCapacity;
    private int designCapacity;
    private int cycleCount;
    private String chemistry;
    private LocalDate manufactureDate;
    private String manufacturer;
    private String serialNumber;
    private double temperature;
    
    protected AbstractPowerSource(final String name, final String deviceName, final double remainingCapacityPercent, final double timeRemainingEstimated, final double timeRemainingInstant, final double powerUsageRate, final double voltage, final double amperage, final boolean powerOnLine, final boolean charging, final boolean discharging, final CapacityUnits capacityUnits, final int currentCapacity, final int maxCapacity, final int designCapacity, final int cycleCount, final String chemistry, final LocalDate manufactureDate, final String manufacturer, final String serialNumber, final double temperature) {
        this.name = name;
        this.deviceName = deviceName;
        this.remainingCapacityPercent = remainingCapacityPercent;
        this.timeRemainingEstimated = timeRemainingEstimated;
        this.timeRemainingInstant = timeRemainingInstant;
        this.powerUsageRate = powerUsageRate;
        this.voltage = voltage;
        this.amperage = amperage;
        this.powerOnLine = powerOnLine;
        this.charging = charging;
        this.discharging = discharging;
        this.capacityUnits = capacityUnits;
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.designCapacity = designCapacity;
        this.cycleCount = cycleCount;
        this.chemistry = chemistry;
        this.manufactureDate = manufactureDate;
        this.manufacturer = manufacturer;
        this.serialNumber = serialNumber;
        this.temperature = temperature;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getDeviceName() {
        return this.deviceName;
    }
    
    @Override
    public double getRemainingCapacityPercent() {
        return this.remainingCapacityPercent;
    }
    
    @Override
    public double getTimeRemainingEstimated() {
        return this.timeRemainingEstimated;
    }
    
    @Override
    public double getTimeRemainingInstant() {
        return this.timeRemainingInstant;
    }
    
    @Override
    public double getPowerUsageRate() {
        return this.powerUsageRate;
    }
    
    @Override
    public double getVoltage() {
        return this.voltage;
    }
    
    @Override
    public double getAmperage() {
        return this.amperage;
    }
    
    @Override
    public boolean isPowerOnLine() {
        return this.powerOnLine;
    }
    
    @Override
    public boolean isCharging() {
        return this.charging;
    }
    
    @Override
    public boolean isDischarging() {
        return this.discharging;
    }
    
    @Override
    public CapacityUnits getCapacityUnits() {
        return this.capacityUnits;
    }
    
    @Override
    public int getCurrentCapacity() {
        return this.currentCapacity;
    }
    
    @Override
    public int getMaxCapacity() {
        return this.maxCapacity;
    }
    
    @Override
    public int getDesignCapacity() {
        return this.designCapacity;
    }
    
    @Override
    public int getCycleCount() {
        return this.cycleCount;
    }
    
    @Override
    public String getChemistry() {
        return this.chemistry;
    }
    
    @Override
    public LocalDate getManufactureDate() {
        return this.manufactureDate;
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }
    
    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }
    
    @Override
    public double getTemperature() {
        return this.temperature;
    }
    
    @Override
    public boolean updateAttributes() {
        final List<PowerSource> psArr = getPowerSources();
        for (final PowerSource ps : psArr) {
            if (ps.getName().equals(this.name)) {
                this.name = ps.getName();
                this.deviceName = ps.getDeviceName();
                this.remainingCapacityPercent = ps.getRemainingCapacityPercent();
                this.timeRemainingEstimated = ps.getTimeRemainingEstimated();
                this.timeRemainingInstant = ps.getTimeRemainingInstant();
                this.powerUsageRate = ps.getPowerUsageRate();
                this.voltage = ps.getVoltage();
                this.amperage = ps.getAmperage();
                this.powerOnLine = ps.isPowerOnLine();
                this.charging = ps.isCharging();
                this.discharging = ps.isDischarging();
                this.capacityUnits = ps.getCapacityUnits();
                this.currentCapacity = ps.getCurrentCapacity();
                this.maxCapacity = ps.getMaxCapacity();
                this.designCapacity = ps.getDesignCapacity();
                this.cycleCount = ps.getCycleCount();
                this.chemistry = ps.getChemistry();
                this.manufactureDate = ps.getManufactureDate();
                this.manufacturer = ps.getManufacturer();
                this.serialNumber = ps.getSerialNumber();
                this.temperature = ps.getTemperature();
                return true;
            }
        }
        return false;
    }
    
    private static List<PowerSource> getPowerSources() {
        switch (SystemInfo.getCurrentPlatform()) {
            case WINDOWS: {
                return WindowsPowerSource.getPowerSources();
            }
            case MACOS: {
                return MacPowerSource.getPowerSources();
            }
            case LINUX: {
                return LinuxPowerSource.getPowerSources();
            }
            case SOLARIS: {
                return SolarisPowerSource.getPowerSources();
            }
            case FREEBSD: {
                return FreeBsdPowerSource.getPowerSources();
            }
            case AIX: {
                return AixPowerSource.getPowerSources();
            }
            default: {
                throw new UnsupportedOperationException("Operating system not supported: " + Platform.getOSType());
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(this.getName()).append(", ");
        sb.append("Device Name: ").append(this.getDeviceName()).append(",\n ");
        sb.append("RemainingCapacityPercent: ").append(this.getRemainingCapacityPercent() * 100.0).append("%, ");
        sb.append("Time Remaining: ").append(formatTimeRemaining(this.getTimeRemainingEstimated())).append(", ");
        sb.append("Time Remaining Instant: ").append(formatTimeRemaining(this.getTimeRemainingInstant())).append(",\n ");
        sb.append("Power Usage Rate: ").append(this.getPowerUsageRate()).append("mW, ");
        sb.append("Voltage: ");
        if (this.getVoltage() > 0.0) {
            sb.append(this.getVoltage()).append("V, ");
        }
        else {
            sb.append("unknown");
        }
        sb.append("Amperage: ").append(this.getAmperage()).append("mA,\n ");
        sb.append("Power OnLine: ").append(this.isPowerOnLine()).append(", ");
        sb.append("Charging: ").append(this.isCharging()).append(", ");
        sb.append("Discharging: ").append(this.isDischarging()).append(",\n ");
        sb.append("Capacity Units: ").append(this.getCapacityUnits()).append(", ");
        sb.append("Current Capacity: ").append(this.getCurrentCapacity()).append(", ");
        sb.append("Max Capacity: ").append(this.getMaxCapacity()).append(", ");
        sb.append("Design Capacity: ").append(this.getDesignCapacity()).append(",\n ");
        sb.append("Cycle Count: ").append(this.getCycleCount()).append(", ");
        sb.append("Chemistry: ").append(this.getChemistry()).append(", ");
        sb.append("Manufacture Date: ").append((this.getManufactureDate() != null) ? this.getManufactureDate() : "unknown").append(", ");
        sb.append("Manufacturer: ").append(this.getManufacturer()).append(",\n ");
        sb.append("SerialNumber: ").append(this.getSerialNumber()).append(", ");
        sb.append("Temperature: ");
        if (this.getTemperature() > 0.0) {
            sb.append(this.getTemperature()).append("°C");
        }
        else {
            sb.append("unknown");
        }
        return sb.toString();
    }
    
    private static String formatTimeRemaining(final double timeInSeconds) {
        String formattedTimeRemaining;
        if (timeInSeconds < -1.5) {
            formattedTimeRemaining = "Charging";
        }
        else if (timeInSeconds < 0.0) {
            formattedTimeRemaining = "Unknown";
        }
        else {
            final int hours = (int)(timeInSeconds / 3600.0);
            final int minutes = (int)(timeInSeconds % 3600.0 / 60.0);
            formattedTimeRemaining = String.format("%d:%02d", hours, minutes);
        }
        return formattedTimeRemaining;
    }
}
