/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class UUIDUtil {
    public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
    private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
    private static AtomicInteger count;
    private static final long TYPE1 = 4096L;
    private static final byte VARIANT = -128;
    private static final int SEQUENCE_MASK = 16383;
    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
    private static long uuidSequence;
    private static long least;
    private static final long LOW_MASK = 0xFFFFFFFFL;
    private static final long MID_MASK = 0xFFFF00000000L;
    private static final long HIGH_MASK = 0xFFF000000000000L;
    private static final int NODE_SIZE = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_4 = 32;
    private static final int SHIFT_6 = 48;
    private static final int HUNDRED_NANOS_PER_MILLI = 10000;

    private UUIDUtil() {
    }

    public static UUID getTimeBasedUUID() {
        long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (long)(count.incrementAndGet() % 10000);
        long timeLow = (time & 0xFFFFFFFFL) << 32;
        long timeMid = (time & 0xFFFF00000000L) >> 16;
        long timeHi = (time & 0xFFF000000000000L) >> 48;
        long most = timeLow | timeMid | 0x1000L | timeHi;
        return new UUID(most, least);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static {
        Runtime runtime;
        count = new AtomicInteger(0);
        uuidSequence = PropertiesUtil.getProperties().getLongProperty(UUID_SEQUENCE, 0L);
        byte[] mac = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            try {
                Method method;
                NetworkInterface ni = NetworkInterface.getByInetAddress(address);
                if (ni != null && !ni.isLoopback() && ni.isUp() && (method = ni.getClass().getMethod("getHardwareAddress", new Class[0])) != null) {
                    mac = (byte[])method.invoke(ni, new Object[0]);
                }
                if (mac == null) {
                    Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                    while (enumeration.hasMoreElements() && mac == null) {
                        Method method2;
                        ni = enumeration.nextElement();
                        if (ni == null || !ni.isUp() || ni.isLoopback() || (method2 = ni.getClass().getMethod("getHardwareAddress", new Class[0])) == null) continue;
                        mac = (byte[])method2.invoke(ni, new Object[0]);
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (mac == null || mac.length == 0) {
                mac = address.getAddress();
            }
        }
        catch (UnknownHostException e) {
            // empty catch block
        }
        SecureRandom randomGenerator = new SecureRandom();
        if (mac == null || mac.length == 0) {
            mac = new byte[6];
            ((Random)randomGenerator).nextBytes(mac);
        }
        int length = mac.length >= 6 ? 6 : mac.length;
        int index = mac.length >= 6 ? mac.length - 6 : 0;
        byte[] node = new byte[8];
        node[0] = -128;
        node[1] = 0;
        for (int i = 2; i < 8; ++i) {
            node[i] = 0;
        }
        System.arraycopy(mac, index, node, index + 2, length);
        ByteBuffer buf = ByteBuffer.wrap(node);
        long rand = uuidSequence;
        Runtime runtime2 = runtime = Runtime.getRuntime();
        synchronized (runtime2) {
            boolean duplicate;
            long[] sequences;
            String assigned = PropertiesUtil.getProperties().getStringProperty(ASSIGNED_SEQUENCES);
            if (assigned == null) {
                sequences = new long[]{};
            } else {
                String[] array = assigned.split(",");
                sequences = new long[array.length];
                int i = 0;
                for (String value : array) {
                    sequences[i] = Long.parseLong(value);
                    ++i;
                }
            }
            if (rand == 0L) {
                rand = randomGenerator.nextLong();
            }
            rand &= 0x3FFFL;
            do {
                duplicate = false;
                for (long sequence : sequences) {
                    if (sequence != rand) continue;
                    duplicate = true;
                    break;
                }
                if (!duplicate) continue;
                rand = rand + 1L & 0x3FFFL;
            } while (duplicate);
            assigned = assigned == null ? Long.toString(rand) : assigned + "," + Long.toString(rand);
            System.setProperty(ASSIGNED_SEQUENCES, assigned);
        }
        least = buf.getLong() | rand << 48;
    }
}

