/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class UuidUtil {
    public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
    private static final AtomicInteger COUNT = new AtomicInteger(0);
    private static final long TYPE1 = 4096L;
    private static final byte VARIANT = -128;
    private static final int SEQUENCE_MASK = 16383;
    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
    private static final long INITIAL_UUID_SEQNO = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
    private static final long LOW_MASK = 0xFFFFFFFFL;
    private static final long MID_MASK = 0xFFFF00000000L;
    private static final long HIGH_MASK = 0xFFF000000000000L;
    private static final int NODE_SIZE = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_4 = 32;
    private static final int SHIFT_6 = 48;
    private static final int HUNDRED_NANOS_PER_MILLI = 10000;
    private static final long LEAST = UuidUtil.initialize(NetUtils.getMacAddress());

    private UuidUtil() {
    }

    static long initialize(byte[] mac) {
        boolean duplicate;
        long[] sequences;
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
        System.arraycopy(mac, index, node, 2, length);
        ByteBuffer buf = ByteBuffer.wrap(node);
        long rand = INITIAL_UUID_SEQNO;
        String assigned = PropertiesUtil.getProperties().getStringProperty(ASSIGNED_SEQUENCES);
        if (assigned == null) {
            sequences = new long[]{};
        } else {
            String[] array = assigned.split(Patterns.COMMA_SEPARATOR);
            sequences = new long[array.length];
            int i = 0;
            String[] arrstring = array;
            int n = arrstring.length;
            for (int j = 0; j < n; ++j) {
                String value = arrstring[j];
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
        assigned = assigned == null ? Long.toString(rand) : assigned + ',' + Long.toString(rand);
        System.setProperty(ASSIGNED_SEQUENCES, assigned);
        return buf.getLong() | rand << 48;
    }

    public static UUID getTimeBasedUuid() {
        long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (long)(COUNT.incrementAndGet() % 10000);
        long timeLow = (time & 0xFFFFFFFFL) << 32;
        long timeMid = (time & 0xFFFF00000000L) >> 16;
        long timeHi = (time & 0xFFF000000000000L) >> 48;
        long most = timeLow | timeMid | 0x1000L | timeHi;
        return new UUID(most, LEAST);
    }
}

