// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix;

import oshi.util.tuples.Pair;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.tuples.Triplet;
import oshi.util.ExecutingCommand;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Lscfg
{
    private Lscfg() {
    }
    
    public static List<String> queryAllDevices() {
        return ExecutingCommand.runNative("lscfg -vp");
    }
    
    public static Triplet<String, String, String> queryBackplaneModelSerialVersion(final List<String> lscfg) {
        final String planeMarker = "WAY BACKPLANE";
        final String modelMarker = "Part Number";
        final String serialMarker = "Serial Number";
        final String versionMarker = "Version";
        final String locationMarker = "Physical Location";
        String model = null;
        String serialNumber = null;
        String version = null;
        boolean planeFlag = false;
        for (final String checkLine : lscfg) {
            if (!planeFlag && checkLine.contains("WAY BACKPLANE")) {
                planeFlag = true;
            }
            else {
                if (!planeFlag) {
                    continue;
                }
                if (checkLine.contains("Part Number")) {
                    model = ParseUtil.removeLeadingDots(checkLine.split("Part Number")[1].trim());
                }
                else if (checkLine.contains("Serial Number")) {
                    serialNumber = ParseUtil.removeLeadingDots(checkLine.split("Serial Number")[1].trim());
                }
                else if (checkLine.contains("Version")) {
                    version = ParseUtil.removeLeadingDots(checkLine.split("Version")[1].trim());
                }
                else {
                    if (checkLine.contains("Physical Location")) {
                        break;
                    }
                    continue;
                }
            }
        }
        return new Triplet<String, String, String>(model, serialNumber, version);
    }
    
    public static Pair<String, String> queryModelSerial(final String device) {
        final String modelMarker = "Machine Type and Model";
        final String serialMarker = "Serial Number";
        String model = null;
        String serial = null;
        for (final String s : ExecutingCommand.runNative("lscfg -vl " + device)) {
            if (s.contains(modelMarker)) {
                model = ParseUtil.removeLeadingDots(s.split(modelMarker)[1].trim());
            }
            else {
                if (!s.contains(serialMarker)) {
                    continue;
                }
                serial = ParseUtil.removeLeadingDots(s.split(serialMarker)[1].trim());
            }
        }
        return new Pair<String, String>(model, serial);
    }
}
