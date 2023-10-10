// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix;

import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractBaseboard;

@Immutable
public final class UnixBaseboard extends AbstractBaseboard
{
    private final String manufacturer;
    private final String model;
    private final String serialNumber;
    private final String version;
    
    public UnixBaseboard(final String manufacturer, final String model, final String serialNumber, final String version) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serialNumber;
        this.version = version;
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }
    
    @Override
    public String getModel() {
        return this.model;
    }
    
    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
}
