// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.openbsd;

import oshi.driver.unix.NetStat;
import oshi.software.os.InternetProtocolStats;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractInternetProtocolStats;

@ThreadSafe
public class OpenBsdInternetProtocolStats extends AbstractInternetProtocolStats
{
    @Override
    public InternetProtocolStats.TcpStats getTCPv4Stats() {
        return NetStat.queryTcpStats("netstat -s -p tcp");
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv4Stats() {
        return NetStat.queryUdpStats("netstat -s -p udp");
    }
}
