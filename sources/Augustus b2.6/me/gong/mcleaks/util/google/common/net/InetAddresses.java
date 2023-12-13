// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.net;

import me.gong.mcleaks.util.google.common.base.MoreObjects;
import java.util.Locale;
import me.gong.mcleaks.util.google.common.hash.Hashing;
import me.gong.mcleaks.util.google.common.io.ByteStreams;
import java.util.Arrays;
import me.gong.mcleaks.util.google.common.primitives.Ints;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.net.InetAddress;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.net.Inet4Address;
import me.gong.mcleaks.util.google.common.base.Splitter;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class InetAddresses
{
    private static final int IPV4_PART_COUNT = 4;
    private static final int IPV6_PART_COUNT = 8;
    private static final Splitter IPV4_SPLITTER;
    private static final Inet4Address LOOPBACK4;
    private static final Inet4Address ANY4;
    
    private InetAddresses() {
    }
    
    private static Inet4Address getInet4Address(final byte[] bytes) {
        Preconditions.checkArgument(bytes.length == 4, "Byte array has invalid length for an IPv4 address: %s != 4.", bytes.length);
        return (Inet4Address)bytesToInetAddress(bytes);
    }
    
    public static InetAddress forString(final String ipString) {
        final byte[] addr = ipStringToBytes(ipString);
        if (addr == null) {
            throw formatIllegalArgumentException("'%s' is not an IP string literal.", ipString);
        }
        return bytesToInetAddress(addr);
    }
    
    public static boolean isInetAddress(final String ipString) {
        return ipStringToBytes(ipString) != null;
    }
    
    @Nullable
    private static byte[] ipStringToBytes(String ipString) {
        boolean hasColon = false;
        boolean hasDot = false;
        for (int i = 0; i < ipString.length(); ++i) {
            final char c = ipString.charAt(i);
            if (c == '.') {
                hasDot = true;
            }
            else if (c == ':') {
                if (hasDot) {
                    return null;
                }
                hasColon = true;
            }
            else if (Character.digit(c, 16) == -1) {
                return null;
            }
        }
        if (hasColon) {
            if (hasDot) {
                ipString = convertDottedQuadToHex(ipString);
                if (ipString == null) {
                    return null;
                }
            }
            return textToNumericFormatV6(ipString);
        }
        if (hasDot) {
            return textToNumericFormatV4(ipString);
        }
        return null;
    }
    
    @Nullable
    private static byte[] textToNumericFormatV4(final String ipString) {
        final byte[] bytes = new byte[4];
        int i = 0;
        try {
            for (final String octet : InetAddresses.IPV4_SPLITTER.split(ipString)) {
                bytes[i++] = parseOctet(octet);
            }
        }
        catch (NumberFormatException ex) {
            return null;
        }
        return (byte[])((i == 4) ? bytes : null);
    }
    
    @Nullable
    private static byte[] textToNumericFormatV6(final String ipString) {
        final String[] parts = ipString.split(":", 10);
        if (parts.length < 3 || parts.length > 9) {
            return null;
        }
        int skipIndex = -1;
        for (int i = 1; i < parts.length - 1; ++i) {
            if (parts[i].length() == 0) {
                if (skipIndex >= 0) {
                    return null;
                }
                skipIndex = i;
            }
        }
        int partsHi;
        int partsLo;
        if (skipIndex >= 0) {
            partsHi = skipIndex;
            partsLo = parts.length - skipIndex - 1;
            if (parts[0].length() == 0 && --partsHi != 0) {
                return null;
            }
            if (parts[parts.length - 1].length() == 0 && --partsLo != 0) {
                return null;
            }
        }
        else {
            partsHi = parts.length;
            partsLo = 0;
        }
        final int partsSkipped = 8 - (partsHi + partsLo);
        Label_0148: {
            if (skipIndex >= 0) {
                if (partsSkipped >= 1) {
                    break Label_0148;
                }
            }
            else if (partsSkipped == 0) {
                break Label_0148;
            }
            return null;
        }
        final ByteBuffer rawBytes = ByteBuffer.allocate(16);
        try {
            for (int j = 0; j < partsHi; ++j) {
                rawBytes.putShort(parseHextet(parts[j]));
            }
            for (int j = 0; j < partsSkipped; ++j) {
                rawBytes.putShort((short)0);
            }
            for (int j = partsLo; j > 0; --j) {
                rawBytes.putShort(parseHextet(parts[parts.length - j]));
            }
        }
        catch (NumberFormatException ex) {
            return null;
        }
        return rawBytes.array();
    }
    
    @Nullable
    private static String convertDottedQuadToHex(final String ipString) {
        final int lastColon = ipString.lastIndexOf(58);
        final String initialPart = ipString.substring(0, lastColon + 1);
        final String dottedQuad = ipString.substring(lastColon + 1);
        final byte[] quad = textToNumericFormatV4(dottedQuad);
        if (quad == null) {
            return null;
        }
        final String penultimate = Integer.toHexString((quad[0] & 0xFF) << 8 | (quad[1] & 0xFF));
        final String ultimate = Integer.toHexString((quad[2] & 0xFF) << 8 | (quad[3] & 0xFF));
        return initialPart + penultimate + ":" + ultimate;
    }
    
    private static byte parseOctet(final String ipPart) {
        final int octet = Integer.parseInt(ipPart);
        if (octet > 255 || (ipPart.startsWith("0") && ipPart.length() > 1)) {
            throw new NumberFormatException();
        }
        return (byte)octet;
    }
    
    private static short parseHextet(final String ipPart) {
        final int hextet = Integer.parseInt(ipPart, 16);
        if (hextet > 65535) {
            throw new NumberFormatException();
        }
        return (short)hextet;
    }
    
    private static InetAddress bytesToInetAddress(final byte[] addr) {
        try {
            return InetAddress.getByAddress(addr);
        }
        catch (UnknownHostException e) {
            throw new AssertionError((Object)e);
        }
    }
    
    public static String toAddrString(final InetAddress ip) {
        Preconditions.checkNotNull(ip);
        if (ip instanceof Inet4Address) {
            return ip.getHostAddress();
        }
        Preconditions.checkArgument(ip instanceof Inet6Address);
        final byte[] bytes = ip.getAddress();
        final int[] hextets = new int[8];
        for (int i = 0; i < hextets.length; ++i) {
            hextets[i] = Ints.fromBytes((byte)0, (byte)0, bytes[2 * i], bytes[2 * i + 1]);
        }
        compressLongestRunOfZeroes(hextets);
        return hextetsToIPv6String(hextets);
    }
    
    private static void compressLongestRunOfZeroes(final int[] hextets) {
        int bestRunStart = -1;
        int bestRunLength = -1;
        int runStart = -1;
        for (int i = 0; i < hextets.length + 1; ++i) {
            if (i < hextets.length && hextets[i] == 0) {
                if (runStart < 0) {
                    runStart = i;
                }
            }
            else if (runStart >= 0) {
                final int runLength = i - runStart;
                if (runLength > bestRunLength) {
                    bestRunStart = runStart;
                    bestRunLength = runLength;
                }
                runStart = -1;
            }
        }
        if (bestRunLength >= 2) {
            Arrays.fill(hextets, bestRunStart, bestRunStart + bestRunLength, -1);
        }
    }
    
    private static String hextetsToIPv6String(final int[] hextets) {
        final StringBuilder buf = new StringBuilder(39);
        boolean lastWasNumber = false;
        for (int i = 0; i < hextets.length; ++i) {
            final boolean thisIsNumber = hextets[i] >= 0;
            if (thisIsNumber) {
                if (lastWasNumber) {
                    buf.append(':');
                }
                buf.append(Integer.toHexString(hextets[i]));
            }
            else if (i == 0 || lastWasNumber) {
                buf.append("::");
            }
            lastWasNumber = thisIsNumber;
        }
        return buf.toString();
    }
    
    public static String toUriString(final InetAddress ip) {
        if (ip instanceof Inet6Address) {
            return "[" + toAddrString(ip) + "]";
        }
        return toAddrString(ip);
    }
    
    public static InetAddress forUriString(final String hostAddr) {
        final InetAddress addr = forUriStringNoThrow(hostAddr);
        if (addr == null) {
            throw formatIllegalArgumentException("Not a valid URI IP literal: '%s'", hostAddr);
        }
        return addr;
    }
    
    @Nullable
    private static InetAddress forUriStringNoThrow(final String hostAddr) {
        Preconditions.checkNotNull(hostAddr);
        String ipString;
        int expectBytes;
        if (hostAddr.startsWith("[") && hostAddr.endsWith("]")) {
            ipString = hostAddr.substring(1, hostAddr.length() - 1);
            expectBytes = 16;
        }
        else {
            ipString = hostAddr;
            expectBytes = 4;
        }
        final byte[] addr = ipStringToBytes(ipString);
        if (addr == null || addr.length != expectBytes) {
            return null;
        }
        return bytesToInetAddress(addr);
    }
    
    public static boolean isUriInetAddress(final String ipString) {
        return forUriStringNoThrow(ipString) != null;
    }
    
    public static boolean isCompatIPv4Address(final Inet6Address ip) {
        if (!ip.isIPv4CompatibleAddress()) {
            return false;
        }
        final byte[] bytes = ip.getAddress();
        return bytes[12] != 0 || bytes[13] != 0 || bytes[14] != 0 || (bytes[15] != 0 && bytes[15] != 1);
    }
    
    public static Inet4Address getCompatIPv4Address(final Inet6Address ip) {
        Preconditions.checkArgument(isCompatIPv4Address(ip), "Address '%s' is not IPv4-compatible.", toAddrString(ip));
        return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
    }
    
    public static boolean is6to4Address(final Inet6Address ip) {
        final byte[] bytes = ip.getAddress();
        return bytes[0] == 32 && bytes[1] == 2;
    }
    
    public static Inet4Address get6to4IPv4Address(final Inet6Address ip) {
        Preconditions.checkArgument(is6to4Address(ip), "Address '%s' is not a 6to4 address.", toAddrString(ip));
        return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 2, 6));
    }
    
    public static boolean isTeredoAddress(final Inet6Address ip) {
        final byte[] bytes = ip.getAddress();
        return bytes[0] == 32 && bytes[1] == 1 && bytes[2] == 0 && bytes[3] == 0;
    }
    
    public static TeredoInfo getTeredoInfo(final Inet6Address ip) {
        Preconditions.checkArgument(isTeredoAddress(ip), "Address '%s' is not a Teredo address.", toAddrString(ip));
        final byte[] bytes = ip.getAddress();
        final Inet4Address server = getInet4Address(Arrays.copyOfRange(bytes, 4, 8));
        final int flags = ByteStreams.newDataInput(bytes, 8).readShort() & 0xFFFF;
        final int port = ~ByteStreams.newDataInput(bytes, 10).readShort() & 0xFFFF;
        final byte[] clientBytes = Arrays.copyOfRange(bytes, 12, 16);
        for (int i = 0; i < clientBytes.length; ++i) {
            clientBytes[i] ^= -1;
        }
        final Inet4Address client = getInet4Address(clientBytes);
        return new TeredoInfo(server, client, port, flags);
    }
    
    public static boolean isIsatapAddress(final Inet6Address ip) {
        if (isTeredoAddress(ip)) {
            return false;
        }
        final byte[] bytes = ip.getAddress();
        return (bytes[8] | 0x3) == 0x3 && bytes[9] == 0 && bytes[10] == 94 && bytes[11] == -2;
    }
    
    public static Inet4Address getIsatapIPv4Address(final Inet6Address ip) {
        Preconditions.checkArgument(isIsatapAddress(ip), "Address '%s' is not an ISATAP address.", toAddrString(ip));
        return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
    }
    
    public static boolean hasEmbeddedIPv4ClientAddress(final Inet6Address ip) {
        return isCompatIPv4Address(ip) || is6to4Address(ip) || isTeredoAddress(ip);
    }
    
    public static Inet4Address getEmbeddedIPv4ClientAddress(final Inet6Address ip) {
        if (isCompatIPv4Address(ip)) {
            return getCompatIPv4Address(ip);
        }
        if (is6to4Address(ip)) {
            return get6to4IPv4Address(ip);
        }
        if (isTeredoAddress(ip)) {
            return getTeredoInfo(ip).getClient();
        }
        throw formatIllegalArgumentException("'%s' has no embedded IPv4 address.", toAddrString(ip));
    }
    
    public static boolean isMappedIPv4Address(final String ipString) {
        final byte[] bytes = ipStringToBytes(ipString);
        if (bytes != null && bytes.length == 16) {
            for (int i = 0; i < 10; ++i) {
                if (bytes[i] != 0) {
                    return false;
                }
            }
            for (int i = 10; i < 12; ++i) {
                if (bytes[i] != -1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static Inet4Address getCoercedIPv4Address(final InetAddress ip) {
        if (ip instanceof Inet4Address) {
            return (Inet4Address)ip;
        }
        final byte[] bytes = ip.getAddress();
        boolean leadingBytesOfZero = true;
        for (int i = 0; i < 15; ++i) {
            if (bytes[i] != 0) {
                leadingBytesOfZero = false;
                break;
            }
        }
        if (leadingBytesOfZero && bytes[15] == 1) {
            return InetAddresses.LOOPBACK4;
        }
        if (leadingBytesOfZero && bytes[15] == 0) {
            return InetAddresses.ANY4;
        }
        final Inet6Address ip2 = (Inet6Address)ip;
        long addressAsLong = 0L;
        if (hasEmbeddedIPv4ClientAddress(ip2)) {
            addressAsLong = getEmbeddedIPv4ClientAddress(ip2).hashCode();
        }
        else {
            addressAsLong = ByteBuffer.wrap(ip2.getAddress(), 0, 8).getLong();
        }
        int coercedHash = Hashing.murmur3_32().hashLong(addressAsLong).asInt();
        coercedHash |= 0xE0000000;
        if (coercedHash == -1) {
            coercedHash = -2;
        }
        return getInet4Address(Ints.toByteArray(coercedHash));
    }
    
    public static int coerceToInteger(final InetAddress ip) {
        return ByteStreams.newDataInput(getCoercedIPv4Address(ip).getAddress()).readInt();
    }
    
    public static Inet4Address fromInteger(final int address) {
        return getInet4Address(Ints.toByteArray(address));
    }
    
    public static InetAddress fromLittleEndianByteArray(final byte[] addr) throws UnknownHostException {
        final byte[] reversed = new byte[addr.length];
        for (int i = 0; i < addr.length; ++i) {
            reversed[i] = addr[addr.length - i - 1];
        }
        return InetAddress.getByAddress(reversed);
    }
    
    public static InetAddress decrement(final InetAddress address) {
        byte[] addr;
        int i;
        for (addr = address.getAddress(), i = addr.length - 1; i >= 0 && addr[i] == 0; --i) {
            addr[i] = -1;
        }
        Preconditions.checkArgument(i >= 0, "Decrementing %s would wrap.", address);
        final byte[] array = addr;
        final int n = i;
        --array[n];
        return bytesToInetAddress(addr);
    }
    
    public static InetAddress increment(final InetAddress address) {
        byte[] addr;
        int i;
        for (addr = address.getAddress(), i = addr.length - 1; i >= 0 && addr[i] == -1; --i) {
            addr[i] = 0;
        }
        Preconditions.checkArgument(i >= 0, "Incrementing %s would wrap.", address);
        final byte[] array = addr;
        final int n = i;
        ++array[n];
        return bytesToInetAddress(addr);
    }
    
    public static boolean isMaximum(final InetAddress address) {
        final byte[] addr = address.getAddress();
        for (int i = 0; i < addr.length; ++i) {
            if (addr[i] != -1) {
                return false;
            }
        }
        return true;
    }
    
    private static IllegalArgumentException formatIllegalArgumentException(final String format, final Object... args) {
        return new IllegalArgumentException(String.format(Locale.ROOT, format, args));
    }
    
    static {
        IPV4_SPLITTER = Splitter.on('.').limit(4);
        LOOPBACK4 = (Inet4Address)forString("127.0.0.1");
        ANY4 = (Inet4Address)forString("0.0.0.0");
    }
    
    @Beta
    public static final class TeredoInfo
    {
        private final Inet4Address server;
        private final Inet4Address client;
        private final int port;
        private final int flags;
        
        public TeredoInfo(@Nullable final Inet4Address server, @Nullable final Inet4Address client, final int port, final int flags) {
            Preconditions.checkArgument(port >= 0 && port <= 65535, "port '%s' is out of range (0 <= port <= 0xffff)", port);
            Preconditions.checkArgument(flags >= 0 && flags <= 65535, "flags '%s' is out of range (0 <= flags <= 0xffff)", flags);
            this.server = MoreObjects.firstNonNull(server, InetAddresses.ANY4);
            this.client = MoreObjects.firstNonNull(client, InetAddresses.ANY4);
            this.port = port;
            this.flags = flags;
        }
        
        public Inet4Address getServer() {
            return this.server;
        }
        
        public Inet4Address getClient() {
            return this.client;
        }
        
        public int getPort() {
            return this.port;
        }
        
        public int getFlags() {
            return this.flags;
        }
    }
}
