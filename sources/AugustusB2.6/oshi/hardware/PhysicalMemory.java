// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import oshi.util.FormatUtil;
import oshi.annotation.concurrent.Immutable;

@Immutable
public class PhysicalMemory
{
    private final String bankLabel;
    private final long capacity;
    private final long clockSpeed;
    private final String manufacturer;
    private final String memoryType;
    
    public PhysicalMemory(final String bankLabel, final long capacity, final long clockSpeed, final String manufacturer, final String memoryType) {
        this.bankLabel = bankLabel;
        this.capacity = capacity;
        this.clockSpeed = clockSpeed;
        this.manufacturer = manufacturer;
        this.memoryType = memoryType;
    }
    
    public String getBankLabel() {
        return this.bankLabel;
    }
    
    public long getCapacity() {
        return this.capacity;
    }
    
    public long getClockSpeed() {
        return this.clockSpeed;
    }
    
    public String getManufacturer() {
        return this.manufacturer;
    }
    
    public String getMemoryType() {
        return this.memoryType;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Bank label: " + this.getBankLabel());
        sb.append(", Capacity: " + FormatUtil.formatBytes(this.getCapacity()));
        sb.append(", Clock speed: " + FormatUtil.formatHertz(this.getClockSpeed()));
        sb.append(", Manufacturer: " + this.getManufacturer());
        sb.append(", Memory type: " + this.getMemoryType());
        return sb.toString();
    }
}
