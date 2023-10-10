// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import oshi.util.tuples.Pair;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.hardware.GraphicsCard;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractGraphicsCard;

@Immutable
final class LinuxGraphicsCard extends AbstractGraphicsCard
{
    LinuxGraphicsCard(final String name, final String deviceId, final String vendor, final String versionInfo, final long vram) {
        super(name, deviceId, vendor, versionInfo, vram);
    }
    
    public static List<GraphicsCard> getGraphicsCards() {
        List<GraphicsCard> cardList = getGraphicsCardsFromLspci();
        if (cardList.isEmpty()) {
            cardList = getGraphicsCardsFromLshw();
        }
        return cardList;
    }
    
    private static List<GraphicsCard> getGraphicsCardsFromLspci() {
        final List<GraphicsCard> cardList = new ArrayList<GraphicsCard>();
        final List<String> lspci = ExecutingCommand.runNative("lspci -vnnm");
        String name = "unknown";
        String deviceId = "unknown";
        String vendor = "unknown";
        final List<String> versionInfoList = new ArrayList<String>();
        boolean found = false;
        String lookupDevice = null;
        for (final String line : lspci) {
            final String[] split = line.trim().split(":", 2);
            final String prefix = split[0];
            if (prefix.equals("Class") && line.contains("VGA")) {
                found = true;
            }
            else if (prefix.equals("Device") && !found && split.length > 1) {
                lookupDevice = split[1].trim();
            }
            if (found) {
                if (split.length < 2) {
                    cardList.add(new LinuxGraphicsCard(name, deviceId, vendor, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), queryLspciMemorySize(lookupDevice)));
                    versionInfoList.clear();
                    found = false;
                }
                else if (prefix.equals("Device")) {
                    final Pair<String, String> pair = ParseUtil.parseLspciMachineReadable(split[1].trim());
                    if (pair == null) {
                        continue;
                    }
                    name = pair.getA();
                    deviceId = "0x" + pair.getB();
                }
                else if (prefix.equals("Vendor")) {
                    final Pair<String, String> pair = ParseUtil.parseLspciMachineReadable(split[1].trim());
                    if (pair != null) {
                        vendor = pair.getA() + " (0x" + pair.getB() + ")";
                    }
                    else {
                        vendor = split[1].trim();
                    }
                }
                else {
                    if (!prefix.equals("Rev:")) {
                        continue;
                    }
                    versionInfoList.add(line.trim());
                }
            }
        }
        if (found) {
            cardList.add(new LinuxGraphicsCard(name, deviceId, vendor, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), queryLspciMemorySize(lookupDevice)));
        }
        return cardList;
    }
    
    private static long queryLspciMemorySize(final String lookupDevice) {
        long vram = 0L;
        final List<String> lspciMem = ExecutingCommand.runNative("lspci -v -s " + lookupDevice);
        for (final String mem : lspciMem) {
            if (mem.contains(" prefetchable")) {
                vram += ParseUtil.parseLspciMemorySize(mem);
            }
        }
        return vram;
    }
    
    private static List<GraphicsCard> getGraphicsCardsFromLshw() {
        final List<GraphicsCard> cardList = new ArrayList<GraphicsCard>();
        final List<String> lshw = ExecutingCommand.runNative("lshw -C display");
        String name = "unknown";
        final String deviceId = "unknown";
        String vendor = "unknown";
        final List<String> versionInfoList = new ArrayList<String>();
        long vram = 0L;
        int cardNum = 0;
        for (final String line : lshw) {
            final String[] split = line.trim().split(":");
            if (split[0].startsWith("*-display")) {
                if (cardNum++ <= 0) {
                    continue;
                }
                cardList.add(new LinuxGraphicsCard(name, deviceId, vendor, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), vram));
                versionInfoList.clear();
            }
            else {
                if (split.length != 2) {
                    continue;
                }
                final String prefix = split[0];
                if (prefix.equals("product")) {
                    name = split[1].trim();
                }
                else if (prefix.equals("vendor")) {
                    vendor = split[1].trim();
                }
                else if (prefix.equals("version")) {
                    versionInfoList.add(line.trim());
                }
                else {
                    if (!prefix.startsWith("resources")) {
                        continue;
                    }
                    vram = ParseUtil.parseLshwResourceString(split[1].trim());
                }
            }
        }
        cardList.add(new LinuxGraphicsCard(name, deviceId, vendor, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), vram));
        return cardList;
    }
}
