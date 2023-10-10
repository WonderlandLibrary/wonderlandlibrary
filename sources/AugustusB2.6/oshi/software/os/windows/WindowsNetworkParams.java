// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import org.slf4j.LoggerFactory;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import com.sun.jna.platform.win32.Kernel32Util;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.IPHlpAPI;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractNetworkParams;

@ThreadSafe
final class WindowsNetworkParams extends AbstractNetworkParams
{
    private static final Logger LOG;
    private static final int COMPUTER_NAME_DNS_DOMAIN_FULLY_QUALIFIED = 3;
    
    @Override
    public String getDomainName() {
        final char[] buffer = new char[256];
        final IntByReference bufferSize = new IntByReference(buffer.length);
        if (!Kernel32.INSTANCE.GetComputerNameEx(3, buffer, bufferSize)) {
            WindowsNetworkParams.LOG.error("Failed to get dns domain name. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return "";
        }
        return Native.toString(buffer);
    }
    
    @Override
    public String[] getDnsServers() {
        final IntByReference bufferSize = new IntByReference();
        int ret = IPHlpAPI.INSTANCE.GetNetworkParams(null, bufferSize);
        if (ret != 111) {
            WindowsNetworkParams.LOG.error("Failed to get network parameters buffer size. Error code: {}", (Object)ret);
            return new String[0];
        }
        final Memory buffer = new Memory(bufferSize.getValue());
        ret = IPHlpAPI.INSTANCE.GetNetworkParams(buffer, bufferSize);
        if (ret != 0) {
            WindowsNetworkParams.LOG.error("Failed to get network parameters. Error code: {}", (Object)ret);
            return new String[0];
        }
        final IPHlpAPI.FIXED_INFO fixedInfo = new IPHlpAPI.FIXED_INFO(buffer);
        final List<String> list = new ArrayList<String>();
        for (IPHlpAPI.IP_ADDR_STRING dns = fixedInfo.DnsServerList; dns != null; dns = dns.Next) {
            String addr = Native.toString(dns.IpAddress.String, StandardCharsets.US_ASCII);
            final int nullPos = addr.indexOf(0);
            if (nullPos != -1) {
                addr = addr.substring(0, nullPos);
            }
            list.add(addr);
        }
        return list.toArray(new String[0]);
    }
    
    @Override
    public String getHostName() {
        return Kernel32Util.getComputerName();
    }
    
    @Override
    public String getIpv4DefaultGateway() {
        return parseIpv4Route();
    }
    
    @Override
    public String getIpv6DefaultGateway() {
        return parseIpv6Route();
    }
    
    private static String parseIpv4Route() {
        final List<String> lines = ExecutingCommand.runNative("route print -4 0.0.0.0");
        for (final String line : lines) {
            final String[] fields = ParseUtil.whitespaces.split(line.trim());
            if (fields.length > 2 && "0.0.0.0".equals(fields[0])) {
                return fields[2];
            }
        }
        return "";
    }
    
    private static String parseIpv6Route() {
        final List<String> lines = ExecutingCommand.runNative("route print -6 ::/0");
        for (final String line : lines) {
            final String[] fields = ParseUtil.whitespaces.split(line.trim());
            if (fields.length > 3 && "::/0".equals(fields[2])) {
                return fields[3];
            }
        }
        return "";
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsNetworkParams.class);
    }
}
