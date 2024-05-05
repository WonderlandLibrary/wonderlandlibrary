// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.hwid;

import java.util.Enumeration;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import ru.fluger.client.helpers.encrypt.SHAUtil;
import java.util.Base64;

public class HwidUtil
{
    public String getHwid() {
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append(System.getProperty("os.name"));
            builder.append(new String(Base64.getEncoder().encode(this.getLocalNetWork().getHardwareAddress())));
            return SHAUtil.SHA1(builder.toString());
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private NetworkInterface getLocalNetWork() throws Exception {
        NetworkInterface ntwinterface = null;
        final Enumeration<NetworkInterface> NETWORK = NetworkInterface.getNetworkInterfaces();
        while (NETWORK.hasMoreElements()) {
            final NetworkInterface element = NETWORK.nextElement();
            final Enumeration<InetAddress> addresses = element.getInetAddresses();
            while (addresses.hasMoreElements() && element.getHardwareAddress() != null && !this.isVMMac(element.getHardwareAddress())) {
                final InetAddress ip = addresses.nextElement();
                if (ip instanceof Inet4Address && ip.isSiteLocalAddress()) {
                    ntwinterface = NetworkInterface.getByInetAddress(ip);
                }
            }
        }
        return ntwinterface;
    }
    
    private boolean isVMMac(final byte[] mac) {
        if (null == mac) {
            return false;
        }
        final byte[][] array;
        final byte[][] invalidMacs = array = new byte[][] { { 0, 5, 105 }, { 0, 28, 20 }, { 0, 12, 41 }, { 0, 80, 86 }, { 8, 0, 39 }, { 10, 0, 39 }, { 0, 3, -1 }, { 0, 21, 93 } };
        for (final byte[] invalid : array) {
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) {
                return true;
            }
        }
        return false;
    }
}
