// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.openbsd;

import oshi.util.ExecutingCommand;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractNetworkParams;

@ThreadSafe
public class OpenBsdNetworkParams extends AbstractNetworkParams
{
    @Override
    public String getIpv4DefaultGateway() {
        return AbstractNetworkParams.searchGateway(ExecutingCommand.runNative("route -n get default"));
    }
    
    @Override
    public String getIpv6DefaultGateway() {
        return AbstractNetworkParams.searchGateway(ExecutingCommand.runNative("route -n get default"));
    }
}
