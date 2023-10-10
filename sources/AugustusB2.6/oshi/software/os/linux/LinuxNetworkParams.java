// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import org.slf4j.LoggerFactory;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import com.sun.jna.Native;
import com.sun.jna.platform.linux.LibC;
import com.sun.jna.ptr.PointerByReference;
import java.net.UnknownHostException;
import java.net.InetAddress;
import oshi.jna.platform.unix.CLibrary;
import oshi.jna.platform.linux.LinuxLibc;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractNetworkParams;

@ThreadSafe
final class LinuxNetworkParams extends AbstractNetworkParams
{
    private static final Logger LOG;
    private static final LinuxLibc LIBC;
    private static final String IPV4_DEFAULT_DEST = "0.0.0.0";
    private static final String IPV6_DEFAULT_DEST = "::/0";
    
    @Override
    public String getDomainName() {
        final CLibrary.Addrinfo hint = new CLibrary.Addrinfo();
        hint.ai_flags = 2;
        String hostname = "";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            LinuxNetworkParams.LOG.error("Unknown host exception when getting address of local host: {}", e.getMessage());
            return "";
        }
        final PointerByReference ptr = new PointerByReference();
        final int res = LinuxNetworkParams.LIBC.getaddrinfo(hostname, null, hint, ptr);
        if (res > 0) {
            if (LinuxNetworkParams.LOG.isErrorEnabled()) {
                LinuxNetworkParams.LOG.error("Failed getaddrinfo(): {}", LinuxNetworkParams.LIBC.gai_strerror(res));
            }
            return "";
        }
        final CLibrary.Addrinfo info = new CLibrary.Addrinfo(ptr.getValue());
        final String canonname = info.ai_canonname.trim();
        LinuxNetworkParams.LIBC.freeaddrinfo(ptr.getValue());
        return canonname;
    }
    
    @Override
    public String getHostName() {
        final byte[] hostnameBuffer = new byte[256];
        if (0 != LibC.INSTANCE.gethostname(hostnameBuffer, hostnameBuffer.length)) {
            return super.getHostName();
        }
        return Native.toString(hostnameBuffer);
    }
    
    @Override
    public String getIpv4DefaultGateway() {
        final List<String> routes = ExecutingCommand.runNative("route -A inet -n");
        if (routes.size() <= 2) {
            return "";
        }
        String gateway = "";
        int minMetric = Integer.MAX_VALUE;
        for (int i = 2; i < routes.size(); ++i) {
            final String[] fields = ParseUtil.whitespaces.split(routes.get(i));
            if (fields.length > 4 && fields[0].equals("0.0.0.0")) {
                final boolean isGateway = fields[3].indexOf(71) != -1;
                final int metric = ParseUtil.parseIntOrDefault(fields[4], Integer.MAX_VALUE);
                if (isGateway && metric < minMetric) {
                    minMetric = metric;
                    gateway = fields[1];
                }
            }
        }
        return gateway;
    }
    
    @Override
    public String getIpv6DefaultGateway() {
        final List<String> routes = ExecutingCommand.runNative("route -A inet6 -n");
        if (routes.size() <= 2) {
            return "";
        }
        String gateway = "";
        int minMetric = Integer.MAX_VALUE;
        for (int i = 2; i < routes.size(); ++i) {
            final String[] fields = ParseUtil.whitespaces.split(routes.get(i));
            if (fields.length > 3 && fields[0].equals("::/0")) {
                final boolean isGateway = fields[2].indexOf(71) != -1;
                final int metric = ParseUtil.parseIntOrDefault(fields[3], Integer.MAX_VALUE);
                if (isGateway && metric < minMetric) {
                    minMetric = metric;
                    gateway = fields[1];
                }
            }
        }
        return gateway;
    }
    
    static {
        LOG = LoggerFactory.getLogger(LinuxNetworkParams.class);
        LIBC = LinuxLibc.INSTANCE;
    }
}
