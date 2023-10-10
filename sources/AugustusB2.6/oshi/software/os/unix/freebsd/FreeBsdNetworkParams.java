// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.freebsd;

import org.slf4j.LoggerFactory;
import oshi.util.ExecutingCommand;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import oshi.jna.platform.unix.CLibrary;
import oshi.jna.platform.unix.FreeBsdLibc;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractNetworkParams;

@ThreadSafe
final class FreeBsdNetworkParams extends AbstractNetworkParams
{
    private static final Logger LOG;
    private static final FreeBsdLibc LIBC;
    
    @Override
    public String getDomainName() {
        final CLibrary.Addrinfo hint = new CLibrary.Addrinfo();
        hint.ai_flags = 2;
        final String hostname = this.getHostName();
        final PointerByReference ptr = new PointerByReference();
        final int res = FreeBsdNetworkParams.LIBC.getaddrinfo(hostname, null, hint, ptr);
        if (res > 0) {
            if (FreeBsdNetworkParams.LOG.isErrorEnabled()) {
                FreeBsdNetworkParams.LOG.warn("Failed getaddrinfo(): {}", FreeBsdNetworkParams.LIBC.gai_strerror(res));
            }
            return "";
        }
        final CLibrary.Addrinfo info = new CLibrary.Addrinfo(ptr.getValue());
        final String canonname = info.ai_canonname.trim();
        FreeBsdNetworkParams.LIBC.freeaddrinfo(ptr.getValue());
        return canonname;
    }
    
    @Override
    public String getHostName() {
        final byte[] hostnameBuffer = new byte[256];
        if (0 != FreeBsdNetworkParams.LIBC.gethostname(hostnameBuffer, hostnameBuffer.length)) {
            return super.getHostName();
        }
        return Native.toString(hostnameBuffer);
    }
    
    @Override
    public String getIpv4DefaultGateway() {
        return AbstractNetworkParams.searchGateway(ExecutingCommand.runNative("route -4 get default"));
    }
    
    @Override
    public String getIpv6DefaultGateway() {
        return AbstractNetworkParams.searchGateway(ExecutingCommand.runNative("route -6 get default"));
    }
    
    static {
        LOG = LoggerFactory.getLogger(FreeBsdNetworkParams.class);
        LIBC = FreeBsdLibc.INSTANCE;
    }
}
