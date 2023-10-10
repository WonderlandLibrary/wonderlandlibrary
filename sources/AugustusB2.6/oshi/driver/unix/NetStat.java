// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix;

import java.net.UnknownHostException;
import java.net.InetAddress;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import oshi.software.os.InternetProtocolStats;
import java.util.Iterator;
import java.util.List;
import oshi.util.ExecutingCommand;
import oshi.util.tuples.Pair;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class NetStat
{
    private NetStat() {
    }
    
    public static Pair<Long, Long> queryTcpnetstat() {
        long tcp4 = 0L;
        long tcp5 = 0L;
        final List<String> activeConns = ExecutingCommand.runNative("netstat -n -p tcp");
        for (final String s : activeConns) {
            if (s.endsWith("ESTABLISHED")) {
                if (s.startsWith("tcp4")) {
                    ++tcp4;
                }
                else {
                    if (!s.startsWith("tcp6")) {
                        continue;
                    }
                    ++tcp5;
                }
            }
        }
        return new Pair<Long, Long>(tcp4, tcp5);
    }
    
    public static List<InternetProtocolStats.IPConnection> queryNetstat() {
        final List<InternetProtocolStats.IPConnection> connections = new ArrayList<InternetProtocolStats.IPConnection>();
        final List<String> activeConns = ExecutingCommand.runNative("netstat -n");
        for (final String s : activeConns) {
            String[] split = null;
            if (s.startsWith("tcp") || s.startsWith("udp")) {
                split = ParseUtil.whitespaces.split(s);
                if (split.length < 5) {
                    continue;
                }
                String state = (split.length == 6) ? split[5] : null;
                if ("SYN_RCVD".equals(state)) {
                    state = "SYN_RECV";
                }
                final String type = split[0];
                final Pair<byte[], Integer> local = parseIP(split[3]);
                final Pair<byte[], Integer> foreign = parseIP(split[4]);
                connections.add(new InternetProtocolStats.IPConnection(type, local.getA(), local.getB(), foreign.getA(), foreign.getB(), (state == null) ? InternetProtocolStats.TcpState.NONE : InternetProtocolStats.TcpState.valueOf(state), ParseUtil.parseIntOrDefault(split[2], 0), ParseUtil.parseIntOrDefault(split[1], 0), -1));
            }
        }
        return connections;
    }
    
    private static Pair<byte[], Integer> parseIP(final String s) {
        final int portPos = s.lastIndexOf(46);
        if (portPos > 0 && s.length() > portPos) {
            final int port = ParseUtil.parseIntOrDefault(s.substring(portPos + 1), 0);
            String ip = s.substring(0, portPos);
            try {
                return new Pair<byte[], Integer>(InetAddress.getByName(ip).getAddress(), port);
            }
            catch (UnknownHostException e) {
                try {
                    if (ip.endsWith(":") && ip.contains("::")) {
                        ip += "0";
                    }
                    else if (ip.endsWith(":") || ip.contains("::")) {
                        ip += ":0";
                    }
                    else {
                        ip += "::0";
                    }
                    return new Pair<byte[], Integer>(InetAddress.getByName(ip).getAddress(), port);
                }
                catch (UnknownHostException e2) {
                    return new Pair<byte[], Integer>(new byte[0], port);
                }
            }
        }
        return new Pair<byte[], Integer>(new byte[0], 0);
    }
    
    public static InternetProtocolStats.TcpStats queryTcpStats(final String netstatStr) {
        long connectionsEstablished = 0L;
        long connectionsActive = 0L;
        long connectionsPassive = 0L;
        long connectionFailures = 0L;
        long connectionsReset = 0L;
        long segmentsSent = 0L;
        long segmentsReceived = 0L;
        long segmentsRetransmitted = 0L;
        long inErrors = 0L;
        long outResets = 0L;
        final List<String> netstat = ExecutingCommand.runNative(netstatStr);
        for (final String s : netstat) {
            final String[] split = s.trim().split(" ", 2);
            if (split.length == 2) {
                final String s2 = split[1];
                switch (s2) {
                    case "connections established":
                    case "connection established (including accepts)":
                    case "connections established (including accepts)": {
                        connectionsEstablished = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "active connection openings": {
                        connectionsActive = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "passive connection openings": {
                        connectionsPassive = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "failed connection attempts":
                    case "bad connection attempts": {
                        connectionFailures = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "connection resets received":
                    case "dropped due to RST": {
                        connectionsReset = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "segments sent out":
                    case "packet sent":
                    case "packets sent": {
                        segmentsSent = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "segments received":
                    case "packet received":
                    case "packets received": {
                        segmentsReceived = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "segments retransmitted": {
                        segmentsRetransmitted = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "bad segments received":
                    case "discarded for bad checksum":
                    case "discarded for bad checksums":
                    case "discarded for bad header offset field":
                    case "discarded for bad header offset fields":
                    case "discarded because packet too short":
                    case "discarded for missing IPsec protection": {
                        inErrors += ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "resets sent": {
                        outResets = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    default: {
                        if (split[1].contains("retransmitted") && split[1].contains("data packet")) {
                            segmentsRetransmitted += ParseUtil.parseLongOrDefault(split[0], 0L);
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
        return new InternetProtocolStats.TcpStats(connectionsEstablished, connectionsActive, connectionsPassive, connectionFailures, connectionsReset, segmentsSent, segmentsReceived, segmentsRetransmitted, inErrors, outResets);
    }
    
    public static InternetProtocolStats.UdpStats queryUdpStats(final String netstatStr) {
        long datagramsSent = 0L;
        long datagramsReceived = 0L;
        long datagramsNoPort = 0L;
        long datagramsReceivedErrors = 0L;
        final List<String> netstat = ExecutingCommand.runNative(netstatStr);
        for (final String s : netstat) {
            final String[] split = s.trim().split(" ", 2);
            if (split.length == 2) {
                final String s2 = split[1];
                switch (s2) {
                    case "packets sent":
                    case "datagram output":
                    case "datagrams output": {
                        datagramsSent = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "packets received":
                    case "datagram received":
                    case "datagrams received": {
                        datagramsReceived = ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "packets to unknown port received":
                    case "dropped due to no socket":
                    case "broadcast/multicast datagram dropped due to no socket":
                    case "broadcast/multicast datagrams dropped due to no socket": {
                        datagramsNoPort += ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                    case "packet receive errors":
                    case "with incomplete header":
                    case "with bad data length field":
                    case "with bad checksum":
                    case "woth no checksum": {
                        datagramsReceivedErrors += ParseUtil.parseLongOrDefault(split[0], 0L);
                        continue;
                    }
                }
            }
        }
        return new InternetProtocolStats.UdpStats(datagramsSent, datagramsReceived, datagramsNoPort, datagramsReceivedErrors);
    }
}
