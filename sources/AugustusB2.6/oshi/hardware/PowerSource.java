// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import java.time.LocalDate;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface PowerSource
{
    String getName();
    
    String getDeviceName();
    
    double getRemainingCapacityPercent();
    
    double getTimeRemainingEstimated();
    
    double getTimeRemainingInstant();
    
    double getPowerUsageRate();
    
    double getVoltage();
    
    double getAmperage();
    
    boolean isPowerOnLine();
    
    boolean isCharging();
    
    boolean isDischarging();
    
    CapacityUnits getCapacityUnits();
    
    int getCurrentCapacity();
    
    int getMaxCapacity();
    
    int getDesignCapacity();
    
    int getCycleCount();
    
    String getChemistry();
    
    LocalDate getManufactureDate();
    
    String getManufacturer();
    
    String getSerialNumber();
    
    double getTemperature();
    
    boolean updateAttributes();
    
    public enum CapacityUnits
    {
        MWH, 
        MAH, 
        RELATIVE;
        
        private static /* synthetic */ CapacityUnits[] $values() {
            return new CapacityUnits[] { CapacityUnits.MWH, CapacityUnits.MAH, CapacityUnits.RELATIVE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
