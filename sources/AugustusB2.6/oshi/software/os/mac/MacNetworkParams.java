// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac;

import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import java.net.UnknownHostException;
import java.net.InetAddress;
import oshi.jna.platform.unix.CLibrary;
import oshi.jna.platform.mac.SystemB;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractNetworkParams;

@ThreadSafe
final class MacNetworkParams extends AbstractNetworkParams
{
    private static final Logger LOG;
    private static final SystemB SYS;
    private static final String IPV6_ROUTE_HEADER = "Internet6:";
    private static final String DEFAULT_GATEWAY = "default";
    
    @Override
    public String getDomainName() {
        final CLibrary.Addrinfo hint = new CLibrary.Addrinfo();
        hint.ai_flags = 2;
        String hostname = "";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            MacNetworkParams.LOG.error("Unknown host exception when getting address of local host: {}", e.getMessage());
            return "";
        }
        final PointerByReference ptr = new PointerByReference();
        final int res = MacNetworkParams.SYS.getaddrinfo(hostname, null, hint, ptr);
        if (res > 0) {
            if (MacNetworkParams.LOG.isErrorEnabled()) {
                MacNetworkParams.LOG.error("Failed getaddrinfo(): {}", MacNetworkParams.SYS.gai_strerror(res));
            }
            return "";
        }
        final CLibrary.Addrinfo info = new CLibrary.Addrinfo(ptr.getValue());
        final String canonname = info.ai_canonname.trim();
        MacNetworkParams.SYS.freeaddrinfo(ptr.getValue());
        return canonname;
    }
    
    @Override
    public String getHostName() {
        final byte[] hostnameBuffer = new byte[256];
        if (0 != MacNetworkParams.SYS.gethostname(hostnameBuffer, hostnameBuffer.length)) {
            return super.getHostName();
        }
        return Native.toString(hostnameBuffer);
    }
    
    @Override
    public String getIpv4DefaultGateway() {
        return AbstractNetworkParams.searchGateway(ExecutingCommand.runNative("route -n get default"));
    }
    
    @Override
    public String getIpv6DefaultGateway() {
        final List<String> lines = ExecutingCommand.runNative("netstat -nr");
        boolean v6Table = false;
        for (final String line : lines) {
            if (v6Table && line.startsWith("default")) {
                final String[] fields = ParseUtil.whitespaces.split(line);
                if (fields.length > 2 && fields[2].contains("G")) {
                    return fields[1].split("%")[0];
                }
                continue;
            }
            else {
                if (!line.startsWith("Internet6:")) {
                    continue;
                }
                v6Table = true;
            }
        }
        return "";
    }
    
    static {
        LOG = LoggerFactory.getLogger(MacNetworkParams.class);
        SYS = SystemB.INSTANCE;
    }
}
