// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.Arrays;
import oshi.annotation.concurrent.Immutable;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface InternetProtocolStats
{
    TcpStats getTCPv4Stats();
    
    TcpStats getTCPv6Stats();
    
    UdpStats getUDPv4Stats();
    
    UdpStats getUDPv6Stats();
    
    List<IPConnection> getConnections();
    
    @Immutable
    public static final class TcpStats
    {
        private final long connectionsEstablished;
        private final long connectionsActive;
        private final long connectionsPassive;
        private final long connectionFailures;
        private final long connectionsReset;
        private final long segmentsSent;
        private final long segmentsReceived;
        private final long segmentsRetransmitted;
        private final long inErrors;
        private final long outResets;
        
        public TcpStats(final long connectionsEstablished, final long connectionsActive, final long connectionsPassive, final long connectionFailures, final long connectionsReset, final long segmentsSent, final long segmentsReceived, final long segmentsRetransmitted, final long inErrors, final long outResets) {
            this.connectionsEstablished = connectionsEstablished;
            this.connectionsActive = connectionsActive;
            this.connectionsPassive = connectionsPassive;
            this.connectionFailures = connectionFailures;
            this.connectionsReset = connectionsReset;
            this.segmentsSent = segmentsSent;
            this.segmentsReceived = segmentsReceived;
            this.segmentsRetransmitted = segmentsRetransmitted;
            this.inErrors = inErrors;
            this.outResets = outResets;
        }
        
        public long getConnectionsEstablished() {
            return this.connectionsEstablished;
        }
        
        public long getConnectionsActive() {
            return this.connectionsActive;
        }
        
        public long getConnectionsPassive() {
            return this.connectionsPassive;
        }
        
        public long getConnectionFailures() {
            return this.connectionFailures;
        }
        
        public long getConnectionsReset() {
            return this.connectionsReset;
        }
        
        public long getSegmentsSent() {
            return this.segmentsSent;
        }
        
        public long getSegmentsReceived() {
            return this.segmentsReceived;
        }
        
        public long getSegmentsRetransmitted() {
            return this.segmentsRetransmitted;
        }
        
        public long getInErrors() {
            return this.inErrors;
        }
        
        public long getOutResets() {
            return this.outResets;
        }
        
        @Override
        public String toString() {
            return "TcpStats [connectionsEstablished=" + this.connectionsEstablished + ", connectionsActive=" + this.connectionsActive + ", connectionsPassive=" + this.connectionsPassive + ", connectionFailures=" + this.connectionFailures + ", connectionsReset=" + this.connectionsReset + ", segmentsSent=" + this.segmentsSent + ", segmentsReceived=" + this.segmentsReceived + ", segmentsRetransmitted=" + this.segmentsRetransmitted + ", inErrors=" + this.inErrors + ", outResets=" + this.outResets + "]";
        }
    }
    
    @Immutable
    public static final class UdpStats
    {
        private final long datagramsSent;
        private final long datagramsReceived;
        private final long datagramsNoPort;
        private final long datagramsReceivedErrors;
        
        public UdpStats(final long datagramsSent, final long datagramsReceived, final long datagramsNoPort, final long datagramsReceivedErrors) {
            this.datagramsSent = datagramsSent;
            this.datagramsReceived = datagramsReceived;
            this.datagramsNoPort = datagramsNoPort;
            this.datagramsReceivedErrors = datagramsReceivedErrors;
        }
        
        public long getDatagramsSent() {
            return this.datagramsSent;
        }
        
        public long getDatagramsReceived() {
            return this.datagramsReceived;
        }
        
        public long getDatagramsNoPort() {
            return this.datagramsNoPort;
        }
        
        public long getDatagramsReceivedErrors() {
            return this.datagramsReceivedErrors;
        }
        
        @Override
        public String toString() {
            return "UdpStats [datagramsSent=" + this.datagramsSent + ", datagramsReceived=" + this.datagramsReceived + ", datagramsNoPort=" + this.datagramsNoPort + ", datagramsReceivedErrors=" + this.datagramsReceivedErrors + "]";
        }
    }
    
    public enum TcpState
    {
        UNKNOWN, 
        CLOSED, 
        LISTEN, 
        SYN_SENT, 
        SYN_RECV, 
        ESTABLISHED, 
        FIN_WAIT_1, 
        FIN_WAIT_2, 
        CLOSE_WAIT, 
        CLOSING, 
        LAST_ACK, 
        TIME_WAIT, 
        NONE;
        
        private static /* synthetic */ TcpState[] $values() {
            return new TcpState[] { TcpState.UNKNOWN, TcpState.CLOSED, TcpState.LISTEN, TcpState.SYN_SENT, TcpState.SYN_RECV, TcpState.ESTABLISHED, TcpState.FIN_WAIT_1, TcpState.FIN_WAIT_2, TcpState.CLOSE_WAIT, TcpState.CLOSING, TcpState.LAST_ACK, TcpState.TIME_WAIT, TcpState.NONE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    @Immutable
    public static final class IPConnection
    {
        private final String type;
        private final byte[] localAddress;
        private final int localPort;
        private final byte[] foreignAddress;
        private final int foreignPort;
        private final TcpState state;
        private final int transmitQueue;
        private final int receiveQueue;
        private int owningProcessId;
        
        public IPConnection(final String type, final byte[] localAddress, final int localPort, final byte[] foreignAddress, final int foreignPort, final TcpState state, final int transmitQueue, final int receiveQueue, final int owningProcessId) {
            this.type = type;
            this.localAddress = Arrays.copyOf(localAddress, localAddress.length);
            this.localPort = localPort;
            this.foreignAddress = Arrays.copyOf(foreignAddress, foreignAddress.length);
            this.foreignPort = foreignPort;
            this.state = state;
            this.transmitQueue = transmitQueue;
            this.receiveQueue = receiveQueue;
            this.owningProcessId = owningProcessId;
        }
        
        public String getType() {
            return this.type;
        }
        
        public byte[] getLocalAddress() {
            return Arrays.copyOf(this.localAddress, this.localAddress.length);
        }
        
        public int getLocalPort() {
            return this.localPort;
        }
        
        public byte[] getForeignAddress() {
            return Arrays.copyOf(this.foreignAddress, this.foreignAddress.length);
        }
        
        public int getForeignPort() {
            return this.foreignPort;
        }
        
        public TcpState getState() {
            return this.state;
        }
        
        public int getTransmitQueue() {
            return this.transmitQueue;
        }
        
        public int getReceiveQueue() {
            return this.receiveQueue;
        }
        
        public int getowningProcessId() {
            return this.owningProcessId;
        }
        
        @Override
        public String toString() {
            String localIp = "*";
            try {
                localIp = InetAddress.getByAddress(this.localAddress).toString();
            }
            catch (UnknownHostException ex) {}
            String foreignIp = "*";
            try {
                foreignIp = InetAddress.getByAddress(this.foreignAddress).toString();
            }
            catch (UnknownHostException ex2) {}
            return "IPConnection [type=" + this.type + ", localAddress=" + localIp + ", localPort=" + this.localPort + ", foreignAddress=" + foreignIp + ", foreignPort=" + this.foreignPort + ", state=" + this.state + ", transmitQueue=" + this.transmitQueue + ", receiveQueue=" + this.receiveQueue + ", owningProcessId=" + this.owningProcessId + "]";
        }
    }
}
