// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Collections;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.hardware.GraphicsCard;
import java.util.List;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractGraphicsCard;

@Immutable
final class OpenBsdGraphicsCard extends AbstractGraphicsCard
{
    private static final String PCI_CLASS_DISPLAY = "Class: 03 Display";
    private static final Pattern PCI_DUMP_HEADER;
    
    OpenBsdGraphicsCard(final String name, final String deviceId, final String vendor, final String versionInfo, final long vram) {
        super(name, deviceId, vendor, versionInfo, vram);
    }
    
    public static List<GraphicsCard> getGraphicsCards() {
        final List<GraphicsCard> cardList = new ArrayList<GraphicsCard>();
        final List<String> devices = ExecutingCommand.runNative("pcidump -v");
        if (devices.isEmpty()) {
            return Collections.emptyList();
        }
        String name = "";
        String vendorId = "";
        String productId = "";
        boolean classCodeFound = false;
        String versionInfo = "";
        for (final String line : devices) {
            final Matcher m = OpenBsdGraphicsCard.PCI_DUMP_HEADER.matcher(line);
            if (m.matches()) {
                if (classCodeFound) {
                    cardList.add(new OpenBsdGraphicsCard(name.isEmpty() ? "unknown" : name, productId.isEmpty() ? "0x0000" : productId, vendorId.isEmpty() ? "0x0000" : vendorId, versionInfo.isEmpty() ? "unknown" : versionInfo, 0L));
                }
                name = m.group(1);
                vendorId = "";
                productId = "";
                classCodeFound = false;
                versionInfo = "";
            }
            else if (!classCodeFound) {
                int idx = line.indexOf("Vendor ID: ");
                if (idx >= 0 && line.length() >= idx + 15) {
                    vendorId = "0x" + line.substring(idx + 11, idx + 15);
                }
                idx = line.indexOf("Product ID: ");
                if (idx >= 0 && line.length() >= idx + 16) {
                    productId = "0x" + line.substring(idx + 12, idx + 16);
                }
                if (!line.contains("Class: 03 Display")) {
                    continue;
                }
                classCodeFound = true;
            }
            else {
                if (!versionInfo.isEmpty()) {
                    continue;
                }
                final int idx = line.indexOf("Revision: ");
                if (idx < 0) {
                    continue;
                }
                versionInfo = line.substring(idx);
            }
        }
        if (classCodeFound) {
            cardList.add(new OpenBsdGraphicsCard(name.isEmpty() ? "unknown" : name, productId.isEmpty() ? "0x0000" : productId, vendorId.isEmpty() ? "0x0000" : vendorId, versionInfo.isEmpty() ? "unknown" : versionInfo, 0L));
        }
        return cardList;
    }
    
    static {
        PCI_DUMP_HEADER = Pattern.compile(" \\d+:\\d+:\\d+: (.+)");
    }
}
