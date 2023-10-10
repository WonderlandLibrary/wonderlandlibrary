// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import oshi.util.Util;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.hardware.Baseboard;
import oshi.hardware.Firmware;
import oshi.util.Memoizer;
import java.util.List;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractComputerSystem;

@Immutable
final class AixComputerSystem extends AbstractComputerSystem
{
    private final Supplier<LsattrStrings> lsattrStrings;
    private final Supplier<List<String>> lscfg;
    
    AixComputerSystem(final Supplier<List<String>> lscfg) {
        this.lsattrStrings = Memoizer.memoize(AixComputerSystem::readLsattr);
        this.lscfg = lscfg;
    }
    
    @Override
    public String getManufacturer() {
        return this.lsattrStrings.get().manufacturer;
    }
    
    @Override
    public String getModel() {
        return this.lsattrStrings.get().model;
    }
    
    @Override
    public String getSerialNumber() {
        return this.lsattrStrings.get().serialNumber;
    }
    
    @Override
    public String getHardwareUUID() {
        return this.lsattrStrings.get().uuid;
    }
    
    public Firmware createFirmware() {
        return new AixFirmware(this.lsattrStrings.get().biosVendor, this.lsattrStrings.get().biosPlatformVersion, this.lsattrStrings.get().biosVersion);
    }
    
    public Baseboard createBaseboard() {
        return new AixBaseboard(this.lscfg);
    }
    
    private static LsattrStrings readLsattr() {
        String fwVendor = "IBM";
        String fwVersion = null;
        String fwPlatformVersion = null;
        String manufacturer = fwVendor;
        String model = null;
        String serialNumber = null;
        String uuid = null;
        final String fwVersionMarker = "fwversion";
        final String modelMarker = "modelname";
        final String systemIdMarker = "systemid";
        final String uuidMarker = "os_uuid";
        final String fwPlatformVersionMarker = "Platform Firmware level is";
        for (final String checkLine : ExecutingCommand.runNative("lsattr -El sys0")) {
            if (checkLine.startsWith("fwversion")) {
                fwVersion = checkLine.split("fwversion")[1].trim();
                final int comma = fwVersion.indexOf(44);
                if (comma > 0 && fwVersion.length() > comma) {
                    fwVendor = fwVersion.substring(0, comma);
                    fwVersion = fwVersion.substring(comma + 1);
                }
                fwVersion = ParseUtil.whitespaces.split(fwVersion)[0];
            }
            else if (checkLine.startsWith("modelname")) {
                model = checkLine.split("modelname")[1].trim();
                final int comma = model.indexOf(44);
                if (comma > 0 && model.length() > comma) {
                    manufacturer = model.substring(0, comma);
                    model = model.substring(comma + 1);
                }
                model = ParseUtil.whitespaces.split(model)[0];
            }
            else if (checkLine.startsWith("systemid")) {
                serialNumber = checkLine.split("systemid")[1].trim();
                serialNumber = ParseUtil.whitespaces.split(serialNumber)[0];
            }
            else {
                if (!checkLine.startsWith("os_uuid")) {
                    continue;
                }
                uuid = checkLine.split("os_uuid")[1].trim();
                uuid = ParseUtil.whitespaces.split(uuid)[0];
            }
        }
        for (final String checkLine : ExecutingCommand.runNative("lsmcode -c")) {
            if (checkLine.startsWith("Platform Firmware level is")) {
                fwPlatformVersion = checkLine.split("Platform Firmware level is")[1].trim();
                break;
            }
        }
        return new LsattrStrings(fwVendor, fwPlatformVersion, fwVersion, manufacturer, model, serialNumber, uuid);
    }
    
    private static final class LsattrStrings
    {
        private final String biosVendor;
        private final String biosPlatformVersion;
        private final String biosVersion;
        private final String manufacturer;
        private final String model;
        private final String serialNumber;
        private final String uuid;
        
        private LsattrStrings(final String biosVendor, final String biosPlatformVersion, final String biosVersion, final String manufacturer, final String model, final String serialNumber, final String uuid) {
            this.biosVendor = (Util.isBlank(biosVendor) ? "unknown" : biosVendor);
            this.biosPlatformVersion = (Util.isBlank(biosPlatformVersion) ? "unknown" : biosPlatformVersion);
            this.biosVersion = (Util.isBlank(biosVersion) ? "unknown" : biosVersion);
            this.manufacturer = (Util.isBlank(manufacturer) ? "unknown" : manufacturer);
            this.model = (Util.isBlank(model) ? "unknown" : model);
            this.serialNumber = (Util.isBlank(serialNumber) ? "unknown" : serialNumber);
            this.uuid = (Util.isBlank(uuid) ? "unknown" : uuid);
        }
    }
}
