// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import com.sun.jna.platform.win32.VersionHelpers;
import oshi.util.ParseUtil;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import oshi.software.os.InternetProtocolStats;
import com.sun.jna.platform.win32.IPHlpAPI;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractInternetProtocolStats;

@ThreadSafe
public class WindowsInternetProtocolStats extends AbstractInternetProtocolStats
{
    private static final IPHlpAPI IPHLP;
    private static final boolean IS_VISTA_OR_GREATER;
    
    @Override
    public InternetProtocolStats.TcpStats getTCPv4Stats() {
        final IPHlpAPI.MIB_TCPSTATS stats = new IPHlpAPI.MIB_TCPSTATS();
        WindowsInternetProtocolStats.IPHLP.GetTcpStatisticsEx(stats, 2);
        return new InternetProtocolStats.TcpStats(stats.dwCurrEstab, stats.dwActiveOpens, stats.dwPassiveOpens, stats.dwAttemptFails, stats.dwEstabResets, stats.dwOutSegs, stats.dwInSegs, stats.dwRetransSegs, stats.dwInErrs, stats.dwOutRsts);
    }
    
    @Override
    public InternetProtocolStats.TcpStats getTCPv6Stats() {
        final IPHlpAPI.MIB_TCPSTATS stats = new IPHlpAPI.MIB_TCPSTATS();
        WindowsInternetProtocolStats.IPHLP.GetTcpStatisticsEx(stats, 23);
        return new InternetProtocolStats.TcpStats(stats.dwCurrEstab, stats.dwActiveOpens, stats.dwPassiveOpens, stats.dwAttemptFails, stats.dwEstabResets, stats.dwOutSegs, stats.dwInSegs, stats.dwRetransSegs, stats.dwInErrs, stats.dwOutRsts);
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv4Stats() {
        final IPHlpAPI.MIB_UDPSTATS stats = new IPHlpAPI.MIB_UDPSTATS();
        WindowsInternetProtocolStats.IPHLP.GetUdpStatisticsEx(stats, 2);
        return new InternetProtocolStats.UdpStats(stats.dwOutDatagrams, stats.dwInDatagrams, stats.dwNoPorts, stats.dwInErrors);
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv6Stats() {
        final IPHlpAPI.MIB_UDPSTATS stats = new IPHlpAPI.MIB_UDPSTATS();
        WindowsInternetProtocolStats.IPHLP.GetUdpStatisticsEx(stats, 23);
        return new InternetProtocolStats.UdpStats(stats.dwOutDatagrams, stats.dwInDatagrams, stats.dwNoPorts, stats.dwInErrors);
    }
    
    @Override
    public List<InternetProtocolStats.IPConnection> getConnections() {
        if (WindowsInternetProtocolStats.IS_VISTA_OR_GREATER) {
            final List<InternetProtocolStats.IPConnection> conns = new ArrayList<InternetProtocolStats.IPConnection>();
            conns.addAll(queryTCPv4Connections());
            conns.addAll(queryTCPv6Connections());
            conns.addAll(queryUDPv4Connections());
            conns.addAll(queryUDPv6Connections());
            return conns;
        }
        return Collections.emptyList();
    }
    
    private static List<InternetProtocolStats.IPConnection> queryTCPv4Connections() {
        final List<InternetProtocolStats.IPConnection> conns = new ArrayList<InternetProtocolStats.IPConnection>();
        final IntByReference sizePtr = new IntByReference();
        WindowsInternetProtocolStats.IPHLP.GetExtendedTcpTable(null, sizePtr, false, 2, 5, 0);
        int size;
        Memory buf;
        do {
            size = sizePtr.getValue();
            buf = new Memory(size);
            WindowsInternetProtocolStats.IPHLP.GetExtendedTcpTable(buf, sizePtr, false, 2, 5, 0);
        } while (size < sizePtr.getValue());
        final IPHlpAPI.MIB_TCPTABLE_OWNER_PID tcpTable = new IPHlpAPI.MIB_TCPTABLE_OWNER_PID(buf);
        for (int i = 0; i < tcpTable.dwNumEntries; ++i) {
            final IPHlpAPI.MIB_TCPROW_OWNER_PID row = tcpTable.table[i];
            conns.add(new InternetProtocolStats.IPConnection("tcp4", ParseUtil.parseIntToIP(row.dwLocalAddr), ParseUtil.bigEndian16ToLittleEndian(row.dwLocalPort), ParseUtil.parseIntToIP(row.dwRemoteAddr), ParseUtil.bigEndian16ToLittleEndian(row.dwRemotePort), stateLookup(row.dwState), 0, 0, row.dwOwningPid));
        }
        return conns;
    }
    
    private static List<InternetProtocolStats.IPConnection> queryTCPv6Connections() {
        final List<InternetProtocolStats.IPConnection> conns = new ArrayList<InternetProtocolStats.IPConnection>();
        final IntByReference sizePtr = new IntByReference();
        WindowsInternetProtocolStats.IPHLP.GetExtendedTcpTable(null, sizePtr, false, 23, 5, 0);
        int size;
        Memory buf;
        do {
            size = sizePtr.getValue();
            buf = new Memory(size);
            WindowsInternetProtocolStats.IPHLP.GetExtendedTcpTable(buf, sizePtr, false, 23, 5, 0);
        } while (size < sizePtr.getValue());
        final IPHlpAPI.MIB_TCP6TABLE_OWNER_PID tcpTable = new IPHlpAPI.MIB_TCP6TABLE_OWNER_PID(buf);
        for (int i = 0; i < tcpTable.dwNumEntries; ++i) {
            final IPHlpAPI.MIB_TCP6ROW_OWNER_PID row = tcpTable.table[i];
            conns.add(new InternetProtocolStats.IPConnection("tcp6", row.LocalAddr, ParseUtil.bigEndian16ToLittleEndian(row.dwLocalPort), row.RemoteAddr, ParseUtil.bigEndian16ToLittleEndian(row.dwRemotePort), stateLookup(row.State), 0, 0, row.dwOwningPid));
        }
        return conns;
    }
    
    private static List<InternetProtocolStats.IPConnection> queryUDPv4Connections() {
        final List<InternetProtocolStats.IPConnection> conns = new ArrayList<InternetProtocolStats.IPConnection>();
        final IntByReference sizePtr = new IntByReference();
        WindowsInternetProtocolStats.IPHLP.GetExtendedUdpTable(null, sizePtr, false, 2, 1, 0);
        int size;
        Memory buf;
        do {
            size = sizePtr.getValue();
            buf = new Memory(size);
            WindowsInternetProtocolStats.IPHLP.GetExtendedUdpTable(buf, sizePtr, false, 2, 1, 0);
        } while (size < sizePtr.getValue());
        final IPHlpAPI.MIB_UDPTABLE_OWNER_PID udpTable = new IPHlpAPI.MIB_UDPTABLE_OWNER_PID(buf);
        for (int i = 0; i < udpTable.dwNumEntries; ++i) {
            final IPHlpAPI.MIB_UDPROW_OWNER_PID row = udpTable.table[i];
            conns.add(new InternetProtocolStats.IPConnection("udp4", ParseUtil.parseIntToIP(row.dwLocalAddr), ParseUtil.bigEndian16ToLittleEndian(row.dwLocalPort), new byte[0], 0, InternetProtocolStats.TcpState.NONE, 0, 0, row.dwOwningPid));
        }
        return conns;
    }
    
    private static List<InternetProtocolStats.IPConnection> queryUDPv6Connections() {
        final List<InternetProtocolStats.IPConnection> conns = new ArrayList<InternetProtocolStats.IPConnection>();
        final IntByReference sizePtr = new IntByReference();
        WindowsInternetProtocolStats.IPHLP.GetExtendedUdpTable(null, sizePtr, false, 23, 1, 0);
        int size;
        Memory buf;
        do {
            size = sizePtr.getValue();
            buf = new Memory(size);
            WindowsInternetProtocolStats.IPHLP.GetExtendedUdpTable(buf, sizePtr, false, 23, 1, 0);
        } while (size < sizePtr.getValue());
        final IPHlpAPI.MIB_UDP6TABLE_OWNER_PID udpTable = new IPHlpAPI.MIB_UDP6TABLE_OWNER_PID(buf);
        for (int i = 0; i < udpTable.dwNumEntries; ++i) {
            final IPHlpAPI.MIB_UDP6ROW_OWNER_PID row = udpTable.table[i];
            conns.add(new InternetProtocolStats.IPConnection("udp6", row.ucLocalAddr, ParseUtil.bigEndian16ToLittleEndian(row.dwLocalPort), new byte[0], 0, InternetProtocolStats.TcpState.NONE, 0, 0, row.dwOwningPid));
        }
        return conns;
    }
    
    private static InternetProtocolStats.TcpState stateLookup(final int state) {
        switch (state) {
            case 1:
            case 12: {
                return InternetProtocolStats.TcpState.CLOSED;
            }
            case 2: {
                return InternetProtocolStats.TcpState.LISTEN;
            }
            case 3: {
                return InternetProtocolStats.TcpState.SYN_SENT;
            }
            case 4: {
                return InternetProtocolStats.TcpState.SYN_RECV;
            }
            case 5: {
                return InternetProtocolStats.TcpState.ESTABLISHED;
            }
            case 6: {
                return InternetProtocolStats.TcpState.FIN_WAIT_1;
            }
            case 7: {
                return InternetProtocolStats.TcpState.FIN_WAIT_2;
            }
            case 8: {
                return InternetProtocolStats.TcpState.CLOSE_WAIT;
            }
            case 9: {
                return InternetProtocolStats.TcpState.CLOSING;
            }
            case 10: {
                return InternetProtocolStats.TcpState.LAST_ACK;
            }
            case 11: {
                return InternetProtocolStats.TcpState.TIME_WAIT;
            }
            default: {
                return InternetProtocolStats.TcpState.UNKNOWN;
            }
        }
    }
    
    static {
        IPHLP = IPHlpAPI.INSTANCE;
        IS_VISTA_OR_GREATER = VersionHelpers.IsWindowsVistaOrGreater();
    }
}
