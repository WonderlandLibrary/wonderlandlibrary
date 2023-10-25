/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.ArrayUtils;
import org.apache.logging.log4j.status.StatusLogger;

public final class NetUtils {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String UNKNOWN_LOCALHOST = "UNKNOWN_LOCALHOST";

    private NetUtils() {
    }

    public static String getLocalHostname() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr == null ? UNKNOWN_LOCALHOST : addr.getHostName();
        }
        catch (UnknownHostException uhe) {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                if (interfaces != null) {
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
            }
            catch (SocketException socketException) {
                // empty catch block
            }
            LOGGER.error("Could not determine local host name", (Throwable)uhe);
            return UNKNOWN_LOCALHOST;
        }
    }

    public static byte[] getMacAddress() {
        byte[] mac = null;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            try {
                Enumeration<NetworkInterface> networkInterfaces;
                NetworkInterface localInterface = NetworkInterface.getByInetAddress(localHost);
                if (NetUtils.isUpAndNotLoopback(localInterface)) {
                    mac = localInterface.getHardwareAddress();
                }
                if (mac == null && (networkInterfaces = NetworkInterface.getNetworkInterfaces()) != null) {
                    while (networkInterfaces.hasMoreElements() && mac == null) {
                        NetworkInterface nic = networkInterfaces.nextElement();
                        if (!NetUtils.isUpAndNotLoopback(nic)) continue;
                        mac = nic.getHardwareAddress();
                    }
                }
            }
            catch (SocketException e) {
                LOGGER.catching(e);
            }
            if (ArrayUtils.isEmpty(mac) && localHost != null) {
                byte[] address = localHost.getAddress();
                mac = Arrays.copyOf(address, 6);
            }
        }
        catch (UnknownHostException unknownHostException) {
            // empty catch block
        }
        return mac;
    }

    public static String getMacAddressString() {
        byte[] macAddr = NetUtils.getMacAddress();
        if (!ArrayUtils.isEmpty(macAddr)) {
            StringBuilder sb = new StringBuilder(String.format("%02x", macAddr[0]));
            for (int i = 1; i < macAddr.length; ++i) {
                sb.append(":").append(String.format("%02x", macAddr[i]));
            }
            return sb.toString();
        }
        return null;
    }

    private static boolean isUpAndNotLoopback(NetworkInterface ni) throws SocketException {
        return ni != null && !ni.isLoopback() && ni.isUp();
    }

    public static URI toURI(String path) {
        try {
            return new URI(path);
        }
        catch (URISyntaxException e) {
            try {
                URL url = new URL(path);
                return new URI(url.getProtocol(), url.getHost(), url.getPath(), null);
            }
            catch (MalformedURLException | URISyntaxException nestedEx) {
                return new File(path).toURI();
            }
        }
    }
}

