// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux;

import oshi.util.ParseUtil;
import oshi.util.tuples.Pair;
import java.util.Iterator;
import oshi.util.ExecutingCommand;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Dmidecode
{
    private Dmidecode() {
    }
    
    public static String querySerialNumber() {
        final String marker = "Serial Number:";
        for (final String checkLine : ExecutingCommand.runNative("dmidecode -t system")) {
            if (checkLine.contains(marker)) {
                return checkLine.split(marker)[1].trim();
            }
        }
        return null;
    }
    
    public static String queryUUID() {
        final String marker = "UUID:";
        for (final String checkLine : ExecutingCommand.runNative("dmidecode -t system")) {
            if (checkLine.contains(marker)) {
                return checkLine.split(marker)[1].trim();
            }
        }
        return null;
    }
    
    public static Pair<String, String> queryBiosNameRev() {
        String biosName = null;
        String revision = null;
        final String biosMarker = "SMBIOS";
        final String revMarker = "Bios Revision:";
        for (final String checkLine : ExecutingCommand.runNative("dmidecode -t bios")) {
            if (checkLine.contains("SMBIOS")) {
                final String[] biosArr = ParseUtil.whitespaces.split(checkLine);
                if (biosArr.length >= 2) {
                    biosName = biosArr[0] + " " + biosArr[1];
                }
            }
            if (checkLine.contains("Bios Revision:")) {
                revision = checkLine.split("Bios Revision:")[1].trim();
                break;
            }
        }
        return new Pair<String, String>(biosName, revision);
    }
}
