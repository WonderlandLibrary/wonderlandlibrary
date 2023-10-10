// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.UUID;
import java.util.Random;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;

public final class UuidUtil
{
    private static final long[] EMPTY_LONG_ARRAY;
    public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
    private static final Logger LOGGER;
    private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
    private static final AtomicInteger COUNT;
    private static final long TYPE1 = 4096L;
    private static final byte VARIANT = Byte.MIN_VALUE;
    private static final int SEQUENCE_MASK = 16383;
    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
    private static final long INITIAL_UUID_SEQNO;
    private static final long LOW_MASK = 4294967295L;
    private static final long MID_MASK = 281470681743360L;
    private static final long HIGH_MASK = 1152640029630136320L;
    private static final int NODE_SIZE = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_4 = 32;
    private static final int SHIFT_6 = 48;
    private static final int HUNDRED_NANOS_PER_MILLI = 10000;
    private static final long LEAST;
    
    private UuidUtil() {
    }
    
    static long initialize(byte[] mac) {
        final Random randomGenerator = new SecureRandom();
        if (mac == null || mac.length == 0) {
            mac = new byte[6];
            randomGenerator.nextBytes(mac);
        }
        final int length = (mac.length >= 6) ? 6 : mac.length;
        final int index = (mac.length >= 6) ? (mac.length - 6) : 0;
        final byte[] node = new byte[8];
        node[0] = -128;
        node[1] = 0;
        for (int i = 2; i < 8; ++i) {
            node[i] = 0;
        }
        System.arraycopy(mac, index, node, 2, length);
        final ByteBuffer buf = ByteBuffer.wrap(node);
        long rand = UuidUtil.INITIAL_UUID_SEQNO;
        String assigned = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
        long[] sequences;
        if (assigned == null) {
            sequences = UuidUtil.EMPTY_LONG_ARRAY;
        }
        else {
            final String[] array = assigned.split(Patterns.COMMA_SEPARATOR);
            sequences = new long[array.length];
            int j = 0;
            for (final String value : array) {
                sequences[j] = Long.parseLong(value);
                ++j;
            }
        }
        if (rand == 0L) {
            rand = randomGenerator.nextLong();
        }
        rand &= 0x3FFFL;
        boolean duplicate;
        do {
            duplicate = false;
            for (final long sequence : sequences) {
                if (sequence == rand) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                rand = (rand + 1L & 0x3FFFL);
            }
        } while (duplicate);
        assigned = ((assigned == null) ? Long.toString(rand) : (assigned + ',' + Long.toString(rand)));
        System.setProperty("org.apache.logging.log4j.assignedSequences", assigned);
        return buf.getLong() | rand << 48;
    }
    
    public static UUID getTimeBasedUuid() {
        final long time = System.currentTimeMillis() * 10000L + 122192928000000000L + UuidUtil.COUNT.incrementAndGet() % 10000;
        final long timeLow = (time & 0xFFFFFFFFL) << 32;
        final long timeMid = (time & 0xFFFF00000000L) >> 16;
        final long timeHi = (time & 0xFFF000000000000L) >> 48;
        final long most = timeLow | timeMid | 0x1000L | timeHi;
        return new UUID(most, UuidUtil.LEAST);
    }
    
    static {
        EMPTY_LONG_ARRAY = new long[0];
        LOGGER = StatusLogger.getLogger();
        COUNT = new AtomicInteger(0);
        INITIAL_UUID_SEQNO = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
        LEAST = initialize(NetUtils.getMacAddress());
    }
}
