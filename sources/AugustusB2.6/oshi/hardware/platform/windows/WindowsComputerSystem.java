// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import oshi.driver.windows.wmi.Win32Bios;
import oshi.driver.windows.wmi.Win32ComputerSystemProduct;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.Util;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32ComputerSystem;
import oshi.hardware.Baseboard;
import oshi.hardware.Firmware;
import oshi.util.Memoizer;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractComputerSystem;

@Immutable
final class WindowsComputerSystem extends AbstractComputerSystem
{
    private final Supplier<Pair<String, String>> manufacturerModel;
    private final Supplier<Pair<String, String>> serialNumberUUID;
    
    WindowsComputerSystem() {
        this.manufacturerModel = Memoizer.memoize(WindowsComputerSystem::queryManufacturerModel);
        this.serialNumberUUID = Memoizer.memoize(WindowsComputerSystem::querySystemSerialNumberUUID);
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturerModel.get().getA();
    }
    
    @Override
    public String getModel() {
        return this.manufacturerModel.get().getB();
    }
    
    @Override
    public String getSerialNumber() {
        return this.serialNumberUUID.get().getA();
    }
    
    @Override
    public String getHardwareUUID() {
        return this.serialNumberUUID.get().getB();
    }
    
    public Firmware createFirmware() {
        return new WindowsFirmware();
    }
    
    public Baseboard createBaseboard() {
        return new WindowsBaseboard();
    }
    
    private static Pair<String, String> queryManufacturerModel() {
        String manufacturer = null;
        String model = null;
        final WbemcliUtil.WmiResult<Win32ComputerSystem.ComputerSystemProperty> win32ComputerSystem = Win32ComputerSystem.queryComputerSystem();
        if (win32ComputerSystem.getResultCount() > 0) {
            manufacturer = WmiUtil.getString(win32ComputerSystem, Win32ComputerSystem.ComputerSystemProperty.MANUFACTURER, 0);
            model = WmiUtil.getString(win32ComputerSystem, Win32ComputerSystem.ComputerSystemProperty.MODEL, 0);
        }
        return new Pair<String, String>(Util.isBlank(manufacturer) ? "unknown" : manufacturer, Util.isBlank(model) ? "unknown" : model);
    }
    
    private static Pair<String, String> querySystemSerialNumberUUID() {
        String serialNumber = null;
        String uuid = null;
        final WbemcliUtil.WmiResult<Win32ComputerSystemProduct.ComputerSystemProductProperty> win32ComputerSystemProduct = Win32ComputerSystemProduct.queryIdentifyingNumberUUID();
        if (win32ComputerSystemProduct.getResultCount() > 0) {
            serialNumber = WmiUtil.getString(win32ComputerSystemProduct, Win32ComputerSystemProduct.ComputerSystemProductProperty.IDENTIFYINGNUMBER, 0);
            uuid = WmiUtil.getString(win32ComputerSystemProduct, Win32ComputerSystemProduct.ComputerSystemProductProperty.UUID, 0);
        }
        if (Util.isBlank(serialNumber)) {
            serialNumber = querySerialFromBios();
        }
        if (Util.isBlank(serialNumber)) {
            serialNumber = "unknown";
        }
        if (Util.isBlank(uuid)) {
            uuid = "unknown";
        }
        return new Pair<String, String>(serialNumber, uuid);
    }
    
    private static String querySerialFromBios() {
        final WbemcliUtil.WmiResult<Win32Bios.BiosSerialProperty> serialNum = Win32Bios.querySerialNumber();
        if (serialNum.getResultCount() > 0) {
            return WmiUtil.getString(serialNum, Win32Bios.BiosSerialProperty.SERIALNUMBER, 0);
        }
        return null;
    }
}
