// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import oshi.driver.linux.Sysfs;
import oshi.driver.linux.proc.CpuInfo;
import oshi.util.Memoizer;
import oshi.util.tuples.Quartet;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractBaseboard;

@Immutable
final class LinuxBaseboard extends AbstractBaseboard
{
    private final Supplier<String> manufacturer;
    private final Supplier<String> model;
    private final Supplier<String> version;
    private final Supplier<String> serialNumber;
    private final Supplier<Quartet<String, String, String, String>> manufacturerModelVersionSerial;
    
    LinuxBaseboard() {
        this.manufacturer = Memoizer.memoize(this::queryManufacturer);
        this.model = Memoizer.memoize(this::queryModel);
        this.version = Memoizer.memoize(this::queryVersion);
        this.serialNumber = Memoizer.memoize(this::querySerialNumber);
        this.manufacturerModelVersionSerial = Memoizer.memoize(CpuInfo::queryBoardInfo);
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturer.get();
    }
    
    @Override
    public String getModel() {
        return this.model.get();
    }
    
    @Override
    public String getVersion() {
        return this.version.get();
    }
    
    @Override
    public String getSerialNumber() {
        return this.serialNumber.get();
    }
    
    private String queryManufacturer() {
        String result = null;
        if ((result = Sysfs.queryBoardVendor()) == null && (result = this.manufacturerModelVersionSerial.get().getA()) == null) {
            return "unknown";
        }
        return result;
    }
    
    private String queryModel() {
        String result = null;
        if ((result = Sysfs.queryBoardModel()) == null && (result = this.manufacturerModelVersionSerial.get().getB()) == null) {
            return "unknown";
        }
        return result;
    }
    
    private String queryVersion() {
        String result = null;
        if ((result = Sysfs.queryBoardVersion()) == null && (result = this.manufacturerModelVersionSerial.get().getC()) == null) {
            return "unknown";
        }
        return result;
    }
    
    private String querySerialNumber() {
        String result = null;
        if ((result = Sysfs.queryBoardSerial()) == null && (result = this.manufacturerModelVersionSerial.get().getD()) == null) {
            return "unknown";
        }
        return result;
    }
}
