// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import java.util.Iterator;
import oshi.util.Util;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import oshi.hardware.GraphicsCard;
import java.util.List;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractGraphicsCard;

@Immutable
final class AixGraphicsCard extends AbstractGraphicsCard
{
    AixGraphicsCard(final String name, final String deviceId, final String vendor, final String versionInfo, final long vram) {
        super(name, deviceId, vendor, versionInfo, vram);
    }
    
    public static List<GraphicsCard> getGraphicsCards(final Supplier<List<String>> lscfg) {
        final List<GraphicsCard> cardList = new ArrayList<GraphicsCard>();
        boolean display = false;
        String name = null;
        String vendor = null;
        final List<String> versionInfo = new ArrayList<String>();
        for (final String line : lscfg.get()) {
            final String s = line.trim();
            if (s.startsWith("Name:") && s.contains("display")) {
                display = true;
            }
            else if (display && s.toLowerCase().contains("graphics")) {
                name = s;
            }
            else {
                if (!display || name == null) {
                    continue;
                }
                if (s.startsWith("Manufacture ID")) {
                    vendor = ParseUtil.removeLeadingDots(s.substring(14));
                }
                else if (s.contains("Level")) {
                    versionInfo.add(s.replaceAll("\\.\\.+", "="));
                }
                else {
                    if (!s.startsWith("Hardware Location Code")) {
                        continue;
                    }
                    cardList.add(new AixGraphicsCard(name, "unknown", Util.isBlank(vendor) ? "unknown" : vendor, versionInfo.isEmpty() ? "unknown" : String.join(",", versionInfo), 0L));
                    display = false;
                }
            }
        }
        return cardList;
    }
}
