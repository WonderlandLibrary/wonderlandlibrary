// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.mac.net;

import oshi.annotation.concurrent.Immutable;
import org.slf4j.LoggerFactory;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.platform.unix.LibCAPI;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class NetStat
{
    private static final Logger LOG;
    private static final int CTL_NET = 4;
    private static final int PF_ROUTE = 17;
    private static final int NET_RT_IFLIST2 = 6;
    private static final int RTM_IFINFO2 = 18;
    
    private NetStat() {
    }
    
    public static Map<Integer, IFdata> queryIFdata(final int index) {
        final Map<Integer, IFdata> data = new HashMap<Integer, IFdata>();
        final int[] mib = { 4, 17, 0, 0, 6, 0 };
        final LibCAPI.size_t.ByReference len = new LibCAPI.size_t.ByReference();
        if (0 != SystemB.INSTANCE.sysctl(mib, 6, null, len, null, LibCAPI.size_t.ZERO)) {
            NetStat.LOG.error("Didn't get buffer length for IFLIST2");
            return data;
        }
        final Memory buf = new Memory(len.longValue());
        if (0 != SystemB.INSTANCE.sysctl(mib, 6, buf, len, null, LibCAPI.size_t.ZERO)) {
            NetStat.LOG.error("Didn't get buffer for IFLIST2");
            return data;
        }
        final long now = System.currentTimeMillis();
        final int lim = (int)(buf.size() - new SystemB.IFmsgHdr().size());
        int offset = 0;
        while (offset < lim) {
            final Pointer p = buf.share(offset);
            final SystemB.IFmsgHdr ifm = new SystemB.IFmsgHdr(p);
            ifm.read();
            offset += ifm.ifm_msglen;
            if (ifm.ifm_type == 18) {
                final SystemB.IFmsgHdr2 if2m = new SystemB.IFmsgHdr2(p);
                if2m.read();
                if (index >= 0 && index != if2m.ifm_index) {
                    continue;
                }
                data.put((int)if2m.ifm_index, new IFdata(0xFF & if2m.ifm_data.ifi_type, if2m.ifm_data.ifi_opackets, if2m.ifm_data.ifi_ipackets, if2m.ifm_data.ifi_obytes, if2m.ifm_data.ifi_ibytes, if2m.ifm_data.ifi_oerrors, if2m.ifm_data.ifi_ierrors, if2m.ifm_data.ifi_collisions, if2m.ifm_data.ifi_iqdrops, if2m.ifm_data.ifi_baudrate, now));
                if (index >= 0) {
                    return data;
                }
                continue;
            }
        }
        return data;
    }
    
    static {
        LOG = LoggerFactory.getLogger(NetStat.class);
    }
    
    @Immutable
    public static class IFdata
    {
        private final int ifType;
        private final long oPackets;
        private final long iPackets;
        private final long oBytes;
        private final long iBytes;
        private final long oErrors;
        private final long iErrors;
        private final long collisions;
        private final long iDrops;
        private final long speed;
        private final long timeStamp;
        
        IFdata(final int ifType, final long oPackets, final long iPackets, final long oBytes, final long iBytes, final long oErrors, final long iErrors, final long collisions, final long iDrops, final long speed, final long timeStamp) {
            this.ifType = ifType;
            this.oPackets = (oPackets & 0xFFFFFFFFL);
            this.iPackets = (iPackets & 0xFFFFFFFFL);
            this.oBytes = (oBytes & 0xFFFFFFFFL);
            this.iBytes = (iBytes & 0xFFFFFFFFL);
            this.oErrors = (oErrors & 0xFFFFFFFFL);
            this.iErrors = (iErrors & 0xFFFFFFFFL);
            this.collisions = (collisions & 0xFFFFFFFFL);
            this.iDrops = (iDrops & 0xFFFFFFFFL);
            this.speed = (speed & 0xFFFFFFFFL);
            this.timeStamp = timeStamp;
        }
        
        public int getIfType() {
            return this.ifType;
        }
        
        public long getOPackets() {
            return this.oPackets;
        }
        
        public long getIPackets() {
            return this.iPackets;
        }
        
        public long getOBytes() {
            return this.oBytes;
        }
        
        public long getIBytes() {
            return this.iBytes;
        }
        
        public long getOErrors() {
            return this.oErrors;
        }
        
        public long getIErrors() {
            return this.iErrors;
        }
        
        public long getCollisions() {
            return this.collisions;
        }
        
        public long getIDrops() {
            return this.iDrops;
        }
        
        public long getSpeed() {
            return this.speed;
        }
        
        public long getTimeStamp() {
            return this.timeStamp;
        }
    }
}
