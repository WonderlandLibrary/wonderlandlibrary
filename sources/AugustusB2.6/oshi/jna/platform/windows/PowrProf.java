// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.windows;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Native;

public interface PowrProf extends com.sun.jna.platform.win32.PowrProf
{
    public static final PowrProf INSTANCE = Native.load("PowrProf", PowrProf.class);
    
    @FieldOrder({ "acOnLine", "batteryPresent", "charging", "discharging", "spare1", "tag", "maxCapacity", "remainingCapacity", "rate", "estimatedTime", "defaultAlert1", "defaultAlert2" })
    public static class SystemBatteryState extends Structure
    {
        public byte acOnLine;
        public byte batteryPresent;
        public byte charging;
        public byte discharging;
        public byte[] spare1;
        public byte tag;
        public int maxCapacity;
        public int remainingCapacity;
        public int rate;
        public int estimatedTime;
        public int defaultAlert1;
        public int defaultAlert2;
        
        public SystemBatteryState(final Pointer p) {
            super(p);
            this.spare1 = new byte[3];
            this.read();
        }
        
        public SystemBatteryState() {
            this.spare1 = new byte[3];
        }
    }
    
    @FieldOrder({ "number", "maxMhz", "currentMhz", "mhzLimit", "maxIdleState", "currentIdleState" })
    public static class ProcessorPowerInformation extends Structure
    {
        public int number;
        public int maxMhz;
        public int currentMhz;
        public int mhzLimit;
        public int maxIdleState;
        public int currentIdleState;
        
        public ProcessorPowerInformation(final Pointer p) {
            super(p);
            this.read();
        }
        
        public ProcessorPowerInformation() {
        }
    }
    
    @FieldOrder({ "BatteryTag", "InformationLevel", "AtRate" })
    public static class BATTERY_QUERY_INFORMATION extends Structure
    {
        public int BatteryTag;
        public int InformationLevel;
        public int AtRate;
    }
    
    public enum BATTERY_QUERY_INFORMATION_LEVEL
    {
        BatteryInformation, 
        BatteryGranularityInformation, 
        BatteryTemperature, 
        BatteryEstimatedTime, 
        BatteryDeviceName, 
        BatteryManufactureDate, 
        BatteryManufactureName, 
        BatteryUniqueID, 
        BatterySerialNumber;
        
        private static /* synthetic */ BATTERY_QUERY_INFORMATION_LEVEL[] $values() {
            return new BATTERY_QUERY_INFORMATION_LEVEL[] { BATTERY_QUERY_INFORMATION_LEVEL.BatteryInformation, BATTERY_QUERY_INFORMATION_LEVEL.BatteryGranularityInformation, BATTERY_QUERY_INFORMATION_LEVEL.BatteryTemperature, BATTERY_QUERY_INFORMATION_LEVEL.BatteryEstimatedTime, BATTERY_QUERY_INFORMATION_LEVEL.BatteryDeviceName, BATTERY_QUERY_INFORMATION_LEVEL.BatteryManufactureDate, BATTERY_QUERY_INFORMATION_LEVEL.BatteryManufactureName, BATTERY_QUERY_INFORMATION_LEVEL.BatteryUniqueID, BATTERY_QUERY_INFORMATION_LEVEL.BatterySerialNumber };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    @FieldOrder({ "Capabilities", "Technology", "Reserved", "Chemistry", "DesignedCapacity", "FullChargedCapacity", "DefaultAlert1", "DefaultAlert2", "CriticalBias", "CycleCount" })
    public static class BATTERY_INFORMATION extends Structure
    {
        public int Capabilities;
        public byte Technology;
        public byte[] Reserved;
        public byte[] Chemistry;
        public int DesignedCapacity;
        public int FullChargedCapacity;
        public int DefaultAlert1;
        public int DefaultAlert2;
        public int CriticalBias;
        public int CycleCount;
        
        public BATTERY_INFORMATION() {
            this.Reserved = new byte[3];
            this.Chemistry = new byte[4];
        }
    }
    
    @FieldOrder({ "BatteryTag", "Timeout", "PowerState", "LowCapacity", "HighCapacity" })
    public static class BATTERY_WAIT_STATUS extends Structure
    {
        public int BatteryTag;
        public int Timeout;
        public int PowerState;
        public int LowCapacity;
        public int HighCapacity;
    }
    
    @FieldOrder({ "PowerState", "Capacity", "Voltage", "Rate" })
    public static class BATTERY_STATUS extends Structure
    {
        public int PowerState;
        public int Capacity;
        public int Voltage;
        public int Rate;
    }
    
    @FieldOrder({ "Day", "Month", "Year" })
    public static class BATTERY_MANUFACTURE_DATE extends Structure
    {
        public byte Day;
        public byte Month;
        public short Year;
    }
}
