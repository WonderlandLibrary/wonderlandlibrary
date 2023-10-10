// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import java.util.Map;
import com.sun.jna.platform.linux.Udev;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import oshi.hardware.UsbDevice;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractUsbDevice;

@Immutable
public class LinuxUsbDevice extends AbstractUsbDevice
{
    private static final String SUBSYSTEM_USB = "usb";
    private static final String DEVTYPE_USB_DEVICE = "usb_device";
    private static final String ATTR_PRODUCT = "product";
    private static final String ATTR_MANUFACTURER = "manufacturer";
    private static final String ATTR_VENDOR_ID = "idVendor";
    private static final String ATTR_PRODUCT_ID = "idProduct";
    private static final String ATTR_SERIAL = "serial";
    
    public LinuxUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        super(name, vendor, vendorId, productId, serialNumber, uniqueDeviceId, connectedDevices);
    }
    
    public static List<UsbDevice> getUsbDevices(final boolean tree) {
        final List<UsbDevice> devices = getUsbDevices();
        if (tree) {
            return devices;
        }
        final List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
        for (final UsbDevice device : devices) {
            deviceList.add(new LinuxUsbDevice(device.getName(), device.getVendor(), device.getVendorId(), device.getProductId(), device.getSerialNumber(), device.getUniqueDeviceId(), Collections.emptyList()));
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
        return deviceList;
    }
    
    private static List<UsbDevice> getUsbDevices() {
        final List<String> usbControllers = new ArrayList<String>();
        final Map<String, String> nameMap = new HashMap<String, String>();
        final Map<String, String> vendorMap = new HashMap<String, String>();
        final Map<String, String> vendorIdMap = new HashMap<String, String>();
        final Map<String, String> productIdMap = new HashMap<String, String>();
        final Map<String, String> serialMap = new HashMap<String, String>();
        final Map<String, List<String>> hubMap = new HashMap<String, List<String>>();
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        try {
            final Udev.UdevEnumerate enumerate = udev.enumerateNew();
            try {
                enumerate.addMatchSubsystem("usb");
                enumerate.scanDevices();
                for (Udev.UdevListEntry entry = enumerate.getListEntry(); entry != null; entry = entry.getNext()) {
                    final String syspath = entry.getName();
                    final Udev.UdevDevice device = udev.deviceNewFromSyspath(syspath);
                    if (device != null) {
                        try {
                            if ("usb_device".equals(device.getDevtype())) {
                                String value = device.getSysattrValue("product");
                                if (value != null) {
                                    nameMap.put(syspath, value);
                                }
                                value = device.getSysattrValue("manufacturer");
                                if (value != null) {
                                    vendorMap.put(syspath, value);
                                }
                                value = device.getSysattrValue("idVendor");
                                if (value != null) {
                                    vendorIdMap.put(syspath, value);
                                }
                                value = device.getSysattrValue("idProduct");
                                if (value != null) {
                                    productIdMap.put(syspath, value);
                                }
                                value = device.getSysattrValue("serial");
                                if (value != null) {
                                    serialMap.put(syspath, value);
                                }
                                final Udev.UdevDevice parent = device.getParentWithSubsystemDevtype("usb", "usb_device");
                                if (parent == null) {
                                    usbControllers.add(syspath);
                                }
                                else {
                                    final String parentPath = parent.getSyspath();
                                    hubMap.computeIfAbsent(parentPath, x -> new ArrayList()).add(syspath);
                                }
                            }
                        }
                        finally {
                            device.unref();
                        }
                    }
                }
            }
            finally {
                enumerate.unref();
            }
        }
        finally {
            udev.unref();
        }
        final List<UsbDevice> controllerDevices = new ArrayList<UsbDevice>();
        for (final String controller : usbControllers) {
            controllerDevices.add(getDeviceAndChildren(controller, "0000", "0000", nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        return controllerDevices;
    }
    
    private static void addDevicesToList(final List<UsbDevice> deviceList, final List<UsbDevice> list) {
        for (final UsbDevice device : list) {
            deviceList.add(device);
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
    }
    
    private static LinuxUsbDevice getDeviceAndChildren(final String devPath, final String vid, final String pid, final Map<String, String> nameMap, final Map<String, String> vendorMap, final Map<String, String> vendorIdMap, final Map<String, String> productIdMap, final Map<String, String> serialMap, final Map<String, List<String>> hubMap) {
        final String vendorId = vendorIdMap.getOrDefault(devPath, vid);
        final String productId = productIdMap.getOrDefault(devPath, pid);
        final List<String> childPaths = hubMap.getOrDefault(devPath, new ArrayList<String>());
        final List<UsbDevice> usbDevices = new ArrayList<UsbDevice>();
        for (final String path : childPaths) {
            usbDevices.add(getDeviceAndChildren(path, vendorId, productId, nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        Collections.sort(usbDevices);
        return new LinuxUsbDevice(nameMap.getOrDefault(devPath, vendorId + ":" + productId), vendorMap.getOrDefault(devPath, ""), vendorId, productId, serialMap.getOrDefault(devPath, ""), devPath, usbDevices);
    }
}
