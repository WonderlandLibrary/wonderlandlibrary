// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractFirmware;

@Immutable
final class SolarisFirmware extends AbstractFirmware
{
    private final String manufacturer;
    private final String version;
    private final String releaseDate;
    
    SolarisFirmware(final String manufacturer, final String version, final String releaseDate) {
        this.manufacturer = manufacturer;
        this.version = version;
        this.releaseDate = releaseDate;
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
    
    @Override
    public String getReleaseDate() {
        return this.releaseDate;
    }
}
