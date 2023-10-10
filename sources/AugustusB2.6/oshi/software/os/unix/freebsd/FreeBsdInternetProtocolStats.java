// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.freebsd;

import com.sun.jna.Memory;
import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import oshi.util.ParseUtil;
import oshi.software.os.InternetProtocolStats;
import oshi.util.Memoizer;
import oshi.driver.unix.NetStat;
import oshi.jna.platform.unix.CLibrary;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractInternetProtocolStats;

@ThreadSafe
public class FreeBsdInternetProtocolStats extends AbstractInternetProtocolStats
{
    private Supplier<Pair<Long, Long>> establishedv4v6;
    private Supplier<CLibrary.BsdTcpstat> tcpstat;
    private Supplier<CLibrary.BsdUdpstat> udpstat;
    
    public FreeBsdInternetProtocolStats() {
        this.establishedv4v6 = Memoizer.memoize(NetStat::queryTcpnetstat, Memoizer.defaultExpiration());
        this.tcpstat = Memoizer.memoize(FreeBsdInternetProtocolStats::queryTcpstat, Memoizer.defaultExpiration());
        this.udpstat = Memoizer.memoize(FreeBsdInternetProtocolStats::queryUdpstat, Memoizer.defaultExpiration());
    }
    
    @Override
    public InternetProtocolStats.TcpStats getTCPv4Stats() {
        final CLibrary.BsdTcpstat tcp = this.tcpstat.get();
        return new InternetProtocolStats.TcpStats(this.establishedv4v6.get().getA(), ParseUtil.unsignedIntToLong(tcp.tcps_connattempt), ParseUtil.unsignedIntToLong(tcp.tcps_accepts), ParseUtil.unsignedIntToLong(tcp.tcps_conndrops), ParseUtil.unsignedIntToLong(tcp.tcps_drops), ParseUtil.unsignedIntToLong(tcp.tcps_sndpack), ParseUtil.unsignedIntToLong(tcp.tcps_rcvpack), ParseUtil.unsignedIntToLong(tcp.tcps_sndrexmitpack), ParseUtil.unsignedIntToLong(tcp.tcps_rcvbadsum + tcp.tcps_rcvbadoff + tcp.tcps_rcvmemdrop + tcp.tcps_rcvshort), 0L);
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv4Stats() {
        final CLibrary.BsdUdpstat stat = this.udpstat.get();
        return new InternetProtocolStats.UdpStats(ParseUtil.unsignedIntToLong(stat.udps_opackets), ParseUtil.unsignedIntToLong(stat.udps_ipackets), ParseUtil.unsignedIntToLong(stat.udps_noportmcast), ParseUtil.unsignedIntToLong(stat.udps_hdrops + stat.udps_badsum + stat.udps_badlen));
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv6Stats() {
        final CLibrary.BsdUdpstat stat = this.udpstat.get();
        return new InternetProtocolStats.UdpStats(ParseUtil.unsignedIntToLong(stat.udps_snd6_swcsum), ParseUtil.unsignedIntToLong(stat.udps_rcv6_swcsum), 0L, 0L);
    }
    
    private static CLibrary.BsdTcpstat queryTcpstat() {
        final CLibrary.BsdTcpstat ft = new CLibrary.BsdTcpstat();
        final Memory m = BsdSysctlUtil.sysctl("net.inet.tcp.stats");
        if (m != null && m.size() >= 128L) {
            ft.tcps_connattempt = m.getInt(0L);
            ft.tcps_accepts = m.getInt(4L);
            ft.tcps_drops = m.getInt(12L);
            ft.tcps_conndrops = m.getInt(16L);
            ft.tcps_sndpack = m.getInt(64L);
            ft.tcps_sndrexmitpack = m.getInt(72L);
            ft.tcps_rcvpack = m.getInt(104L);
            ft.tcps_rcvbadsum = m.getInt(112L);
            ft.tcps_rcvbadoff = m.getInt(116L);
            ft.tcps_rcvmemdrop = m.getInt(120L);
            ft.tcps_rcvshort = m.getInt(124L);
        }
        return ft;
    }
    
    private static CLibrary.BsdUdpstat queryUdpstat() {
        final CLibrary.BsdUdpstat ut = new CLibrary.BsdUdpstat();
        final Memory m = BsdSysctlUtil.sysctl("net.inet.udp.stats");
        if (m != null && m.size() >= 1644L) {
            ut.udps_ipackets = m.getInt(0L);
            ut.udps_hdrops = m.getInt(4L);
            ut.udps_badsum = m.getInt(8L);
            ut.udps_badlen = m.getInt(12L);
            ut.udps_opackets = m.getInt(36L);
            ut.udps_noportmcast = m.getInt(48L);
            ut.udps_rcv6_swcsum = m.getInt(64L);
            ut.udps_snd6_swcsum = m.getInt(80L);
        }
        return ut;
    }
}
