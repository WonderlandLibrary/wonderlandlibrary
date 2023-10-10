// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import oshi.util.tuples.Triplet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import oshi.util.ParseUtil;
import oshi.util.tuples.Quintet;
import java.util.Set;
import java.util.Map;
import oshi.driver.windows.DeviceTree;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import oshi.hardware.UsbDevice;
import java.util.List;
import com.sun.jna.platform.win32.Guid;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractUsbDevice;

@Immutable
public class WindowsUsbDevice extends AbstractUsbDevice
{
    private static final Guid.GUID GUID_DEVINTERFACE_USB_HOST_CONTROLLER;
    
    public WindowsUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        super(name, vendor, vendorId, productId, serialNumber, uniqueDeviceId, connectedDevices);
    }
    
    public static List<UsbDevice> getUsbDevices(final boolean tree) {
        final List<UsbDevice> devices = queryUsbDevices();
        if (tree) {
            return devices;
        }
        final List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
        for (final UsbDevice device : devices) {
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
        return deviceList;
    }
    
    private static void addDevicesToList(final List<UsbDevice> deviceList, final List<UsbDevice> list) {
        for (final UsbDevice device : list) {
            deviceList.add(new WindowsUsbDevice(device.getName(), device.getVendor(), device.getVendorId(), device.getProductId(), device.getSerialNumber(), device.getUniqueDeviceId(), Collections.emptyList()));
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
    }
    
    private static List<UsbDevice> queryUsbDevices() {
        final Quintet<Set<Integer>, Map<Integer, Integer>, Map<Integer, String>, Map<Integer, String>, Map<Integer, String>> controllerDevices = DeviceTree.queryDeviceTree(WindowsUsbDevice.GUID_DEVINTERFACE_USB_HOST_CONTROLLER);
        final Map<Integer, Integer> parentMap = controllerDevices.getB();
        final Map<Integer, String> nameMap = controllerDevices.getC();
        final Map<Integer, String> deviceIdMap = controllerDevices.getD();
        final Map<Integer, String> mfgMap = controllerDevices.getE();
        final List<UsbDevice> usbDevices = new ArrayList<UsbDevice>();
        for (final Integer controllerDevice : controllerDevices.getA()) {
            final WindowsUsbDevice deviceAndChildren = queryDeviceAndChildren(controllerDevice, parentMap, nameMap, deviceIdMap, mfgMap, "0000", "0000", "");
            if (deviceAndChildren != null) {
                usbDevices.add(deviceAndChildren);
            }
        }
        return usbDevices;
    }
    
    private static WindowsUsbDevice queryDeviceAndChildren(final Integer device, final Map<Integer, Integer> parentMap, final Map<Integer, String> nameMap, final Map<Integer, String> deviceIdMap, final Map<Integer, String> mfgMap, final String vid, final String pid, final String parentSerial) {
        String vendorId = vid;
        String productId = pid;
        String serial = parentSerial;
        final Triplet<String, String, String> idsAndSerial = ParseUtil.parseDeviceIdToVendorProductSerial(deviceIdMap.get(device));
        if (idsAndSerial != null) {
            vendorId = idsAndSerial.getA();
            productId = idsAndSerial.getB();
            serial = idsAndSerial.getC();
            if (serial.isEmpty() && vendorId.equals(vid) && productId.equals(pid)) {
                serial = parentSerial;
            }
        }
        final Set<Integer> childDeviceSet = parentMap.entrySet().stream().filter(e -> e.getValue().equals(device)).map((Function<? super Object, ?>)Map.Entry::getKey).collect((Collector<? super Object, ?, Set<Integer>>)Collectors.toSet());
        final List<UsbDevice> childDevices = new ArrayList<UsbDevice>();
        for (final Integer child : childDeviceSet) {
            final WindowsUsbDevice deviceAndChildren = queryDeviceAndChildren(child, parentMap, nameMap, deviceIdMap, mfgMap, vendorId, productId, serial);
            if (deviceAndChildren != null) {
                childDevices.add(deviceAndChildren);
            }
        }
        Collections.sort(childDevices);
        if (nameMap.containsKey(device)) {
            String name = nameMap.get(device);
            if (name.isEmpty()) {
                name = vendorId + ":" + productId;
            }
            final String deviceId = deviceIdMap.get(device);
            final String mfg = mfgMap.get(device);
            return new WindowsUsbDevice(name, mfg, vendorId, productId, serial, deviceId, childDevices);
        }
        return null;
    }
    
    static {
        GUID_DEVINTERFACE_USB_HOST_CONTROLLER = new Guid.GUID("{3ABF6F2D-71C4-462A-8A92-1E6861E6AF27}");
    }
}
