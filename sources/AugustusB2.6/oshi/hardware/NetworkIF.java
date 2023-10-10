// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import java.util.Arrays;
import java.net.NetworkInterface;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface NetworkIF
{
    NetworkInterface queryNetworkInterface();
    
    String getName();
    
    int getIndex();
    
    String getDisplayName();
    
    default String getIfAlias() {
        return "";
    }
    
    default IfOperStatus getIfOperStatus() {
        return IfOperStatus.UNKNOWN;
    }
    
    long getMTU();
    
    String getMacaddr();
    
    String[] getIPv4addr();
    
    Short[] getSubnetMasks();
    
    String[] getIPv6addr();
    
    Short[] getPrefixLengths();
    
    default int getIfType() {
        return 0;
    }
    
    default int getNdisPhysicalMediumType() {
        return 0;
    }
    
    default boolean isConnectorPresent() {
        return false;
    }
    
    long getBytesRecv();
    
    long getBytesSent();
    
    long getPacketsRecv();
    
    long getPacketsSent();
    
    long getInErrors();
    
    long getOutErrors();
    
    long getInDrops();
    
    long getCollisions();
    
    long getSpeed();
    
    long getTimeStamp();
    
    boolean isKnownVmMacAddr();
    
    boolean updateAttributes();
    
    public enum IfOperStatus
    {
        UP(1), 
        DOWN(2), 
        TESTING(3), 
        UNKNOWN(4), 
        DORMANT(5), 
        NOT_PRESENT(6), 
        LOWER_LAYER_DOWN(7);
        
        private final int value;
        
        private IfOperStatus(final int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
        
        public static IfOperStatus byValue(final int value) {
            return Arrays.stream(values()).filter(st -> st.getValue() == value).findFirst().orElse(IfOperStatus.UNKNOWN);
        }
        
        private static /* synthetic */ IfOperStatus[] $values() {
            return new IfOperStatus[] { IfOperStatus.UP, IfOperStatus.DOWN, IfOperStatus.TESTING, IfOperStatus.UNKNOWN, IfOperStatus.DORMANT, IfOperStatus.NOT_PRESENT, IfOperStatus.LOWER_LAYER_DOWN };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
