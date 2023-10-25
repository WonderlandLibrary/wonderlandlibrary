package org.apache.logging.log4j.core.util;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class UuidUtil {
  private static final long[] EMPTY_LONG_ARRAY = new long[0];
  
  public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
  
  private static final AtomicInteger COUNT = new AtomicInteger(0);
  
  private static final long TYPE1 = 4096L;
  
  private static final byte VARIANT = -128;
  
  private static final int SEQUENCE_MASK = 16383;
  
  private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
  
  private static final long INITIAL_UUID_SEQNO = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
  
  private static final long LOW_MASK = 4294967295L;
  
  private static final long MID_MASK = 281470681743360L;
  
  private static final long HIGH_MASK = 1152640029630136320L;
  
  private static final int NODE_SIZE = 8;
  
  private static final int SHIFT_2 = 16;
  
  private static final int SHIFT_4 = 32;
  
  private static final int SHIFT_6 = 48;
  
  private static final int HUNDRED_NANOS_PER_MILLI = 10000;
  
  private static final long LEAST = initialize(NetUtils.getMacAddress());
  
  static long initialize(byte[] mac) {
    long[] sequences;
    Random randomGenerator = new SecureRandom();
    if (mac == null || mac.length == 0) {
      mac = new byte[6];
      randomGenerator.nextBytes(mac);
    } 
    int length = (mac.length >= 6) ? 6 : mac.length;
    int index = (mac.length >= 6) ? (mac.length - 6) : 0;
    byte[] node = new byte[8];
    node[0] = Byte.MIN_VALUE;
    node[1] = 0;
    for (int i = 2; i < 8; i++)
      node[i] = 0; 
    System.arraycopy(mac, index, node, 2, length);
    ByteBuffer buf = ByteBuffer.wrap(node);
    long rand = INITIAL_UUID_SEQNO;
    String assigned = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
    if (assigned == null) {
      sequences = EMPTY_LONG_ARRAY;
    } else {
      String[] array = assigned.split(Patterns.COMMA_SEPARATOR);
      sequences = new long[array.length];
      int j = 0;
      for (String value : array) {
        sequences[j] = Long.parseLong(value);
        j++;
      } 
    } 
    if (rand == 0L)
      rand = randomGenerator.nextLong(); 
    rand &= 0x3FFFL;
    while (true) {
      boolean duplicate = false;
      for (long sequence : sequences) {
        if (sequence == rand) {
          duplicate = true;
          break;
        } 
      } 
      if (duplicate)
        rand = rand + 1L & 0x3FFFL; 
      if (!duplicate) {
        assigned = (assigned == null) ? Long.toString(rand) : (assigned + ',' + Long.toString(rand));
        System.setProperty("org.apache.logging.log4j.assignedSequences", assigned);
        return buf.getLong() | rand << 48L;
      } 
    } 
  }
  
  public static UUID getTimeBasedUuid() {
    long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (COUNT.incrementAndGet() % 10000);
    long timeLow = (time & 0xFFFFFFFFL) << 32L;
    long timeMid = (time & 0xFFFF00000000L) >> 16L;
    long timeHi = (time & 0xFFF000000000000L) >> 48L;
    long most = timeLow | timeMid | 0x1000L | timeHi;
    return new UUID(most, LEAST);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\UuidUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */