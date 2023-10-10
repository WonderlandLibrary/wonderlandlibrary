// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import oshi.driver.linux.Lshal;
import oshi.driver.linux.Dmidecode;
import oshi.driver.linux.Lshw;
import oshi.driver.linux.Devicetree;
import oshi.driver.linux.proc.CpuInfo;
import oshi.driver.linux.Sysfs;
import oshi.hardware.Baseboard;
import oshi.hardware.Firmware;
import oshi.util.Memoizer;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractComputerSystem;

@Immutable
final class LinuxComputerSystem extends AbstractComputerSystem
{
    private final Supplier<String> manufacturer;
    private final Supplier<String> model;
    private final Supplier<String> serialNumber;
    private final Supplier<String> uuid;
    
    LinuxComputerSystem() {
        this.manufacturer = Memoizer.memoize(LinuxComputerSystem::queryManufacturer);
        this.model = Memoizer.memoize(LinuxComputerSystem::queryModel);
        this.serialNumber = Memoizer.memoize(LinuxComputerSystem::querySerialNumber);
        this.uuid = Memoizer.memoize(LinuxComputerSystem::queryUUID);
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
    public String getSerialNumber() {
        return this.serialNumber.get();
    }
    
    @Override
    public String getHardwareUUID() {
        return this.uuid.get();
    }
    
    public Firmware createFirmware() {
        return new LinuxFirmware();
    }
    
    public Baseboard createBaseboard() {
        return new LinuxBaseboard();
    }
    
    private static String queryManufacturer() {
        String result = null;
        if ((result = Sysfs.querySystemVendor()) == null && (result = CpuInfo.queryCpuManufacturer()) == null) {
            return "unknown";
        }
        return result;
    }
    
    private static String queryModel() {
        String result = null;
        if ((result = Sysfs.queryProductModel()) == null && (result = Devicetree.queryModel()) == null && (result = Lshw.queryModel()) == null) {
            return "unknown";
        }
        return result;
    }
    
    private static String querySerialNumber() {
        String result = null;
        if ((result = Sysfs.queryProductSerial()) == null && (result = Dmidecode.querySerialNumber()) == null && (result = Lshal.querySerialNumber()) == null && (result = Lshw.querySerialNumber()) == null) {
            return "unknown";
        }
        return result;
    }
    
    private static String queryUUID() {
        String result = null;
        if ((result = Sysfs.queryUUID()) == null && (result = Dmidecode.queryUUID()) == null && (result = Lshal.queryUUID()) == null && (result = Lshw.queryUUID()) == null) {
            return "unknown";
        }
        return result;
    }
}
