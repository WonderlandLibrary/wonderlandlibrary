// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.util.Memoizer;
import oshi.hardware.Baseboard;
import oshi.hardware.Firmware;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.ComputerSystem;

@Immutable
public abstract class AbstractComputerSystem implements ComputerSystem
{
    private final Supplier<Firmware> firmware;
    private final Supplier<Baseboard> baseboard;
    
    public AbstractComputerSystem() {
        this.firmware = Memoizer.memoize(this::createFirmware);
        this.baseboard = Memoizer.memoize(this::createBaseboard);
    }
    
    @Override
    public Firmware getFirmware() {
        return this.firmware.get();
    }
    
    protected abstract Firmware createFirmware();
    
    @Override
    public Baseboard getBaseboard() {
        return this.baseboard.get();
    }
    
    protected abstract Baseboard createBaseboard();
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("manufacturer=").append(this.getManufacturer()).append(", ");
        sb.append("model=").append(this.getModel()).append(", ");
        sb.append("serial number=").append(this.getSerialNumber()).append(", ");
        sb.append("uuid=").append(this.getHardwareUUID());
        return sb.toString();
    }
}
