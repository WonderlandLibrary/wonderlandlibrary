// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.aix;

import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import com.sun.jna.Native;
import oshi.jna.platform.unix.AixLibc;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractNetworkParams;

@ThreadSafe
final class AixNetworkParams extends AbstractNetworkParams
{
    private static final AixLibc LIBC;
    
    @Override
    public String getHostName() {
        final byte[] hostnameBuffer = new byte[256];
        if (0 != AixNetworkParams.LIBC.gethostname(hostnameBuffer, hostnameBuffer.length)) {
            return super.getHostName();
        }
        return Native.toString(hostnameBuffer);
    }
    
    @Override
    public String getIpv4DefaultGateway() {
        return getDefaultGateway("netstat -rnf inet");
    }
    
    @Override
    public String getIpv6DefaultGateway() {
        return getDefaultGateway("netstat -rnf inet6");
    }
    
    private static String getDefaultGateway(final String netstat) {
        for (final String line : ExecutingCommand.runNative(netstat)) {
            final String[] split = ParseUtil.whitespaces.split(line);
            if (split.length > 7 && "default".equals(split[0])) {
                return split[1];
            }
        }
        return "unknown";
    }
    
    static {
        LIBC = AixLibc.INSTANCE;
    }
}
