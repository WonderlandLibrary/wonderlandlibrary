// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import oshi.util.ParseUtil;
import java.util.Iterator;
import oshi.util.Util;
import oshi.util.ExecutingCommand;
import oshi.hardware.platform.unix.UnixBaseboard;
import oshi.hardware.Baseboard;
import oshi.hardware.Firmware;
import oshi.util.Memoizer;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractComputerSystem;

@Immutable
final class SolarisComputerSystem extends AbstractComputerSystem
{
    private final Supplier<SmbiosStrings> smbiosStrings;
    
    SolarisComputerSystem() {
        this.smbiosStrings = Memoizer.memoize(SolarisComputerSystem::readSmbios);
    }
    
    @Override
    public String getManufacturer() {
        return this.smbiosStrings.get().manufacturer;
    }
    
    @Override
    public String getModel() {
        return this.smbiosStrings.get().model;
    }
    
    @Override
    public String getSerialNumber() {
        return this.smbiosStrings.get().serialNumber;
    }
    
    @Override
    public String getHardwareUUID() {
        return this.smbiosStrings.get().uuid;
    }
    
    public Firmware createFirmware() {
        return new SolarisFirmware(this.smbiosStrings.get().biosVendor, this.smbiosStrings.get().biosVersion, this.smbiosStrings.get().biosDate);
    }
    
    public Baseboard createBaseboard() {
        return new UnixBaseboard(this.smbiosStrings.get().boardManufacturer, this.smbiosStrings.get().boardModel, this.smbiosStrings.get().boardSerialNumber, this.smbiosStrings.get().boardVersion);
    }
    
    private static SmbiosStrings readSmbios() {
        String biosVendor = null;
        String biosVersion = null;
        String biosDate = null;
        String manufacturer = null;
        String model = null;
        String serialNumber = null;
        String uuid = null;
        String boardManufacturer = null;
        String boardModel = null;
        String boardVersion = null;
        String boardSerialNumber = null;
        final String vendorMarker = "Vendor:";
        final String biosDateMarker = "Release Date:";
        final String biosVersionMarker = "VersionString:";
        final String manufacturerMarker = "Manufacturer:";
        final String productMarker = "Product:";
        final String serialNumMarker = "Serial Number:";
        final String uuidMarker = "UUID:";
        final String versionMarker = "Version:";
        int smbTypeId = -1;
        for (final String checkLine : ExecutingCommand.runNative("smbios")) {
            if (checkLine.contains("SMB_TYPE_") && (smbTypeId = getSmbType(checkLine)) == Integer.MAX_VALUE) {
                break;
            }
            switch (smbTypeId) {
                case 0: {
                    if (checkLine.contains("Vendor:")) {
                        biosVendor = checkLine.split("Vendor:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("VersionString:")) {
                        biosVersion = checkLine.split("VersionString:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("Release Date:")) {
                        biosDate = checkLine.split("Release Date:")[1].trim();
                        continue;
                    }
                    continue;
                }
                case 1: {
                    if (checkLine.contains("Manufacturer:")) {
                        manufacturer = checkLine.split("Manufacturer:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("Product:")) {
                        model = checkLine.split("Product:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("Serial Number:")) {
                        serialNumber = checkLine.split("Serial Number:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("UUID:")) {
                        uuid = checkLine.split("UUID:")[1].trim();
                        continue;
                    }
                    continue;
                }
                case 2: {
                    if (checkLine.contains("Manufacturer:")) {
                        boardManufacturer = checkLine.split("Manufacturer:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("Product:")) {
                        boardModel = checkLine.split("Product:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("Version:")) {
                        boardVersion = checkLine.split("Version:")[1].trim();
                        continue;
                    }
                    if (checkLine.contains("Serial Number:")) {
                        boardSerialNumber = checkLine.split("Serial Number:")[1].trim();
                        continue;
                    }
                    continue;
                }
            }
        }
        if (Util.isBlank(serialNumber)) {
            serialNumber = readSerialNumber();
        }
        return new SmbiosStrings(biosVendor, biosVersion, biosDate, manufacturer, model, serialNumber, uuid, boardManufacturer, boardModel, boardVersion, boardSerialNumber);
    }
    
    private static int getSmbType(final String checkLine) {
        if (checkLine.contains("SMB_TYPE_BIOS")) {
            return 0;
        }
        if (checkLine.contains("SMB_TYPE_SYSTEM")) {
            return 1;
        }
        if (checkLine.contains("SMB_TYPE_BASEBOARD")) {
            return 2;
        }
        return Integer.MAX_VALUE;
    }
    
    private static String readSerialNumber() {
        String serialNumber = ExecutingCommand.getFirstAnswer("sneep");
        if (serialNumber.isEmpty()) {
            final String marker = "chassis-sn:";
            for (final String checkLine : ExecutingCommand.runNative("prtconf -pv")) {
                if (checkLine.contains(marker)) {
                    serialNumber = ParseUtil.getSingleQuoteStringValue(checkLine);
                    break;
                }
            }
        }
        return serialNumber;
    }
    
    private static final class SmbiosStrings
    {
        private final String biosVendor;
        private final String biosVersion;
        private final String biosDate;
        private final String manufacturer;
        private final String model;
        private final String serialNumber;
        private final String uuid;
        private final String boardManufacturer;
        private final String boardModel;
        private final String boardVersion;
        private final String boardSerialNumber;
        
        private SmbiosStrings(final String biosVendor, final String biosVersion, final String biosDate, final String manufacturer, final String model, final String serialNumber, final String uuid, final String boardManufacturer, final String boardModel, final String boardVersion, final String boardSerialNumber) {
            this.biosVendor = (Util.isBlank(biosVendor) ? "unknown" : biosVendor);
            this.biosVersion = (Util.isBlank(biosVersion) ? "unknown" : biosVersion);
            this.biosDate = (Util.isBlank(biosDate) ? "unknown" : biosDate);
            this.manufacturer = (Util.isBlank(manufacturer) ? "unknown" : manufacturer);
            this.model = (Util.isBlank(model) ? "unknown" : model);
            this.serialNumber = (Util.isBlank(serialNumber) ? "unknown" : serialNumber);
            this.uuid = (Util.isBlank(uuid) ? "unknown" : uuid);
            this.boardManufacturer = (Util.isBlank(boardManufacturer) ? "unknown" : boardManufacturer);
            this.boardModel = (Util.isBlank(boardModel) ? "unknown" : boardModel);
            this.boardVersion = (Util.isBlank(boardVersion) ? "unknown" : boardVersion);
            this.boardSerialNumber = (Util.isBlank(boardSerialNumber) ? "unknown" : boardSerialNumber);
        }
    }
}
