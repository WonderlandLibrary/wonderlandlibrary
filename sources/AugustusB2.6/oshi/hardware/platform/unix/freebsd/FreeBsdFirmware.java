// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

import java.util.Iterator;
import oshi.util.Util;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.util.Memoizer;
import oshi.util.tuples.Triplet;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractFirmware;

@Immutable
final class FreeBsdFirmware extends AbstractFirmware
{
    private final Supplier<Triplet<String, String, String>> manufVersRelease;
    
    FreeBsdFirmware() {
        this.manufVersRelease = Memoizer.memoize(FreeBsdFirmware::readDmiDecode);
    }
    
    @Override
    public String getManufacturer() {
        return this.manufVersRelease.get().getA();
    }
    
    @Override
    public String getVersion() {
        return this.manufVersRelease.get().getB();
    }
    
    @Override
    public String getReleaseDate() {
        return this.manufVersRelease.get().getC();
    }
    
    private static Triplet<String, String, String> readDmiDecode() {
        String manufacturer = null;
        String version = null;
        String releaseDate = "";
        final String manufacturerMarker = "Vendor:";
        final String versionMarker = "Version:";
        final String releaseDateMarker = "Release Date:";
        for (final String checkLine : ExecutingCommand.runNative("dmidecode -t bios")) {
            if (checkLine.contains("Vendor:")) {
                manufacturer = checkLine.split("Vendor:")[1].trim();
            }
            else if (checkLine.contains("Version:")) {
                version = checkLine.split("Version:")[1].trim();
            }
            else {
                if (!checkLine.contains("Release Date:")) {
                    continue;
                }
                releaseDate = checkLine.split("Release Date:")[1].trim();
            }
        }
        releaseDate = ParseUtil.parseMmDdYyyyToYyyyMmDD(releaseDate);
        return new Triplet<String, String, String>(Util.isBlank(manufacturer) ? "unknown" : manufacturer, Util.isBlank(version) ? "unknown" : version, Util.isBlank(releaseDate) ? "unknown" : releaseDate);
    }
}
