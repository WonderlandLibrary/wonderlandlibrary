// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.common;

import oshi.driver.unix.NetStat;
import java.util.List;
import oshi.software.os.InternetProtocolStats;

public abstract class AbstractInternetProtocolStats implements InternetProtocolStats
{
    @Override
    public TcpStats getTCPv6Stats() {
        return new TcpStats(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    }
    
    @Override
    public UdpStats getUDPv6Stats() {
        return new UdpStats(0L, 0L, 0L, 0L);
    }
    
    @Override
    public List<IPConnection> getConnections() {
        return NetStat.queryNetstat();
    }
}
