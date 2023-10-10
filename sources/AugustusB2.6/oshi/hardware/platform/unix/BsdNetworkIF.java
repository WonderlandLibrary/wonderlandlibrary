// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix;

import org.slf4j.LoggerFactory;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.Iterator;
import java.util.ArrayList;
import oshi.hardware.NetworkIF;
import java.util.List;
import java.net.NetworkInterface;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractNetworkIF;

@ThreadSafe
public final class BsdNetworkIF extends AbstractNetworkIF
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
    private long timeStamp;
    
    public BsdNetworkIF(final NetworkInterface netint) throws InstantiationException {
        super(netint);
        this.updateAttributes();
    }
    
    public static List<NetworkIF> getNetworks(final boolean includeLocalInterfaces) {
        final List<NetworkIF> ifList = new ArrayList<NetworkIF>();
        for (final NetworkInterface ni : AbstractNetworkIF.getNetworkInterfaces(includeLocalInterfaces)) {
            try {
                ifList.add(new BsdNetworkIF(ni));
            }
            catch (InstantiationException e) {
                BsdNetworkIF.LOG.debug("Network Interface Instantiation failed: {}", e.getMessage());
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
        return 0L;
    }
    
    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    @Override
    public boolean updateAttributes() {
        final String stats = ExecutingCommand.getAnswerAt("netstat -bI " + this.getName(), 1);
        this.timeStamp = System.currentTimeMillis();
        final String[] split = ParseUtil.whitespaces.split(stats);
        if (split.length < 12) {
            return false;
        }
        this.bytesSent = ParseUtil.parseUnsignedLongOrDefault(split[10], 0L);
        this.bytesRecv = ParseUtil.parseUnsignedLongOrDefault(split[7], 0L);
        this.packetsSent = ParseUtil.parseUnsignedLongOrDefault(split[8], 0L);
        this.packetsRecv = ParseUtil.parseUnsignedLongOrDefault(split[4], 0L);
        this.outErrors = ParseUtil.parseUnsignedLongOrDefault(split[9], 0L);
        this.inErrors = ParseUtil.parseUnsignedLongOrDefault(split[5], 0L);
        this.collisions = ParseUtil.parseUnsignedLongOrDefault(split[11], 0L);
        this.inDrops = ParseUtil.parseUnsignedLongOrDefault(split[6], 0L);
        return true;
    }
    
    static {
        LOG = LoggerFactory.getLogger(BsdNetworkIF.class);
    }
}
