// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.ArrayList;
import oshi.hardware.NetworkIF;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;
import oshi.jna.platform.mac.SystemConfiguration;
import oshi.driver.mac.net.NetStat;
import java.util.Map;
import java.net.NetworkInterface;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractNetworkIF;

@ThreadSafe
public final class MacNetworkIF extends AbstractNetworkIF
{
    private static final Logger LOG;
    private int ifType;
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
    
    public MacNetworkIF(final NetworkInterface netint, final Map<Integer, NetStat.IFdata> data) throws InstantiationException {
        super(netint, queryIfDisplayName(netint));
        this.updateNetworkStats(data);
    }
    
    private static String queryIfDisplayName(final NetworkInterface netint) {
        final String name = netint.getName();
        final CoreFoundation.CFArrayRef ifArray = SystemConfiguration.INSTANCE.SCNetworkInterfaceCopyAll();
        if (ifArray != null) {
            try {
                for (int count = ifArray.getCount(), i = 0; i < count; ++i) {
                    final Pointer pNetIf = ifArray.getValueAtIndex(i);
                    final SystemConfiguration.SCNetworkInterfaceRef scNetIf = new SystemConfiguration.SCNetworkInterfaceRef(pNetIf);
                    final CoreFoundation.CFStringRef cfName = SystemConfiguration.INSTANCE.SCNetworkInterfaceGetBSDName(scNetIf);
                    if (cfName != null && name.equals(cfName.stringValue())) {
                        final CoreFoundation.CFStringRef cfDisplayName = SystemConfiguration.INSTANCE.SCNetworkInterfaceGetLocalizedDisplayName(scNetIf);
                        return cfDisplayName.stringValue();
                    }
                }
            }
            finally {
                ifArray.release();
            }
        }
        return name;
    }
    
    public static List<NetworkIF> getNetworks(final boolean includeLocalInterfaces) {
        final Map<Integer, NetStat.IFdata> data = NetStat.queryIFdata(-1);
        final List<NetworkIF> ifList = new ArrayList<NetworkIF>();
        for (final NetworkInterface ni : AbstractNetworkIF.getNetworkInterfaces(includeLocalInterfaces)) {
            try {
                ifList.add(new MacNetworkIF(ni, data));
            }
            catch (InstantiationException e) {
                MacNetworkIF.LOG.debug("Network Interface Instantiation failed: {}", e.getMessage());
            }
        }
        return ifList;
    }
    
    @Override
    public int getIfType() {
        return this.ifType;
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
    public boolean updateAttributes() {
        final int index = this.queryNetworkInterface().getIndex();
        return this.updateNetworkStats(NetStat.queryIFdata(index));
    }
    
    private boolean updateNetworkStats(final Map<Integer, NetStat.IFdata> data) {
        final int index = this.queryNetworkInterface().getIndex();
        if (data.containsKey(index)) {
            final NetStat.IFdata ifData = data.get(index);
            this.ifType = ifData.getIfType();
            this.bytesSent = ifData.getOBytes();
            this.bytesRecv = ifData.getIBytes();
            this.packetsSent = ifData.getOPackets();
            this.packetsRecv = ifData.getIPackets();
            this.outErrors = ifData.getOErrors();
            this.inErrors = ifData.getIErrors();
            this.collisions = ifData.getCollisions();
            this.inDrops = ifData.getIDrops();
            this.speed = ifData.getSpeed();
            this.timeStamp = ifData.getTimeStamp();
            return true;
        }
        return false;
    }
    
    static {
        LOG = LoggerFactory.getLogger(MacNetworkIF.class);
    }
}
