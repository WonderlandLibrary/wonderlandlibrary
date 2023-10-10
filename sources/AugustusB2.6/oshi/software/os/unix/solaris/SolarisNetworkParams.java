// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.solaris;

import oshi.util.ExecutingCommand;
import com.sun.jna.Native;
import oshi.jna.platform.unix.SolarisLibc;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractNetworkParams;

@ThreadSafe
final class SolarisNetworkParams extends AbstractNetworkParams
{
    private static final SolarisLibc LIBC;
    
    @Override
    public String getHostName() {
        final byte[] hostnameBuffer = new byte[256];
        if (0 != SolarisNetworkParams.LIBC.gethostname(hostnameBuffer, hostnameBuffer.length)) {
            return super.getHostName();
        }
        return Native.toString(hostnameBuffer);
    }
    
    @Override
    public String getIpv4DefaultGateway() {
        return AbstractNetworkParams.searchGateway(ExecutingCommand.runNative("route get -inet default"));
    }
    
    @Override
    public String getIpv6DefaultGateway() {
        return AbstractNetworkParams.searchGateway(ExecutingCommand.runNative("route get -inet6 default"));
    }
    
    static {
        LIBC = SolarisLibc.INSTANCE;
    }
}
