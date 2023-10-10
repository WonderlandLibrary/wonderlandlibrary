// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import java.util.Map;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import oshi.hardware.UsbDevice;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractUsbDevice;

@Immutable
public class OpenBsdUsbDevice extends AbstractUsbDevice
{
    public OpenBsdUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        super(name, vendor, vendorId, productId, serialNumber, uniqueDeviceId, connectedDevices);
    }
    
    public static List<UsbDevice> getUsbDevices(final boolean tree) {
        final List<UsbDevice> devices = getUsbDevices();
        if (tree) {
            return devices;
        }
        final List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
        for (final UsbDevice device : devices) {
            deviceList.add(new OpenBsdUsbDevice(device.getName(), device.getVendor(), device.getVendorId(), device.getProductId(), device.getSerialNumber(), device.getUniqueDeviceId(), Collections.emptyList()));
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
        return deviceList;
    }
    
    private static List<UsbDevice> getUsbDevices() {
        final Map<String, String> nameMap = new HashMap<String, String>();
        final Map<String, String> vendorMap = new HashMap<String, String>();
        final Map<String, String> vendorIdMap = new HashMap<String, String>();
        final Map<String, String> productIdMap = new HashMap<String, String>();
        final Map<String, String> serialMap = new HashMap<String, String>();
        final Map<String, List<String>> hubMap = new HashMap<String, List<String>>();
        final List<String> rootHubs = new ArrayList<String>();
        String key = "";
        String parent = "";
        for (final String line : ExecutingCommand.runNative("usbdevs -v")) {
            if (line.startsWith("Controller ")) {
                parent = line.substring(11);
            }
            else if (line.startsWith("addr ")) {
                if (line.indexOf(58) != 7 || line.indexOf(44) < 18) {
                    continue;
                }
                key = parent + line.substring(0, 7);
                final String[] split = line.substring(8).trim().split(",");
                if (split.length <= 1) {
                    continue;
                }
                final String vendorStr = split[0].trim();
                final int idx1 = vendorStr.indexOf(58);
                final int idx2 = vendorStr.indexOf(32);
                if (idx1 >= 0 && idx2 >= 0) {
                    vendorIdMap.put(key, vendorStr.substring(0, idx1));
                    productIdMap.put(key, vendorStr.substring(idx1 + 1, idx2));
                    vendorMap.put(key, vendorStr.substring(idx2 + 1));
                }
                nameMap.put(key, split[1].trim());
                hubMap.computeIfAbsent(parent, x -> new ArrayList()).add(key);
                if (parent.contains("addr")) {
                    continue;
                }
                parent = key;
                rootHubs.add(parent);
            }
            else {
                if (key.isEmpty()) {
                    continue;
                }
                final int idx3 = line.indexOf("iSerial ");
                if (idx3 >= 0) {
                    serialMap.put(key, line.substring(idx3 + 8).trim());
                }
                key = "";
            }
        }
        final List<UsbDevice> controllerDevices = new ArrayList<UsbDevice>();
        for (final String devusb : rootHubs) {
            controllerDevices.add(getDeviceAndChildren(devusb, "0000", "0000", nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        return controllerDevices;
    }
    
    private static void addDevicesToList(final List<UsbDevice> deviceList, final List<UsbDevice> list) {
        for (final UsbDevice device : list) {
            deviceList.add(device);
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
    }
    
    private static OpenBsdUsbDevice getDeviceAndChildren(final String devPath, final String vid, final String pid, final Map<String, String> nameMap, final Map<String, String> vendorMap, final Map<String, String> vendorIdMap, final Map<String, String> productIdMap, final Map<String, String> serialMap, final Map<String, List<String>> hubMap) {
        final String vendorId = vendorIdMap.getOrDefault(devPath, vid);
        final String productId = productIdMap.getOrDefault(devPath, pid);
        final List<String> childPaths = hubMap.getOrDefault(devPath, new ArrayList<String>());
        final List<UsbDevice> usbDevices = new ArrayList<UsbDevice>();
        for (final String path : childPaths) {
            usbDevices.add(getDeviceAndChildren(path, vendorId, productId, nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        Collections.sort(usbDevices);
        return new OpenBsdUsbDevice(nameMap.getOrDefault(devPath, vendorId + ":" + productId), vendorMap.getOrDefault(devPath, ""), vendorId, productId, serialMap.getOrDefault(devPath, ""), devPath, usbDevices);
    }
}
