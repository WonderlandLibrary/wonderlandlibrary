// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import com.sun.jna.platform.win32.VersionHelpers;
import org.slf4j.LoggerFactory;
import oshi.util.ParseUtil;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.IPHlpAPI;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.net.NetworkInterface;
import oshi.hardware.NetworkIF;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractNetworkIF;

@ThreadSafe
public final class WindowsNetworkIF extends AbstractNetworkIF
{
    private static final Logger LOG;
    private static final boolean IS_VISTA_OR_GREATER;
    private static final byte CONNECTOR_PRESENT_BIT = 4;
    private int ifType;
    private int ndisPhysicalMediumType;
    private boolean connectorPresent;
    private long bytesRecv;
    private long bytesSent;
    private long packetsRecv;
    private long packetsSent;
    private long inErrors;
    private long outErrors;
    private long inDrops;
    private long collisions;
    private long speed;
    private long timeStamp;
    private String ifAlias;
    private NetworkIF.IfOperStatus ifOperStatus;
    
    public WindowsNetworkIF(final NetworkInterface netint) throws InstantiationException {
        super(netint);
        this.updateAttributes();
    }
    
    public static List<NetworkIF> getNetworks(final boolean includeLocalInterfaces) {
        final List<NetworkIF> ifList = new ArrayList<NetworkIF>();
        for (final NetworkInterface ni : AbstractNetworkIF.getNetworkInterfaces(includeLocalInterfaces)) {
            try {
                ifList.add(new WindowsNetworkIF(ni));
            }
            catch (InstantiationException e) {
                WindowsNetworkIF.LOG.debug("Network Interface Instantiation failed: {}", e.getMessage());
            }
        }
        return ifList;
    }
    
    @Override
    public int getIfType() {
        return this.ifType;
    }
    
    @Override
    public int getNdisPhysicalMediumType() {
        return this.ndisPhysicalMediumType;
    }
    
    @Override
    public boolean isConnectorPresent() {
        return this.connectorPresent;
    }
    
    @Override
    public long getBytesRecv() {
        return this.bytesRecv;
    }
    
    @Override
    public long getBytesSent() {
        return this.bytesSent;
    }
    
    @Override
    public long getPacketsRecv() {
        return this.packetsRecv;
    }
    
    @Override
    public long getPacketsSent() {
        return this.packetsSent;
    }
    
    @Override
    public long getInErrors() {
        return this.inErrors;
    }
    
    @Override
    public long getOutErrors() {
        return this.outErrors;
    }
    
    @Override
    public long getInDrops() {
        return this.inDrops;
    }
    
    @Override
    public long getCollisions() {
        return this.collisions;
    }
    
    @Override
    public long getSpeed() {
        return this.speed;
    }
    
    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    @Override
    public String getIfAlias() {
        return this.ifAlias;
    }
    
    @Override
    public NetworkIF.IfOperStatus getIfOperStatus() {
        return this.ifOperStatus;
    }
    
    @Override
    public boolean updateAttributes() {
        if (WindowsNetworkIF.IS_VISTA_OR_GREATER) {
            final IPHlpAPI.MIB_IF_ROW2 ifRow = new IPHlpAPI.MIB_IF_ROW2();
            ifRow.InterfaceIndex = this.queryNetworkInterface().getIndex();
            if (0 != IPHlpAPI.INSTANCE.GetIfEntry2(ifRow)) {
                WindowsNetworkIF.LOG.error("Failed to retrieve data for interface {}, {}", (Object)this.queryNetworkInterface().getIndex(), this.getName());
                return false;
            }
            this.ifType = ifRow.Type;
            this.ndisPhysicalMediumType = ifRow.PhysicalMediumType;
            this.connectorPresent = ((ifRow.InterfaceAndOperStatusFlags & 0x4) > 0);
            this.bytesSent = ifRow.OutOctets;
            this.bytesRecv = ifRow.InOctets;
            this.packetsSent = ifRow.OutUcastPkts;
            this.packetsRecv = ifRow.InUcastPkts;
            this.outErrors = ifRow.OutErrors;
            this.inErrors = ifRow.InErrors;
            this.collisions = ifRow.OutDiscards;
            this.inDrops = ifRow.InDiscards;
            this.speed = ifRow.ReceiveLinkSpeed;
            this.ifAlias = Native.toString(ifRow.Alias);
            this.ifOperStatus = NetworkIF.IfOperStatus.byValue(ifRow.OperStatus);
        }
        else {
            final IPHlpAPI.MIB_IFROW ifRow2 = new IPHlpAPI.MIB_IFROW();
            ifRow2.dwIndex = this.queryNetworkInterface().getIndex();
            if (0 != IPHlpAPI.INSTANCE.GetIfEntry(ifRow2)) {
                WindowsNetworkIF.LOG.error("Failed to retrieve data for interface {}, {}", (Object)this.queryNetworkInterface().getIndex(), this.getName());
                return false;
            }
            this.ifType = ifRow2.dwType;
            this.bytesSent = ParseUtil.unsignedIntToLong(ifRow2.dwOutOctets);
            this.bytesRecv = ParseUtil.unsignedIntToLong(ifRow2.dwInOctets);
            this.packetsSent = ParseUtil.unsignedIntToLong(ifRow2.dwOutUcastPkts);
            this.packetsRecv = ParseUtil.unsignedIntToLong(ifRow2.dwInUcastPkts);
            this.outErrors = ParseUtil.unsignedIntToLong(ifRow2.dwOutErrors);
            this.inErrors = ParseUtil.unsignedIntToLong(ifRow2.dwInErrors);
            this.collisions = ParseUtil.unsignedIntToLong(ifRow2.dwOutDiscards);
            this.inDrops = ParseUtil.unsignedIntToLong(ifRow2.dwInDiscards);
            this.speed = ParseUtil.unsignedIntToLong(ifRow2.dwSpeed);
            this.ifAlias = "";
            this.ifOperStatus = NetworkIF.IfOperStatus.UNKNOWN;
        }
        this.timeStamp = System.currentTimeMillis();
        return true;
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsNetworkIF.class);
        IS_VISTA_OR_GREATER = VersionHelpers.IsWindowsVistaOrGreater();
    }
}
