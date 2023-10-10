// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import org.slf4j.LoggerFactory;
import oshi.util.FileUtil;
import java.io.File;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import oshi.util.Util;
import com.sun.jna.platform.linux.Udev;
import java.net.NetworkInterface;
import oshi.hardware.NetworkIF;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractNetworkIF;

@ThreadSafe
public final class LinuxNetworkIF extends AbstractNetworkIF
{
    private static final Logger LOG;
    private int ifType;
    private boolean connectorPresent;
    private long bytesRecv;
    private long bytesSent;
    private long packetsRecv;
    private long packetsSent;
    private long inErrors;
    private long outErrors;
    private long inDrops;
    private long collisions;
    private long speed;
    private long timeStamp;
    private String ifAlias;
    private NetworkIF.IfOperStatus ifOperStatus;
    
    public LinuxNetworkIF(final NetworkInterface netint) throws InstantiationException {
        super(netint, queryIfModel(netint));
        this.updateAttributes();
    }
    
    private static String queryIfModel(final NetworkInterface netint) {
        final String name = netint.getName();
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        if (udev != null) {
            try {
                final Udev.UdevDevice device = udev.deviceNewFromSyspath("/sys/class/net/" + name);
                if (device != null) {
                    try {
                        final String devVendor = device.getPropertyValue("ID_VENDOR_FROM_DATABASE");
                        final String devModel = device.getPropertyValue("ID_MODEL_FROM_DATABASE");
                        if (!Util.isBlank(devModel)) {
                            if (!Util.isBlank(devVendor)) {
                                return devVendor + " " + devModel;
                            }
                            return devModel;
                        }
                    }
                    finally {
                        device.unref();
                    }
                }
            }
            finally {
                udev.unref();
            }
        }
        return name;
    }
    
    public static List<NetworkIF> getNetworks(final boolean includeLocalInterfaces) {
        final List<NetworkIF> ifList = new ArrayList<NetworkIF>();
        for (final NetworkInterface ni : AbstractNetworkIF.getNetworkInterfaces(includeLocalInterfaces)) {
            try {
                ifList.add(new LinuxNetworkIF(ni));
            }
            catch (InstantiationException e) {
                LinuxNetworkIF.LOG.debug("Network Interface Instantiation failed: {}", e.getMessage());
            }
        }
        return ifList;
    }
    
    @Override
    public int getIfType() {
        return this.ifType;
    }
    
    @Override
    public boolean isConnectorPresent() {
        return this.connectorPresent;
    }
    
    @Override
    public long getBytesRecv() {
        return this.bytesRecv;
    }
    
    @Override
    public long getBytesSent() {
        return this.bytesSent;
    }
    
    @Override
    public long getPacketsRecv() {
        return this.packetsRecv;
    }
    
    @Override
    public long getPacketsSent() {
        return this.packetsSent;
    }
    
    @Override
    public long getInErrors() {
        return this.inErrors;
    }
    
    @Override
    public long getOutErrors() {
        return this.outErrors;
    }
    
    @Override
    public long getInDrops() {
        return this.inDrops;
    }
    
    @Override
    public long getCollisions() {
        return this.collisions;
    }
    
    @Override
    public long getSpeed() {
        return this.speed;
    }
    
    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    @Override
    public String getIfAlias() {
        return this.ifAlias;
    }
    
    @Override
    public NetworkIF.IfOperStatus getIfOperStatus() {
        return this.ifOperStatus;
    }
    
    @Override
    public boolean updateAttributes() {
        try {
            final File ifDir = new File(String.format("/sys/class/net/%s/statistics", this.getName()));
            if (!ifDir.isDirectory()) {
                return false;
            }
        }
        catch (SecurityException e) {
            return false;
        }
        final String ifTypePath = String.format("/sys/class/net/%s/type", this.getName());
        final String carrierPath = String.format("/sys/class/net/%s/carrier", this.getName());
        final String txBytesPath = String.format("/sys/class/net/%s/statistics/tx_bytes", this.getName());
        final String rxBytesPath = String.format("/sys/class/net/%s/statistics/rx_bytes", this.getName());
        final String txPacketsPath = String.format("/sys/class/net/%s/statistics/tx_packets", this.getName());
        final String rxPacketsPath = String.format("/sys/class/net/%s/statistics/rx_packets", this.getName());
        final String txErrorsPath = String.format("/sys/class/net/%s/statistics/tx_errors", this.getName());
        final String rxErrorsPath = String.format("/sys/class/net/%s/statistics/rx_errors", this.getName());
        final String collisionsPath = String.format("/sys/class/net/%s/statistics/collisions", this.getName());
        final String rxDropsPath = String.format("/sys/class/net/%s/statistics/rx_dropped", this.getName());
        final String ifSpeed = String.format("/sys/class/net/%s/speed", this.getName());
        final String ifAliasPath = String.format("/sys/class/net/%s/ifalias", this.getName());
        final String ifOperStatusPath = String.format("/sys/class/net/%s/operstate", this.getName());
        this.timeStamp = System.currentTimeMillis();
        this.ifType = FileUtil.getIntFromFile(ifTypePath);
        this.connectorPresent = (FileUtil.getIntFromFile(carrierPath) > 0);
        this.bytesSent = FileUtil.getUnsignedLongFromFile(txBytesPath);
        this.bytesRecv = FileUtil.getUnsignedLongFromFile(rxBytesPath);
        this.packetsSent = FileUtil.getUnsignedLongFromFile(txPacketsPath);
        this.packetsRecv = FileUtil.getUnsignedLongFromFile(rxPacketsPath);
        this.outErrors = FileUtil.getUnsignedLongFromFile(txErrorsPath);
        this.inErrors = FileUtil.getUnsignedLongFromFile(rxErrorsPath);
        this.collisions = FileUtil.getUnsignedLongFromFile(collisionsPath);
        this.inDrops = FileUtil.getUnsignedLongFromFile(rxDropsPath);
        final long speedMiB = FileUtil.getUnsignedLongFromFile(ifSpeed);
        this.speed = ((speedMiB < 0L) ? 0L : (speedMiB << 20));
        this.ifAlias = FileUtil.getStringFromFile(ifAliasPath);
        this.ifOperStatus = parseIfOperStatus(FileUtil.getStringFromFile(ifOperStatusPath));
        return true;
    }
    
    private static NetworkIF.IfOperStatus parseIfOperStatus(final String operState) {
        switch (operState) {
            case "up": {
                return NetworkIF.IfOperStatus.UP;
            }
            case "down": {
                return NetworkIF.IfOperStatus.DOWN;
            }
            case "testing": {
                return NetworkIF.IfOperStatus.TESTING;
            }
            case "dormant": {
                return NetworkIF.IfOperStatus.DORMANT;
            }
            case "notpresent": {
                return NetworkIF.IfOperStatus.NOT_PRESENT;
            }
            case "lowerlayerdown": {
                return NetworkIF.IfOperStatus.LOWER_LAYER_DOWN;
            }
            default: {
                return NetworkIF.IfOperStatus.UNKNOWN;
            }
        }
    }
    
    static {
        LOG = LoggerFactory.getLogger(LinuxNetworkIF.class);
    }
}
