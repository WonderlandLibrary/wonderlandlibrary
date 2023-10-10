// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import org.slf4j.LoggerFactory;
import com.sun.jna.platform.unix.solaris.LibKstat;
import oshi.util.platform.unix.solaris.KstatUtil;
import oshi.software.os.unix.solaris.SolarisOperatingSystem;
import java.util.Iterator;
import java.util.ArrayList;
import oshi.hardware.NetworkIF;
import java.util.List;
import java.net.NetworkInterface;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractNetworkIF;

@ThreadSafe
public final class SolarisNetworkIF extends AbstractNetworkIF
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
    
    public SolarisNetworkIF(final NetworkInterface netint) throws InstantiationException {
        super(netint);
        this.updateAttributes();
    }
    
    public static List<NetworkIF> getNetworks(final boolean includeLocalInterfaces) {
        final List<NetworkIF> ifList = new ArrayList<NetworkIF>();
        for (final NetworkInterface ni : AbstractNetworkIF.getNetworkInterfaces(includeLocalInterfaces)) {
            try {
                ifList.add(new SolarisNetworkIF(ni));
            }
            catch (InstantiationException e) {
                SolarisNetworkIF.LOG.debug("Network Interface Instantiation failed: {}", e.getMessage());
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
        this.timeStamp = System.currentTimeMillis();
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return this.updateAttributes2();
        }
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup("link", -1, this.getName());
            if (ksp == null) {
                ksp = KstatUtil.KstatChain.lookup(null, -1, this.getName());
            }
            if (ksp != null && KstatUtil.KstatChain.read(ksp)) {
                this.bytesSent = KstatUtil.dataLookupLong(ksp, "obytes64");
                this.bytesRecv = KstatUtil.dataLookupLong(ksp, "rbytes64");
                this.packetsSent = KstatUtil.dataLookupLong(ksp, "opackets64");
                this.packetsRecv = KstatUtil.dataLookupLong(ksp, "ipackets64");
                this.outErrors = KstatUtil.dataLookupLong(ksp, "oerrors");
                this.inErrors = KstatUtil.dataLookupLong(ksp, "ierrors");
                this.collisions = KstatUtil.dataLookupLong(ksp, "collisions");
                this.inDrops = KstatUtil.dataLookupLong(ksp, "dl_idrops");
                this.speed = KstatUtil.dataLookupLong(ksp, "ifspeed");
                this.timeStamp = ksp.ks_snaptime / 1000000L;
                final boolean b = true;
                if (kc != null) {
                    kc.close();
                }
                return b;
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        return false;
    }
    
    private boolean updateAttributes2() {
        final Object[] results = KstatUtil.queryKstat2("kstat:/net/link/" + this.getName() + "/0", "obytes64", "rbytes64", "opackets64", "ipackets64", "oerrors", "ierrors", "collisions", "dl_idrops", "ifspeed", "snaptime");
        if (results[results.length - 1] == null) {
            return false;
        }
        this.bytesSent = (long)((results[0] == null) ? 0L : results[0]);
        this.bytesRecv = (long)((results[1] == null) ? 0L : results[1]);
        this.packetsSent = (long)((results[2] == null) ? 0L : results[2]);
        this.packetsRecv = (long)((results[3] == null) ? 0L : results[3]);
        this.outErrors = (long)((results[4] == null) ? 0L : results[4]);
        this.collisions = (long)((results[5] == null) ? 0L : results[5]);
        this.inDrops = (long)((results[6] == null) ? 0L : results[6]);
        this.speed = (long)((results[7] == null) ? 0L : results[7]);
        this.timeStamp = (long)results[8] / 1000000L;
        return true;
    }
    
    static {
        LOG = LoggerFactory.getLogger(SolarisNetworkIF.class);
    }
}
