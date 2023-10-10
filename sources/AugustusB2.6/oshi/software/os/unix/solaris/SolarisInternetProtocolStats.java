// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.solaris;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import java.util.Collection;
import oshi.util.ExecutingCommand;
import oshi.software.os.InternetProtocolStats;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractInternetProtocolStats;

@ThreadSafe
public class SolarisInternetProtocolStats extends AbstractInternetProtocolStats
{
    @Override
    public InternetProtocolStats.TcpStats getTCPv4Stats() {
        return getTcpStats();
    }
    
    @Override
    public InternetProtocolStats.UdpStats getUDPv4Stats() {
        return getUdpStats();
    }
    
    private static InternetProtocolStats.TcpStats getTcpStats() {
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
        final List<String> netstat = ExecutingCommand.runNative("netstat -s -P tcp");
        netstat.addAll(ExecutingCommand.runNative("netstat -s -P ip"));
        for (final String s : netstat) {
            final String[] splitOnPrefix;
            final String[] stats = splitOnPrefix = splitOnPrefix(s, "tcp");
            for (final String stat : splitOnPrefix) {
                if (stat != null) {
                    final String[] split = stat.split("=");
                    if (split.length == 2) {
                        final String trim = split[0].trim();
                        switch (trim) {
                            case "tcpCurrEstab": {
                                connectionsEstablished = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpActiveOpens": {
                                connectionsActive = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpPassiveOpens": {
                                connectionsPassive = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpAttemptFails": {
                                connectionFailures = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpEstabResets": {
                                connectionsReset = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpOutSegs": {
                                segmentsSent = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpInSegs": {
                                segmentsReceived = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpRetransSegs": {
                                segmentsRetransmitted = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "tcpInErr": {
                                inErrors = ParseUtil.getFirstIntValue(split[1].trim());
                                break;
                            }
                            case "tcpOutRsts": {
                                outResets = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return new InternetProtocolStats.TcpStats(connectionsEstablished, connectionsActive, connectionsPassive, connectionFailures, connectionsReset, segmentsSent, segmentsReceived, segmentsRetransmitted, inErrors, outResets);
    }
    
    private static InternetProtocolStats.UdpStats getUdpStats() {
        long datagramsSent = 0L;
        long datagramsReceived = 0L;
        long datagramsNoPort = 0L;
        long datagramsReceivedErrors = 0L;
        final List<String> netstat = ExecutingCommand.runNative("netstat -s -P udp");
        netstat.addAll(ExecutingCommand.runNative("netstat -s -P ip"));
        for (final String s : netstat) {
            final String[] splitOnPrefix;
            final String[] stats = splitOnPrefix = splitOnPrefix(s, "udp");
            for (final String stat : splitOnPrefix) {
                if (stat != null) {
                    final String[] split = stat.split("=");
                    if (split.length == 2) {
                        final String trim = split[0].trim();
                        switch (trim) {
                            case "udpOutDatagrams": {
                                datagramsSent = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "udpInDatagrams": {
                                datagramsReceived = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "udpNoPorts": {
                                datagramsNoPort = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                            case "udpInErrors": {
                                datagramsReceivedErrors = ParseUtil.parseLongOrDefault(split[1].trim(), 0L);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return new InternetProtocolStats.UdpStats(datagramsSent, datagramsReceived, datagramsNoPort, datagramsReceivedErrors);
    }
    
    private static String[] splitOnPrefix(final String s, final String prefix) {
        final String[] stats = new String[2];
        final int first = s.indexOf(prefix);
        if (first >= 0) {
            final int second = s.indexOf(prefix, first + 1);
            if (second >= 0) {
                stats[0] = s.substring(first, second).trim();
                stats[1] = s.substring(second).trim();
            }
            else {
                stats[0] = s.substring(first).trim();
            }
        }
        return stats;
    }
}
