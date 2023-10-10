// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

import java.util.Map;
import oshi.util.ParseUtil;
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
public class FreeBsdUsbDevice extends AbstractUsbDevice
{
    public FreeBsdUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        super(name, vendor, vendorId, productId, serialNumber, uniqueDeviceId, connectedDevices);
    }
    
    public static List<UsbDevice> getUsbDevices(final boolean tree) {
        final List<UsbDevice> devices = getUsbDevices();
        if (tree) {
            return devices;
        }
        final List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
        for (final UsbDevice device : devices) {
            deviceList.add(new FreeBsdUsbDevice(device.getName(), device.getVendor(), device.getVendorId(), device.getProductId(), device.getSerialNumber(), device.getUniqueDeviceId(), Collections.emptyList()));
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
        final Map<String, String> parentMap = new HashMap<String, String>();
        final Map<String, List<String>> hubMap = new HashMap<String, List<String>>();
        final List<String> devices = ExecutingCommand.runNative("lshal");
        if (devices.isEmpty()) {
            return Collections.emptyList();
        }
        String key = "";
        final List<String> usBuses = new ArrayList<String>();
        for (String line : devices) {
            if (line.startsWith("udi =")) {
                key = ParseUtil.getSingleQuoteStringValue(line);
            }
            else {
                if (key.isEmpty()) {
                    continue;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("freebsd.driver =") && "usbus".equals(ParseUtil.getSingleQuoteStringValue(line))) {
                    usBuses.add(key);
                }
                else if (line.contains(".parent =")) {
                    final String parent = ParseUtil.getSingleQuoteStringValue(line);
                    if (key.replace(parent, "").startsWith("_if")) {
                        continue;
                    }
                    parentMap.put(key, parent);
                    hubMap.computeIfAbsent(parent, x -> new ArrayList()).add(key);
                }
                else if (line.contains(".product =")) {
                    nameMap.put(key, ParseUtil.getSingleQuoteStringValue(line));
                }
                else if (line.contains(".vendor =")) {
                    vendorMap.put(key, ParseUtil.getSingleQuoteStringValue(line));
                }
                else if (line.contains(".serial =")) {
                    final String serial = ParseUtil.getSingleQuoteStringValue(line);
                    serialMap.put(key, serial.startsWith("0x") ? ParseUtil.hexStringToString(serial.replace("0x", "")) : serial);
                }
                else if (line.contains(".vendor_id =")) {
                    vendorIdMap.put(key, String.format("%04x", ParseUtil.getFirstIntValue(line)));
                }
                else {
                    if (!line.contains(".product_id =")) {
                        continue;
                    }
                    productIdMap.put(key, String.format("%04x", ParseUtil.getFirstIntValue(line)));
                }
            }
        }
        final List<UsbDevice> controllerDevices = new ArrayList<UsbDevice>();
        for (final String usbus : usBuses) {
            final String parent2 = parentMap.get(usbus);
            hubMap.put(parent2, hubMap.get(usbus));
            controllerDevices.add(getDeviceAndChildren(parent2, "0000", "0000", nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        return controllerDevices;
    }
    
    private static void addDevicesToList(final List<UsbDevice> deviceList, final List<UsbDevice> list) {
        for (final UsbDevice device : list) {
            deviceList.add(device);
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
    }
    
    private static FreeBsdUsbDevice getDeviceAndChildren(final String devPath, final String vid, final String pid, final Map<String, String> nameMap, final Map<String, String> vendorMap, final Map<String, String> vendorIdMap, final Map<String, String> productIdMap, final Map<String, String> serialMap, final Map<String, List<String>> hubMap) {
        final String vendorId = vendorIdMap.getOrDefault(devPath, vid);
        final String productId = productIdMap.getOrDefault(devPath, pid);
        final List<String> childPaths = hubMap.getOrDefault(devPath, new ArrayList<String>());
        final List<UsbDevice> usbDevices = new ArrayList<UsbDevice>();
        for (final String path : childPaths) {
            usbDevices.add(getDeviceAndChildren(path, vendorId, productId, nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        Collections.sort(usbDevices);
        return new FreeBsdUsbDevice(nameMap.getOrDefault(devPath, vendorId + ":" + productId), vendorMap.getOrDefault(devPath, ""), vendorId, productId, serialMap.getOrDefault(devPath, ""), devPath, usbDevices);
    }
}
