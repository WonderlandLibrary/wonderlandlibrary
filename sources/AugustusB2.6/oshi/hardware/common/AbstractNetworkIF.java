// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import org.slf4j.LoggerFactory;
import oshi.util.FormatUtil;
import oshi.util.FileUtil;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.net.SocketException;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import oshi.util.ParseUtil;
import oshi.util.Memoizer;
import java.util.Properties;
import java.util.function.Supplier;
import java.net.NetworkInterface;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.NetworkIF;

@ThreadSafe
public abstract class AbstractNetworkIF implements NetworkIF
{
    private static final Logger LOG;
    private static final String OSHI_VM_MAC_ADDR_PROPERTIES = "oshi.vmmacaddr.properties";
    private NetworkInterface networkInterface;
    private String name;
    private String displayName;
    private int index;
    private long mtu;
    private String mac;
    private String[] ipv4;
    private Short[] subnetMasks;
    private String[] ipv6;
    private Short[] prefixLengths;
    private final Supplier<Properties> vmMacAddrProps;
    
    protected AbstractNetworkIF(final NetworkInterface netint) throws InstantiationException {
        this(netint, netint.getDisplayName());
    }
    
    protected AbstractNetworkIF(final NetworkInterface netint, final String displayName) throws InstantiationException {
        this.vmMacAddrProps = Memoizer.memoize(AbstractNetworkIF::queryVmMacAddrProps);
        this.networkInterface = netint;
        try {
            this.name = this.networkInterface.getName();
            this.displayName = displayName;
            this.index = this.networkInterface.getIndex();
            this.mtu = ParseUtil.unsignedIntToLong(this.networkInterface.getMTU());
            final byte[] hwmac = this.networkInterface.getHardwareAddress();
            if (hwmac != null) {
                final List<String> octets = new ArrayList<String>(6);
                for (final byte b : hwmac) {
                    octets.add(String.format("%02x", b));
                }
                this.mac = String.join(":", octets);
            }
            else {
                this.mac = "unknown";
            }
            final ArrayList<String> ipv4list = new ArrayList<String>();
            final ArrayList<Short> subnetMaskList = new ArrayList<Short>();
            final ArrayList<String> ipv6list = new ArrayList<String>();
            final ArrayList<Short> prefixLengthList = new ArrayList<Short>();
            for (final InterfaceAddress interfaceAddress : this.networkInterface.getInterfaceAddresses()) {
                final InetAddress address = interfaceAddress.getAddress();
                if (address.getHostAddress().length() > 0) {
                    if (address.getHostAddress().contains(":")) {
                        ipv6list.add(address.getHostAddress().split("%")[0]);
                        prefixLengthList.add(interfaceAddress.getNetworkPrefixLength());
                    }
                    else {
                        ipv4list.add(address.getHostAddress());
                        subnetMaskList.add(interfaceAddress.getNetworkPrefixLength());
                    }
                }
            }
            this.ipv4 = ipv4list.toArray(new String[0]);
            this.subnetMasks = subnetMaskList.toArray(new Short[0]);
            this.ipv6 = ipv6list.toArray(new String[0]);
            this.prefixLengths = prefixLengthList.toArray(new Short[0]);
        }
        catch (SocketException e) {
            throw new InstantiationException(e.getMessage());
        }
    }
    
    protected static List<NetworkInterface> getNetworkInterfaces(final boolean includeLocalInterfaces) {
        final List<NetworkInterface> interfaces = getAllNetworkInterfaces();
        return includeLocalInterfaces ? interfaces : getAllNetworkInterfaces().stream().filter(networkInterface1 -> !isLocalInterface(networkInterface1)).collect((Collector<? super Object, ?, List<NetworkInterface>>)Collectors.toList());
    }
    
    private static List<NetworkInterface> getAllNetworkInterfaces() {
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            return (interfaces == null) ? Collections.emptyList() : Collections.list(interfaces);
        }
        catch (SocketException ex) {
            AbstractNetworkIF.LOG.error("Socket exception when retrieving interfaces: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }
    
    private static boolean isLocalInterface(final NetworkInterface networkInterface) {
        try {
            return networkInterface.getHardwareAddress() == null;
        }
        catch (SocketException e) {
            AbstractNetworkIF.LOG.error("Socket exception when retrieving interface information for {}: {}", networkInterface, e.getMessage());
            return false;
        }
    }
    
    @Override
    public NetworkInterface queryNetworkInterface() {
        return this.networkInterface;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public String getDisplayName() {
        return this.displayName;
    }
    
    @Override
    public long getMTU() {
        return this.mtu;
    }
    
    @Override
    public String getMacaddr() {
        return this.mac;
    }
    
    @Override
    public String[] getIPv4addr() {
        return Arrays.copyOf(this.ipv4, this.ipv4.length);
    }
    
    @Override
    public Short[] getSubnetMasks() {
        return Arrays.copyOf(this.subnetMasks, this.subnetMasks.length);
    }
    
    @Override
    public String[] getIPv6addr() {
        return Arrays.copyOf(this.ipv6, this.ipv6.length);
    }
    
    @Override
    public Short[] getPrefixLengths() {
        return Arrays.copyOf(this.prefixLengths, this.prefixLengths.length);
    }
    
    @Override
    public boolean isKnownVmMacAddr() {
        final String oui = (this.getMacaddr().length() > 7) ? this.getMacaddr().substring(0, 8) : this.getMacaddr();
        return this.vmMacAddrProps.get().containsKey(oui.toUpperCase());
    }
    
    private static Properties queryVmMacAddrProps() {
        return FileUtil.readPropertiesFromFilename("oshi.vmmacaddr.properties");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(this.getName());
        if (!this.getName().equals(this.getDisplayName())) {
            sb.append(" (").append(this.getDisplayName()).append(")");
        }
        if (!this.getIfAlias().isEmpty()) {
            sb.append(" [IfAlias=").append(this.getIfAlias()).append("]");
        }
        sb.append("\n");
        sb.append("  MAC Address: ").append(this.getMacaddr()).append("\n");
        sb.append("  MTU: ").append(this.getMTU()).append(", ").append("Speed: ").append(this.getSpeed()).append("\n");
        final String[] ipv4withmask = this.getIPv4addr();
        if (this.ipv4.length == this.subnetMasks.length) {
            for (int i = 0; i < this.subnetMasks.length; ++i) {
                final StringBuilder sb2 = new StringBuilder();
                final String[] array = ipv4withmask;
                final int n = i;
                array[n] = sb2.append(array[n]).append("/").append(this.subnetMasks[i]).toString();
            }
        }
        sb.append("  IPv4: ").append(Arrays.toString(ipv4withmask)).append("\n");
        final String[] ipv6withprefixlength = this.getIPv6addr();
        if (this.ipv6.length == this.prefixLengths.length) {
            for (int j = 0; j < this.prefixLengths.length; ++j) {
                final StringBuilder sb3 = new StringBuilder();
                final String[] array2 = ipv6withprefixlength;
                final int n2 = j;
                array2[n2] = sb3.append(array2[n2]).append("/").append(this.prefixLengths[j]).toString();
            }
        }
        sb.append("  IPv6: ").append(Arrays.toString(ipv6withprefixlength)).append("\n");
        sb.append("  Traffic: received ").append(this.getPacketsRecv()).append(" packets/").append(FormatUtil.formatBytes(this.getBytesRecv())).append(" (" + this.getInErrors() + " err, ").append(this.getInDrops() + " drop);");
        sb.append(" transmitted ").append(this.getPacketsSent()).append(" packets/").append(FormatUtil.formatBytes(this.getBytesSent())).append(" (" + this.getOutErrors() + " err, ").append(this.getCollisions() + " coll);");
        return sb.toString();
    }
    
    static {
        LOG = LoggerFactory.getLogger(AbstractNetworkIF.class);
    }
}
