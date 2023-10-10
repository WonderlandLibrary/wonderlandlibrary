// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import oshi.util.tuples.Triplet;
import oshi.util.Util;
import oshi.driver.unix.aix.Lscfg;
import java.util.List;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractBaseboard;

@Immutable
final class AixBaseboard extends AbstractBaseboard
{
    private static final String IBM = "IBM";
    private final String model;
    private final String serialNumber;
    private final String version;
    
    AixBaseboard(final Supplier<List<String>> lscfg) {
        final Triplet<String, String, String> msv = Lscfg.queryBackplaneModelSerialVersion(lscfg.get());
        this.model = (Util.isBlank(msv.getA()) ? "unknown" : msv.getA());
        this.serialNumber = (Util.isBlank(msv.getB()) ? "unknown" : msv.getB());
        this.version = (Util.isBlank(msv.getC()) ? "unknown" : msv.getC());
    }
    
    @Override
    public String getManufacturer() {
        return "IBM";
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
