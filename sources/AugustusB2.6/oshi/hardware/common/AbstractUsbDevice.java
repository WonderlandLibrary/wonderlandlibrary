// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.UsbDevice;

@Immutable
public abstract class AbstractUsbDevice implements UsbDevice
{
    private final String name;
    private final String vendor;
    private final String vendorId;
    private final String productId;
    private final String serialNumber;
    private final String uniqueDeviceId;
    private final List<UsbDevice> connectedDevices;
    
    protected AbstractUsbDevice(final String name, final String vendor, final String vendorId, final String productId, final String serialNumber, final String uniqueDeviceId, final List<UsbDevice> connectedDevices) {
        this.name = name;
        this.vendor = vendor;
        this.vendorId = vendorId;
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.uniqueDeviceId = uniqueDeviceId;
        this.connectedDevices = Collections.unmodifiableList((List<? extends UsbDevice>)connectedDevices);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getVendor() {
        return this.vendor;
    }
    
    @Override
    public String getVendorId() {
        return this.vendorId;
    }
    
    @Override
    public String getProductId() {
        return this.productId;
    }
    
    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }
    
    @Override
    public String getUniqueDeviceId() {
        return this.uniqueDeviceId;
    }
    
    @Override
    public List<UsbDevice> getConnectedDevices() {
        return this.connectedDevices;
    }
    
    @Override
    public int compareTo(final UsbDevice usb) {
        return this.getName().compareTo(usb.getName());
    }
    
    @Override
    public String toString() {
        return indentUsb(this, 1);
    }
    
    private static String indentUsb(final UsbDevice usbDevice, final int indent) {
        final String indentFmt = (indent > 4) ? String.format("%%%ds|-- ", indent - 4) : String.format("%%%ds", indent);
        final StringBuilder sb = new StringBuilder(String.format(indentFmt, ""));
        sb.append(usbDevice.getName());
        if (!usbDevice.getVendor().isEmpty()) {
            sb.append(" (").append(usbDevice.getVendor()).append(')');
        }
        if (!usbDevice.getSerialNumber().isEmpty()) {
            sb.append(" [s/n: ").append(usbDevice.getSerialNumber()).append(']');
        }
        for (final UsbDevice connected : usbDevice.getConnectedDevices()) {
            sb.append('\n').append(indentUsb(connected, indent + 4));
        }
        return sb.toString();
    }
}
