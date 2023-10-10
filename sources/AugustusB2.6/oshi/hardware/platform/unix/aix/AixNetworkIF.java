// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import org.slf4j.LoggerFactory;
import com.sun.jna.Native;
import java.util.Iterator;
import java.util.ArrayList;
import oshi.util.Memoizer;
import oshi.driver.unix.aix.perfstat.PerfstatNetInterface;
import oshi.hardware.NetworkIF;
import java.util.List;
import java.net.NetworkInterface;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractNetworkIF;

@ThreadSafe
public final class AixNetworkIF extends AbstractNetworkIF
{
    private static final Logger LOG;
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
    private Supplier<Perfstat.perfstat_netinterface_t[]> netstats;
    
    public AixNetworkIF(final NetworkInterface netint, final Supplier<Perfstat.perfstat_netinterface_t[]> netstats) throws InstantiationException {
        super(netint);
        this.netstats = netstats;
        this.updateAttributes();
    }
    
    public static List<NetworkIF> getNetworks(final boolean includeLocalInterfaces) {
        final Supplier<Perfstat.perfstat_netinterface_t[]> netstats = Memoizer.memoize(PerfstatNetInterface::queryNetInterfaces, Memoizer.defaultExpiration());
        final List<NetworkIF> ifList = new ArrayList<NetworkIF>();
        for (final NetworkInterface ni : AbstractNetworkIF.getNetworkInterfaces(includeLocalInterfaces)) {
            try {
                ifList.add(new AixNetworkIF(ni, netstats));
            }
            catch (InstantiationException e) {
                AixNetworkIF.LOG.debug("Network Interface Instantiation failed: {}", e.getMessage());
            }
        }
        return ifList;
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
        final Perfstat.perfstat_netinterface_t[] stats = this.netstats.get();
        final long now = System.currentTimeMillis();
        for (final Perfstat.perfstat_netinterface_t stat : stats) {
            final String name = Native.toString(stat.name);
            if (name.equals(this.getName())) {
                this.bytesSent = stat.obytes;
                this.bytesRecv = stat.ibytes;
                this.packetsSent = stat.opackets;
                this.packetsRecv = stat.ipackets;
                this.outErrors = stat.oerrors;
                this.inErrors = stat.ierrors;
                this.collisions = stat.collisions;
                this.inDrops = stat.if_iqdrops;
                this.speed = stat.bitrate;
                this.timeStamp = now;
                return true;
            }
        }
        return false;
    }
    
    static {
        LOG = LoggerFactory.getLogger(AixNetworkIF.class);
    }
}
