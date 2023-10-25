/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public final class NetUtils {
    private static final Logger LOGGER = StatusLogger.getLogger();

    private NetUtils() {
    }

    public static String getLocalHostname() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (UnknownHostException uhe) {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface nic = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = nic.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        String hostname;
                        InetAddress address = addresses.nextElement();
                        if (address.isLoopbackAddress() || (hostname = address.getHostName()) == null) continue;
                        return hostname;
                    }
                }
            }
            catch (SocketException se) {
                LOGGER.error("Could not determine local host name", (Throwable)uhe);
                return "UNKNOWN_LOCALHOST";
            }
            LOGGER.error("Could not determine local host name", (Throwable)uhe);
            return "UNKNOWN_LOCALHOST";
        }
    }
}

