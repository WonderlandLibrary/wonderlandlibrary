// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractFirmware;

@Immutable
final class AixFirmware extends AbstractFirmware
{
    private final String manufacturer;
    private final String name;
    private final String version;
    
    AixFirmware(final String manufacturer, final String name, final String version) {
        this.manufacturer = manufacturer;
        this.name = name;
        this.version = version;
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
}
