// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.hardware.GraphicsCard;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractGraphicsCard;

@Immutable
final class MacGraphicsCard extends AbstractGraphicsCard
{
    MacGraphicsCard(final String name, final String deviceId, final String vendor, final String versionInfo, final long vram) {
        super(name, deviceId, vendor, versionInfo, vram);
    }
    
    public static List<GraphicsCard> getGraphicsCards() {
        final List<GraphicsCard> cardList = new ArrayList<GraphicsCard>();
        final List<String> sp = ExecutingCommand.runNative("system_profiler SPDisplaysDataType");
        String name = "unknown";
        String deviceId = "unknown";
        String vendor = "unknown";
        final List<String> versionInfoList = new ArrayList<String>();
        long vram = 0L;
        int cardNum = 0;
        for (final String line : sp) {
            final String[] split = line.trim().split(":", 2);
            if (split.length == 2) {
                final String prefix = split[0].toLowerCase();
                if (prefix.equals("chipset model")) {
                    if (cardNum++ > 0) {
                        cardList.add(new MacGraphicsCard(name, deviceId, vendor, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), vram));
                        versionInfoList.clear();
                    }
                    name = split[1].trim();
                }
                else if (prefix.equals("device id")) {
                    deviceId = split[1].trim();
                }
                else if (prefix.equals("vendor")) {
                    vendor = split[1].trim();
                }
                else if (prefix.contains("version") || prefix.contains("revision")) {
                    versionInfoList.add(line.trim());
                }
                else {
                    if (!prefix.startsWith("vram")) {
                        continue;
                    }
                    vram = ParseUtil.parseDecimalMemorySizeToBinary(split[1].trim());
                }
            }
        }
        cardList.add(new MacGraphicsCard(name, deviceId, vendor, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), vram));
        return cardList;
    }
}
