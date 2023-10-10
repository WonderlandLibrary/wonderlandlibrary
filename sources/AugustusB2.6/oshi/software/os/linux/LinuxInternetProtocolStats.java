// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import oshi.util.tuples.Pair;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import java.util.Map;
import java.util.Collection;
import oshi.driver.linux.proc.ProcessStat;
import java.util.ArrayList;
import java.util.List;
import oshi.driver.unix.NetStat;
import oshi.software.os.InternetProtocolStats;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractInternetProtocolStats;

@ThreadSafe
public class LinuxInternetProtocolStats extends AbstractInternetProtocolStats
{
    @Override
    public InternetProtocolStats.TcpStats getTCPv4Stats() {
        return NetStat.queryTcpStats("netstat -st4");
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv4Stats() {
        return NetStat.queryUdpStats("netstat -su4");
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv6Stats() {
        return NetStat.queryUdpStats("netstat -su6");
    }
    
    @Override
    public List<InternetProtocolStats.IPConnection> getConnections() {
        final List<InternetProtocolStats.IPConnection> conns = new ArrayList<InternetProtocolStats.IPConnection>();
        final Map<Integer, Integer> pidMap = ProcessStat.querySocketToPidMap();
        conns.addAll(queryConnections("tcp", 4, pidMap));
        conns.addAll(queryConnections("tcp", 6, pidMap));
        conns.addAll(queryConnections("udp", 4, pidMap));
        conns.addAll(queryConnections("udp", 6, pidMap));
        return conns;
    }
    
    private static List<InternetProtocolStats.IPConnection> queryConnections(final String protocol, final int ipver, final Map<Integer, Integer> pidMap) {
        final List<InternetProtocolStats.IPConnection> conns = new ArrayList<InternetProtocolStats.IPConnection>();
        for (final String s : FileUtil.readFile(ProcPath.NET + "/" + protocol + ((ipver == 6) ? "6" : ""))) {
            if (s.indexOf(58) >= 0) {
                final String[] split = ParseUtil.whitespaces.split(s.trim());
                if (split.length <= 9) {
                    continue;
                }
                final Pair<byte[], Integer> lAddr = parseIpAddr(split[1]);
                final Pair<byte[], Integer> fAddr = parseIpAddr(split[2]);
                final InternetProtocolStats.TcpState state = stateLookup(ParseUtil.hexStringToInt(split[3], 0));
                final Pair<Integer, Integer> txQrxQ = parseHexColonHex(split[4]);
                final int inode = ParseUtil.parseIntOrDefault(split[9], 0);
                conns.add(new InternetProtocolStats.IPConnection(protocol + ipver, lAddr.getA(), lAddr.getB(), fAddr.getA(), fAddr.getB(), state, txQrxQ.getA(), txQrxQ.getB(), pidMap.getOrDefault(inode, -1)));
            }
        }
        return conns;
    }
    
    private static Pair<byte[], Integer> parseIpAddr(final String s) {
        final int colon = s.indexOf(58);
        if (colon > 0 && colon < s.length()) {
            final byte[] first = ParseUtil.hexStringToByteArray(s.substring(0, colon));
            for (int i = 0; i + 3 < first.length; i += 4) {
                byte tmp = first[i];
                first[i] = first[i + 3];
                first[i + 3] = tmp;
                tmp = first[i + 1];
                first[i + 1] = first[i + 2];
                first[i + 2] = tmp;
            }
            final int second = ParseUtil.hexStringToInt(s.substring(colon + 1), 0);
            return new Pair<byte[], Integer>(first, second);
        }
        return new Pair<byte[], Integer>(new byte[0], 0);
    }
    
    private static Pair<Integer, Integer> parseHexColonHex(final String s) {
        final int colon = s.indexOf(58);
        if (colon > 0 && colon < s.length()) {
            final int first = ParseUtil.hexStringToInt(s.substring(0, colon), 0);
            final int second = ParseUtil.hexStringToInt(s.substring(colon + 1), 0);
            return new Pair<Integer, Integer>(first, second);
        }
        return new Pair<Integer, Integer>(0, 0);
    }
    
    private static InternetProtocolStats.TcpState stateLookup(final int state) {
        switch (state) {
            case 1: {
                return InternetProtocolStats.TcpState.ESTABLISHED;
            }
            case 2: {
                return InternetProtocolStats.TcpState.SYN_SENT;
            }
            case 3: {
                return InternetProtocolStats.TcpState.SYN_RECV;
            }
            case 4: {
                return InternetProtocolStats.TcpState.FIN_WAIT_1;
            }
            case 5: {
                return InternetProtocolStats.TcpState.FIN_WAIT_2;
            }
            case 6: {
                return InternetProtocolStats.TcpState.TIME_WAIT;
            }
            case 7: {
                return InternetProtocolStats.TcpState.CLOSED;
            }
            case 8: {
                return InternetProtocolStats.TcpState.CLOSE_WAIT;
            }
            case 9: {
                return InternetProtocolStats.TcpState.LAST_ACK;
            }
            case 10: {
                return InternetProtocolStats.TcpState.LISTEN;
            }
            case 11: {
                return InternetProtocolStats.TcpState.CLOSING;
            }
            default: {
                return InternetProtocolStats.TcpState.UNKNOWN;
            }
        }
    }
}
