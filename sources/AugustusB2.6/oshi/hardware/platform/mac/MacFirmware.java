// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import com.sun.jna.platform.mac.IOKit;
import oshi.util.Util;
import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import com.sun.jna.platform.mac.IOKitUtil;
import oshi.util.Memoizer;
import oshi.util.tuples.Quintet;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractFirmware;

@Immutable
final class MacFirmware extends AbstractFirmware
{
    private final Supplier<Quintet<String, String, String, String, String>> manufNameDescVersRelease;
    
    MacFirmware() {
        this.manufNameDescVersRelease = Memoizer.memoize(MacFirmware::queryEfi);
    }
    
    @Override
    public String getManufacturer() {
        return this.manufNameDescVersRelease.get().getA();
    }
    
    @Override
    public String getName() {
        return this.manufNameDescVersRelease.get().getB();
    }
    
    @Override
    public String getDescription() {
        return this.manufNameDescVersRelease.get().getC();
    }
    
    @Override
    public String getVersion() {
        return this.manufNameDescVersRelease.get().getD();
    }
    
    @Override
    public String getReleaseDate() {
        return this.manufNameDescVersRelease.get().getE();
    }
    
    private static Quintet<String, String, String, String, String> queryEfi() {
        String manufacturer = null;
        String name = null;
        String description = null;
        String version = null;
        String releaseDate = null;
        final IOKit.IORegistryEntry platformExpert = IOKitUtil.getMatchingService("IOPlatformExpertDevice");
        if (platformExpert != null) {
            final IOKit.IOIterator iter = platformExpert.getChildIterator("IODeviceTree");
            if (iter != null) {
                for (IOKit.IORegistryEntry entry = iter.next(); entry != null; entry = iter.next()) {
                    final String name2 = entry.getName();
                    switch (name2) {
                        case "rom": {
                            byte[] data = entry.getByteArrayProperty("vendor");
                            if (data != null) {
                                manufacturer = Native.toString(data, StandardCharsets.UTF_8);
                            }
                            data = entry.getByteArrayProperty("version");
                            if (data != null) {
                                version = Native.toString(data, StandardCharsets.UTF_8);
                            }
                            data = entry.getByteArrayProperty("release-date");
                            if (data != null) {
                                releaseDate = Native.toString(data, StandardCharsets.UTF_8);
                                break;
                            }
                            break;
                        }
                        case "chosen": {
                            final byte[] data = entry.getByteArrayProperty("booter-name");
                            if (data != null) {
                                name = Native.toString(data, StandardCharsets.UTF_8);
                                break;
                            }
                            break;
                        }
                        case "efi": {
                            final byte[] data = entry.getByteArrayProperty("firmware-abi");
                            if (data != null) {
                                description = Native.toString(data, StandardCharsets.UTF_8);
                                break;
                            }
                            break;
                        }
                        default: {
                            if (Util.isBlank(name)) {
                                name = entry.getStringProperty("IONameMatch");
                                break;
                            }
                            break;
                        }
                    }
                    entry.release();
                }
                iter.release();
            }
            if (Util.isBlank(manufacturer)) {
                final byte[] data = platformExpert.getByteArrayProperty("manufacturer");
                if (data != null) {
                    manufacturer = Native.toString(data, StandardCharsets.UTF_8);
                }
            }
            if (Util.isBlank(version)) {
                final byte[] data = platformExpert.getByteArrayProperty("target-type");
                if (data != null) {
                    version = Native.toString(data, StandardCharsets.UTF_8);
                }
            }
            if (Util.isBlank(name)) {
                final byte[] data = platformExpert.getByteArrayProperty("device_type");
                if (data != null) {
                    name = Native.toString(data, StandardCharsets.UTF_8);
                }
            }
            platformExpert.release();
        }
        return new Quintet<String, String, String, String, String>(Util.isBlank(manufacturer) ? "unknown" : manufacturer, Util.isBlank(name) ? "unknown" : name, Util.isBlank(description) ? "unknown" : description, Util.isBlank(version) ? "unknown" : version, Util.isBlank(releaseDate) ? "unknown" : releaseDate);
    }
}
