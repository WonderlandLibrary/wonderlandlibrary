// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import com.sun.jna.PointerType;
import com.sun.jna.Pointer;
import java.util.Collections;
import com.sun.jna.platform.mac.IOKit;
import java.util.Map;
import com.sun.jna.platform.mac.IOKitUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import oshi.hardware.UsbDevice;
import java.util.List;
import com.sun.jna.platform.mac.CoreFoundation;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractUsbDevice;

@Immutable
public class MacUsbDevice extends AbstractUsbDevice
{
    private static final CoreFoundation CF;
    private static final String IOUSB = "IOUSB";
    private static final String IOSERVICE = "IOService";
    
    public MacUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        super(name, vendor, vendorId, productId, serialNumber, uniqueDeviceId, connectedDevices);
    }
    
    public static List<UsbDevice> getUsbDevices(final boolean tree) {
        final List<UsbDevice> devices = getUsbDevices();
        if (tree) {
            return devices;
        }
        final List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
        for (final UsbDevice device : devices) {
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
        return deviceList;
    }
    
    private static List<UsbDevice> getUsbDevices() {
        final Map<Long, String> nameMap = new HashMap<Long, String>();
        final Map<Long, String> vendorMap = new HashMap<Long, String>();
        final Map<Long, String> vendorIdMap = new HashMap<Long, String>();
        final Map<Long, String> productIdMap = new HashMap<Long, String>();
        final Map<Long, String> serialMap = new HashMap<Long, String>();
        final Map<Long, List<Long>> hubMap = new HashMap<Long, List<Long>>();
        final List<Long> usbControllers = new ArrayList<Long>();
        final IOKit.IORegistryEntry root = IOKitUtil.getRoot();
        final IOKit.IOIterator iter = root.getChildIterator("IOUSB");
        if (iter != null) {
            final CoreFoundation.CFStringRef locationIDKey = CoreFoundation.CFStringRef.createCFString("locationID");
            final CoreFoundation.CFStringRef ioPropertyMatchKey = CoreFoundation.CFStringRef.createCFString("IOPropertyMatch");
            for (IOKit.IORegistryEntry device = iter.next(); device != null; device = iter.next()) {
                long id = 0L;
                final IOKit.IORegistryEntry controller = device.getParentEntry("IOService");
                if (controller != null) {
                    id = controller.getRegistryEntryID();
                    nameMap.put(id, controller.getName());
                    final CoreFoundation.CFTypeRef ref = controller.createCFProperty(locationIDKey);
                    if (ref != null) {
                        getControllerIdByLocation(id, ref, locationIDKey, ioPropertyMatchKey, vendorIdMap, productIdMap);
                        ref.release();
                    }
                    controller.release();
                }
                usbControllers.add(id);
                addDeviceAndChildrenToMaps(device, id, nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap);
                device.release();
            }
            locationIDKey.release();
            ioPropertyMatchKey.release();
            iter.release();
        }
        root.release();
        final List<UsbDevice> controllerDevices = new ArrayList<UsbDevice>();
        for (final Long controller2 : usbControllers) {
            controllerDevices.add(getDeviceAndChildren(controller2, "0000", "0000", nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        return controllerDevices;
    }
    
    private static void addDeviceAndChildrenToMaps(final IOKit.IORegistryEntry device, final long parentId, final Map<Long, String> nameMap, final Map<Long, String> vendorMap, final Map<Long, String> vendorIdMap, final Map<Long, String> productIdMap, final Map<Long, String> serialMap, final Map<Long, List<Long>> hubMap) {
        final long id = device.getRegistryEntryID();
        hubMap.computeIfAbsent(Long.valueOf(parentId), x -> new ArrayList()).add(id);
        nameMap.put(id, device.getName().trim());
        final String vendor = device.getStringProperty("USB Vendor Name");
        if (vendor != null) {
            vendorMap.put(id, vendor.trim());
        }
        final Long vendorId = device.getLongProperty("idVendor");
        if (vendorId != null) {
            vendorIdMap.put(id, String.format("%04x", 0xFFFFL & (long)vendorId));
        }
        final Long productId = device.getLongProperty("idProduct");
        if (productId != null) {
            productIdMap.put(id, String.format("%04x", 0xFFFFL & (long)productId));
        }
        final String serial = device.getStringProperty("USB Serial Number");
        if (serial != null) {
            serialMap.put(id, serial.trim());
        }
        final IOKit.IOIterator childIter = device.getChildIterator("IOUSB");
        for (IOKit.IORegistryEntry childDevice = childIter.next(); childDevice != null; childDevice = childIter.next()) {
            addDeviceAndChildrenToMaps(childDevice, id, nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap);
            childDevice.release();
        }
        childIter.release();
    }
    
    private static void addDevicesToList(final List<UsbDevice> deviceList, final List<UsbDevice> list) {
        for (final UsbDevice device : list) {
            deviceList.add(new MacUsbDevice(device.getName(), device.getVendor(), device.getVendorId(), device.getProductId(), device.getSerialNumber(), device.getUniqueDeviceId(), Collections.emptyList()));
            addDevicesToList(deviceList, device.getConnectedDevices());
        }
    }
    
    private static void getControllerIdByLocation(final long id, final CoreFoundation.CFTypeRef locationId, final CoreFoundation.CFStringRef locationIDKey, final CoreFoundation.CFStringRef ioPropertyMatchKey, final Map<Long, String> vendorIdMap, final Map<Long, String> productIdMap) {
        final CoreFoundation.CFMutableDictionaryRef propertyDict = MacUsbDevice.CF.CFDictionaryCreateMutable(MacUsbDevice.CF.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
        propertyDict.setValue(locationIDKey, locationId);
        final CoreFoundation.CFMutableDictionaryRef matchingDict = MacUsbDevice.CF.CFDictionaryCreateMutable(MacUsbDevice.CF.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
        matchingDict.setValue(ioPropertyMatchKey, propertyDict);
        final IOKit.IOIterator serviceIterator = IOKitUtil.getMatchingServices(matchingDict);
        propertyDict.release();
        boolean found = false;
        if (serviceIterator != null) {
            for (IOKit.IORegistryEntry matchingService = serviceIterator.next(); matchingService != null && !found; matchingService = serviceIterator.next()) {
                final IOKit.IORegistryEntry parent = matchingService.getParentEntry("IOService");
                if (parent != null) {
                    final byte[] vid = parent.getByteArrayProperty("vendor-id");
                    if (vid != null && vid.length >= 2) {
                        vendorIdMap.put(id, String.format("%02x%02x", vid[1], vid[0]));
                        found = true;
                    }
                    final byte[] pid = parent.getByteArrayProperty("device-id");
                    if (pid != null && pid.length >= 2) {
                        productIdMap.put(id, String.format("%02x%02x", pid[1], pid[0]));
                        found = true;
                    }
                    parent.release();
                }
                matchingService.release();
            }
            serviceIterator.release();
        }
    }
    
    private static MacUsbDevice getDeviceAndChildren(final Long registryEntryId, final String vid, final String pid, final Map<Long, String> nameMap, final Map<Long, String> vendorMap, final Map<Long, String> vendorIdMap, final Map<Long, String> productIdMap, final Map<Long, String> serialMap, final Map<Long, List<Long>> hubMap) {
        final String vendorId = vendorIdMap.getOrDefault(registryEntryId, vid);
        final String productId = productIdMap.getOrDefault(registryEntryId, pid);
        final List<Long> childIds = hubMap.getOrDefault(registryEntryId, new ArrayList<Long>());
        final List<UsbDevice> usbDevices = new ArrayList<UsbDevice>();
        for (final Long id : childIds) {
            usbDevices.add(getDeviceAndChildren(id, vendorId, productId, nameMap, vendorMap, vendorIdMap, productIdMap, serialMap, hubMap));
        }
        Collections.sort(usbDevices);
        return new MacUsbDevice(nameMap.getOrDefault(registryEntryId, vendorId + ":" + productId), vendorMap.getOrDefault(registryEntryId, ""), vendorId, productId, serialMap.getOrDefault(registryEntryId, ""), "0x" + Long.toHexString(registryEntryId), usbDevices);
    }
    
    static {
        CF = CoreFoundation.INSTANCE;
    }
}
