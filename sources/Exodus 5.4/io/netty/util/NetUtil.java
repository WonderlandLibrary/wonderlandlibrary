/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public final class NetUtil {
    public static final InetAddress LOCALHOST;
    public static final NetworkInterface LOOPBACK_IF;
    public static final Inet4Address LOCALHOST4;
    public static final Inet6Address LOCALHOST6;
    public static final int SOMAXCONN;
    private static final InternalLogger logger;

    static int getIntValue(char c) {
        switch (c) {
            case '0': {
                return 0;
            }
            case '1': {
                return 1;
            }
            case '2': {
                return 2;
            }
            case '3': {
                return 3;
            }
            case '4': {
                return 4;
            }
            case '5': {
                return 5;
            }
            case '6': {
                return 6;
            }
            case '7': {
                return 7;
            }
            case '8': {
                return 8;
            }
            case '9': {
                return 9;
            }
        }
        c = Character.toLowerCase(c);
        switch (c) {
            case 'a': {
                return 10;
            }
            case 'b': {
                return 11;
            }
            case 'c': {
                return 12;
            }
            case 'd': {
                return 13;
            }
            case 'e': {
                return 14;
            }
            case 'f': {
                return 15;
            }
        }
        return 0;
    }

    private NetUtil() {
    }

    private static void convertToBytes(String string, byte[] byArray, int n) {
        int n2;
        int n3 = string.length();
        int n4 = 0;
        byArray[n] = 0;
        byArray[n + 1] = 0;
        if (n3 > 3) {
            n2 = NetUtil.getIntValue(string.charAt(n4++));
            int n5 = n;
            byArray[n5] = (byte)(byArray[n5] | n2 << 4);
        }
        if (n3 > 2) {
            n2 = NetUtil.getIntValue(string.charAt(n4++));
            int n6 = n;
            byArray[n6] = (byte)(byArray[n6] | n2);
        }
        if (n3 > 1) {
            n2 = NetUtil.getIntValue(string.charAt(n4++));
            int n7 = n + 1;
            byArray[n7] = (byte)(byArray[n7] | n2 << 4);
        }
        n2 = NetUtil.getIntValue(string.charAt(n4));
        int n8 = n + 1;
        byArray[n8] = (byte)(byArray[n8] | n2 & 0xF);
    }

    public static boolean isValidIp4Word(String string) {
        if (string.length() < 1 || string.length() > 3) {
            return false;
        }
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c >= '0' && c <= '9') continue;
            return false;
        }
        return Integer.parseInt(string) <= 255;
    }

    public static boolean isValidIpV4Address(String string) {
        int n = 0;
        int n2 = string.length();
        if (n2 > 15) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n2; ++i) {
            char c = string.charAt(i);
            if (c == '.') {
                if (++n > 3) {
                    return false;
                }
                if (stringBuilder.length() == 0) {
                    return false;
                }
                if (Integer.parseInt(stringBuilder.toString()) > 255) {
                    return false;
                }
                stringBuilder.delete(0, stringBuilder.length());
                continue;
            }
            if (!Character.isDigit(c)) {
                return false;
            }
            if (stringBuilder.length() > 2) {
                return false;
            }
            stringBuilder.append(c);
        }
        if (stringBuilder.length() == 0 || Integer.parseInt(stringBuilder.toString()) > 255) {
            return false;
        }
        return n == 3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static {
        int n;
        block34: {
            Object object;
            Object object2;
            Object object3;
            block33: {
                logger = InternalLoggerFactory.getInstance(NetUtil.class);
                byte[] byArray = new byte[]{127, 0, 0, 1};
                byte[] byArray2 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
                Inet4Address inet4Address = null;
                try {
                    inet4Address = (Inet4Address)InetAddress.getByAddress(byArray);
                }
                catch (Exception exception) {
                    PlatformDependent.throwException(exception);
                }
                LOCALHOST4 = inet4Address;
                Inet6Address inet6Address = null;
                try {
                    inet6Address = (Inet6Address)InetAddress.getByAddress(byArray2);
                }
                catch (Exception exception) {
                    PlatformDependent.throwException(exception);
                }
                LOCALHOST6 = inet6Address;
                ArrayList<NetworkInterface> arrayList = new ArrayList<NetworkInterface>();
                try {
                    object3 = NetworkInterface.getNetworkInterfaces();
                    while (object3.hasMoreElements()) {
                        object2 = object3.nextElement();
                        if (!((NetworkInterface)object2).getInetAddresses().hasMoreElements()) continue;
                        arrayList.add((NetworkInterface)object2);
                    }
                }
                catch (SocketException socketException) {
                    logger.warn("Failed to retrieve the list of available network interfaces", socketException);
                }
                object3 = null;
                object2 = null;
                block18: for (NetworkInterface networkInterface : arrayList) {
                    object = networkInterface.getInetAddresses();
                    while (object.hasMoreElements()) {
                        InetAddress inetAddress = object.nextElement();
                        if (!inetAddress.isLoopbackAddress()) continue;
                        object3 = networkInterface;
                        object2 = inetAddress;
                        break block18;
                    }
                }
                if (object3 == null) {
                    try {
                        for (NetworkInterface networkInterface : arrayList) {
                            if (!networkInterface.isLoopback() || !(object = networkInterface.getInetAddresses()).hasMoreElements()) continue;
                            object3 = networkInterface;
                            object2 = object.nextElement();
                            break;
                        }
                        if (object3 == null) {
                            logger.warn("Failed to find the loopback interface");
                        }
                    }
                    catch (SocketException socketException) {
                        logger.warn("Failed to find the loopback interface", socketException);
                    }
                }
                if (object3 != null) {
                    logger.debug("Loopback interface: {} ({}, {})", ((NetworkInterface)object3).getName(), ((NetworkInterface)object3).getDisplayName(), ((InetAddress)object2).getHostAddress());
                } else if (object2 == null) {
                    try {
                        if (NetworkInterface.getByInetAddress(LOCALHOST6) != null) {
                            logger.debug("Using hard-coded IPv6 localhost address: {}", (Object)inet6Address);
                            object2 = inet6Address;
                        }
                        if (object2 == null) {
                            logger.debug("Using hard-coded IPv4 localhost address: {}", (Object)inet4Address);
                            object2 = inet4Address;
                        }
                    }
                    catch (Exception exception) {
                        if (object2 != null) break block33;
                        logger.debug("Using hard-coded IPv4 localhost address: {}", (Object)inet4Address);
                        object2 = inet4Address;
                    }
                }
            }
            LOOPBACK_IF = object3;
            LOCALHOST = object2;
            n = PlatformDependent.isWindows() ? 200 : 128;
            File file = new File("/proc/sys/net/core/somaxconn");
            if (file.exists()) {
                object = null;
                try {
                    object = new BufferedReader(new FileReader(file));
                    n = Integer.parseInt(((BufferedReader)object).readLine());
                    if (logger.isDebugEnabled()) {
                        logger.debug("{}: {}", (Object)file, (Object)n);
                    }
                    if (object == null) break block34;
                }
                catch (Exception exception) {
                    logger.debug("Failed to get SOMAXCONN from: {}", (Object)file, (Object)exception);
                    if (object != null) {
                        try {
                            ((BufferedReader)object).close();
                        }
                        catch (Exception exception2) {}
                    }
                    break block34;
                }
                try {
                    ((BufferedReader)object).close();
                }
                catch (Exception exception) {}
            } else if (logger.isDebugEnabled()) {
                logger.debug("{}: {} (non-existent)", (Object)file, (Object)n);
            }
        }
        SOMAXCONN = n;
    }

    public static byte[] createByteArrayFromIpAddressString(String string) {
        if (NetUtil.isValidIpV4Address(string)) {
            StringTokenizer stringTokenizer = new StringTokenizer(string, ".");
            byte[] byArray = new byte[4];
            for (int i = 0; i < 4; ++i) {
                String string2 = stringTokenizer.nextToken();
                int n = Integer.parseInt(string2);
                byArray[i] = (byte)n;
            }
            return byArray;
        }
        if (NetUtil.isValidIpV6Address(string)) {
            int n;
            if (string.charAt(0) == '[') {
                string = string.substring(1, string.length() - 1);
            }
            StringTokenizer stringTokenizer = new StringTokenizer(string, ":.", true);
            ArrayList<String> arrayList = new ArrayList<String>();
            ArrayList<String> arrayList2 = new ArrayList<String>();
            String string3 = "";
            String string4 = "";
            int n2 = -1;
            while (stringTokenizer.hasMoreTokens()) {
                string4 = string3;
                string3 = stringTokenizer.nextToken();
                if (":".equals(string3)) {
                    if (":".equals(string4)) {
                        n2 = arrayList.size();
                        continue;
                    }
                    if (string4.isEmpty()) continue;
                    arrayList.add(string4);
                    continue;
                }
                if (!".".equals(string3)) continue;
                arrayList2.add(string4);
            }
            if (":".equals(string4)) {
                if (":".equals(string3)) {
                    n2 = arrayList.size();
                } else {
                    arrayList.add(string3);
                }
            } else if (".".equals(string4)) {
                arrayList2.add(string3);
            }
            int n3 = 8;
            if (!arrayList2.isEmpty()) {
                n3 -= 2;
            }
            if (n2 != -1) {
                int n4 = n3 - arrayList.size();
                for (n = 0; n < n4; ++n) {
                    arrayList.add(n2, "0");
                }
            }
            byte[] byArray = new byte[16];
            for (n = 0; n < arrayList.size(); ++n) {
                NetUtil.convertToBytes((String)arrayList.get(n), byArray, n * 2);
            }
            for (n = 0; n < arrayList2.size(); ++n) {
                byArray[n + 12] = (byte)(Integer.parseInt((String)arrayList2.get(n)) & 0xFF);
            }
            return byArray;
        }
        return null;
    }

    public static boolean isValidIpV6Address(String string) {
        int n = string.length();
        boolean bl = false;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        StringBuilder stringBuilder = new StringBuilder();
        char c = '\u0000';
        int n5 = 0;
        if (n < 2) {
            return false;
        }
        block9: for (int i = 0; i < n; ++i) {
            char c2 = c;
            c = string.charAt(i);
            switch (c) {
                case '[': {
                    if (i != 0) {
                        return false;
                    }
                    if (string.charAt(n - 1) != ']') {
                        return false;
                    }
                    n5 = 1;
                    if (n >= 4) continue block9;
                    return false;
                }
                case ']': {
                    if (i != n - 1) {
                        return false;
                    }
                    if (string.charAt(0) == '[') continue block9;
                    return false;
                }
                case '.': {
                    if (++n3 > 3) {
                        return false;
                    }
                    if (!NetUtil.isValidIp4Word(stringBuilder.toString())) {
                        return false;
                    }
                    if (n2 != 6 && !bl) {
                        return false;
                    }
                    if (n2 == 7 && string.charAt(n5) != ':' && string.charAt(1 + n5) != ':') {
                        return false;
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    continue block9;
                }
                case ':': {
                    if (i == n5 && (string.length() <= i || string.charAt(i + 1) != ':')) {
                        return false;
                    }
                    if (++n2 > 7) {
                        return false;
                    }
                    if (n3 > 0) {
                        return false;
                    }
                    if (c2 == ':') {
                        if (bl) {
                            return false;
                        }
                        bl = true;
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    continue block9;
                }
                case '%': {
                    if (n2 == 0) {
                        return false;
                    }
                    ++n4;
                    if (i + 1 >= n) {
                        return false;
                    }
                    try {
                        if (Integer.parseInt(string.substring(i + 1)) >= 0) continue block9;
                        return false;
                    }
                    catch (NumberFormatException numberFormatException) {
                        return false;
                    }
                }
                default: {
                    if (n4 == 0) {
                        if (stringBuilder != null && stringBuilder.length() > 3) {
                            return false;
                        }
                        if (!NetUtil.isValidHexChar(c)) {
                            return false;
                        }
                    }
                    stringBuilder.append(c);
                }
            }
        }
        if (n3 > 0) {
            if (n3 != 3 || !NetUtil.isValidIp4Word(stringBuilder.toString()) || n2 >= 7) {
                return false;
            }
        } else {
            if (n2 != 7 && !bl) {
                return false;
            }
            if (n4 == 0 && stringBuilder.length() == 0 && string.charAt(n - 1 - n5) == ':' && string.charAt(n - 2 - n5) != ':') {
                return false;
            }
        }
        return true;
    }

    static boolean isValidHexChar(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f';
    }
}

