// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import java.util.Iterator;
import java.util.Arrays;
import java.util.Collections;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import java.util.function.Supplier;
import oshi.hardware.UsbDevice;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractUsbDevice;

@Immutable
public class AixUsbDevice extends AbstractUsbDevice
{
    public AixUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        super(name, vendor, vendorId, productId, serialNumber, uniqueDeviceId, connectedDevices);
    }
    
    public static List<UsbDevice> getUsbDevices(final boolean tree, final Supplier<List<String>> lscfg) {
        final List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
        for (final String line : lscfg.get()) {
            final String s = line.trim();
            if (s.startsWith("usb")) {
                final String[] split = ParseUtil.whitespaces.split(s, 3);
                if (split.length != 3) {
                    continue;
                }
                deviceList.add(new AixUsbDevice(split[2], "unknown", "unknown", "unknown", "unknown", split[0], Collections.emptyList()));
            }
        }
        if (tree) {
            return Arrays.asList(new AixUsbDevice("USB Controller", "", "0000", "0000", "", "", deviceList));
        }
        return deviceList;
    }
}
