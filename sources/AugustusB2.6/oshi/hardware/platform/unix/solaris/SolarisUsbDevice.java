// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

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
public class SolarisUsbDevice extends AbstractUsbDevice
{
    private static final String PCI_TYPE_USB = "000c";
    
    public SolarisUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        super(name, vendor, vendorId, productId, serialNumber, uniqueDeviceId, connectedDevices);
    }
    
    public static List<UsbDevice> getUsbDevices(final boolean tree) {
        final List<UsbDevice> devices = getUsbDevices();
        if (tree) {
            return devices;
        }
        final List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
        for (final UsbDevice device : devices) {
            deviceList.add(new SolarisUsbDevice(device.getName(), device.getVendor(), device.getVendorId(), device.getProductId(), device.getSerialNumber(), device.getUniqueDeviceId(), Collections.emptyList()));
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
        return deviceList;
    }
    
    private static List<UsbDevice> getUsbDevices() {
        final Map<String, String> nameMap = new HashMap<String, String>();
        final Map<String, String> vendorIdMap = new HashMap<String, String>();
        final Map<String, String> productIdMap = new HashMap<String, String>();
        final Map<String, List<String>> hubMap = new HashMap<String, List<String>>();
        final Map<String, String> deviceTypeMap = new HashMap<String, String>();
        final List<String> devices = ExecutingCommand.runNative("prtconf -pv");
        if (devices.isEmpty()) {
            return Collections.emptyList();
        }
        final Map<Integer, String> lastParent = new HashMap<Integer, String>();
        String key = "";
        int indent = 0;
        final List<String> usbControllers = new ArrayList<String>();
        for (String line : devices) {
            if (line.contains("Node 0x")) {
                key = line.replaceFirst("^\\s*", "");
                final int depth = line.length() - key.length();
                if (indent == 0) {
                    indent = depth;
                }
                lastParent.put(depth, key);
                if (depth > indent) {
                    hubMap.computeIfAbsent((String)lastParent.get(depth - indent), x -> new ArrayList()).add(key);
                }
                else {
                    usbControllers.add(key);
                }
            }
            else {
                if (key.isEmpty()) {
                    continue;
                }
                line = line.trim();
                if (line.startsWith("model:")) {
                    nameMap.put(key, ParseUtil.getSingleQuoteStringValue(line));
                }
                else if (line.startsWith("name:")) {
                    nameMap.putIfAbsent(key, ParseUtil.getSingleQuoteStringValue(line));
                }
                else if (line.startsWith("vendor-id:")) {
                    vendorIdMap.put(key, line.substring(line.length() - 4));
                }
                else if (line.startsWith("device-id:")) {
                    productIdMap.put(key, line.substring(line.length() - 4));
                }
                else if (line.startsWith("class-code:")) {
                    deviceTypeMap.putIfAbsent(key, line.substring(line.length() - 8, line.length() - 4));
                }
                else {
                    if (!line.startsWith("device_type:")) {
                        continue;
                    }
                    deviceTypeMap.putIfAbsent(key, ParseUtil.getSingleQuoteStringValue(line));
                }
            }
        }
        final List<UsbDevice> controllerDevices = new ArrayList<UsbDevice>();
        for (final String controller : usbControllers) {
            if ("000c".equals(deviceTypeMap.getOrDefault(controller, "")) || "usb".equals(deviceTypeMap.getOrDefault(controller, ""))) {
                controllerDevices.add(getDeviceAndChildren(controller, "0000", "0000", nameMap, vendorIdMap, productIdMap, hubMap));
            }
        }
        return controllerDevices;
    }
    
    private static void addDevicesToList(final List<UsbDevice> deviceList, final List<UsbDevice> list) {
        for (final UsbDevice device : list) {
            deviceList.add(device);
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
    }
    
    private static SolarisUsbDevice getDeviceAndChildren(final String devPath, final String vid, final String pid, final Map<String, String> nameMap, final Map<String, String> vendorIdMap, final Map<String, String> productIdMap, final Map<String, List<String>> hubMap) {
        final String vendorId = vendorIdMap.getOrDefault(devPath, vid);
        final String productId = productIdMap.getOrDefault(devPath, pid);
        final List<String> childPaths = hubMap.getOrDefault(devPath, new ArrayList<String>());
        final List<UsbDevice> usbDevices = new ArrayList<UsbDevice>();
        for (final String path : childPaths) {
            usbDevices.add(getDeviceAndChildren(path, vendorId, productId, nameMap, vendorIdMap, productIdMap, hubMap));
        }
        Collections.sort(usbDevices);
        return new SolarisUsbDevice(nameMap.getOrDefault(devPath, vendorId + ":" + productId), "", vendorId, productId, "", devPath, usbDevices);
    }
}
