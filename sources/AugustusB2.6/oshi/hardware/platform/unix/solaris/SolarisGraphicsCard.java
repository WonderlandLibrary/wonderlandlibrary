// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.hardware.GraphicsCard;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractGraphicsCard;

@Immutable
final class SolarisGraphicsCard extends AbstractGraphicsCard
{
    private static final String PCI_CLASS_DISPLAY = "0003";
    
    SolarisGraphicsCard(final String name, final String deviceId, final String vendor, final String versionInfo, final long vram) {
        super(name, deviceId, vendor, versionInfo, vram);
    }
    
    public static List<GraphicsCard> getGraphicsCards() {
        final List<GraphicsCard> cardList = new ArrayList<GraphicsCard>();
        final List<String> devices = ExecutingCommand.runNative("prtconf -pv");
        if (devices.isEmpty()) {
            return cardList;
        }
        String name = "";
        String vendorId = "";
        String productId = "";
        String classCode = "";
        final List<String> versionInfoList = new ArrayList<String>();
        for (final String line : devices) {
            if (line.contains("Node 0x")) {
                if ("0003".equals(classCode)) {
                    cardList.add(new SolarisGraphicsCard(name.isEmpty() ? "unknown" : name, productId.isEmpty() ? "unknown" : productId, vendorId.isEmpty() ? "unknown" : vendorId, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), 0L));
                }
                name = "";
                vendorId = "unknown";
                productId = "unknown";
                classCode = "";
                versionInfoList.clear();
            }
            else {
                final String[] split = line.trim().split(":", 2);
                if (split.length != 2) {
                    continue;
                }
                if (split[0].equals("model")) {
                    name = ParseUtil.getSingleQuoteStringValue(line);
                }
                else if (split[0].equals("name")) {
                    if (!name.isEmpty()) {
                        continue;
                    }
                    name = ParseUtil.getSingleQuoteStringValue(line);
                }
                else if (split[0].equals("vendor-id")) {
                    vendorId = "0x" + line.substring(line.length() - 4);
                }
                else if (split[0].equals("device-id")) {
                    productId = "0x" + line.substring(line.length() - 4);
                }
                else if (split[0].equals("revision-id")) {
                    versionInfoList.add(line.trim());
                }
                else {
                    if (!split[0].equals("class-code")) {
                        continue;
                    }
                    classCode = line.substring(line.length() - 8, line.length() - 4);
                }
            }
        }
        if ("0003".equals(classCode)) {
            cardList.add(new SolarisGraphicsCard(name.isEmpty() ? "unknown" : name, productId.isEmpty() ? "unknown" : productId, vendorId.isEmpty() ? "unknown" : vendorId, versionInfoList.isEmpty() ? "unknown" : String.join(", ", versionInfoList), 0L));
        }
        return cardList;
    }
}
