// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux.proc;

import oshi.util.tuples.Quartet;
import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class CpuInfo
{
    private CpuInfo() {
    }
    
    public static String queryCpuManufacturer() {
        final List<String> cpuInfo = FileUtil.readFile(ProcPath.CPUINFO);
        for (final String line : cpuInfo) {
            if (line.startsWith("CPU implementer")) {
                final int part = ParseUtil.parseLastInt(line, 0);
                switch (part) {
                    case 65: {
                        return "ARM";
                    }
                    case 66: {
                        return "Broadcom";
                    }
                    case 67: {
                        return "Cavium";
                    }
                    case 68: {
                        return "DEC";
                    }
                    case 78: {
                        return "Nvidia";
                    }
                    case 80: {
                        return "APM";
                    }
                    case 81: {
                        return "Qualcomm";
                    }
                    case 83: {
                        return "Samsung";
                    }
                    case 86: {
                        return "Marvell";
                    }
                    case 102: {
                        return "Faraday";
                    }
                    case 105: {
                        return "Intel";
                    }
                    default: {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    
    public static Quartet<String, String, String, String> queryBoardInfo() {
        String pcManufacturer = null;
        String pcModel = null;
        String pcVersion = null;
        String pcSerialNumber = null;
        final List<String> cpuInfo = FileUtil.readFile(ProcPath.CPUINFO);
        for (final String line : cpuInfo) {
            final String[] splitLine = ParseUtil.whitespacesColonWhitespace.split(line);
            if (splitLine.length < 2) {
                continue;
            }
            final String s = splitLine[0];
            switch (s) {
                case "Hardware": {
                    pcModel = splitLine[1];
                    continue;
                }
                case "Revision": {
                    pcVersion = splitLine[1];
                    if (pcVersion.length() > 1) {
                        pcManufacturer = queryBoardManufacturer(pcVersion.charAt(1));
                        continue;
                    }
                    continue;
                }
                case "Serial": {
                    pcSerialNumber = splitLine[1];
                    continue;
                }
            }
        }
        return new Quartet<String, String, String, String>(pcManufacturer, pcModel, pcVersion, pcSerialNumber);
    }
    
    private static String queryBoardManufacturer(final char digit) {
        switch (digit) {
            case '0': {
                return "Sony UK";
            }
            case '1': {
                return "Egoman";
            }
            case '2': {
                return "Embest";
            }
            case '3': {
                return "Sony Japan";
            }
            case '4': {
                return "Embest";
            }
            case '5': {
                return "Stadium";
            }
            default: {
                return "unknown";
            }
        }
    }
}
