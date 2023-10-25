package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadLocalRandom extends Random {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
  
  private static final AtomicLong seedUniquifier = new AtomicLong();
  
  private static volatile long initialSeedUniquifier;
  
  private static final long multiplier = 25214903917L;
  
  private static final long addend = 11L;
  
  private static final long mask = 281474976710655L;
  
  private long rnd;
  
  boolean initialized;
  
  private long pad0;
  
  private long pad1;
  
  private long pad2;
  
  private long pad3;
  
  private long pad4;
  
  private long pad5;
  
  private long pad6;
  
  private long pad7;
  
  private static final long serialVersionUID = -5851777807851030925L;
  
  public static void setInitialSeedUniquifier(long initialSeedUniquifier) {
    ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;
  }
  
  public static synchronized long getInitialSeedUniquifier() {
    long initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
    if (initialSeedUniquifier == 0L)
      ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L); 
    if (initialSeedUniquifier == 0L) {
      final BlockingQueue<byte[]> queue = (BlockingQueue)new LinkedBlockingQueue<byte>();
      Thread generatorThread = new Thread("initialSeedUniquifierGenerator") {
          public void run() {
            SecureRandom random = new SecureRandom();
            queue.add(random.generateSeed(8));
          }
        };
      generatorThread.setDaemon(true);
      generatorThread.start();
      generatorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
              ThreadLocalRandom.logger.debug("An exception has been raised by {}", t.getName(), e);
            }
          });
      long timeoutSeconds = 3L;
      long deadLine = System.nanoTime() + TimeUnit.SECONDS.toNanos(3L);
      boolean interrupted = false;
      while (true) {
        long waitTime = deadLine - System.nanoTime();
        if (waitTime <= 0L) {
          generatorThread.interrupt();
          logger.warn("Failed to generate a seed from SecureRandom within {} seconds. Not enough entrophy?", Long.valueOf(3L));
          break;
        } 
        try {
          byte[] seed = queue.poll(waitTime, TimeUnit.NANOSECONDS);
          if (seed != null) {
            initialSeedUniquifier = (seed[0] & 0xFFL) << 56L | (seed[1] & 0xFFL) << 48L | (seed[2] & 0xFFL) << 40L | (seed[3] & 0xFFL) << 32L | (seed[4] & 0xFFL) << 24L | (seed[5] & 0xFFL) << 16L | (seed[6] & 0xFFL) << 8L | seed[7] & 0xFFL;
            break;
          } 
        } catch (InterruptedException e) {
          interrupted = true;
          logger.warn("Failed to generate a seed from SecureRandom due to an InterruptedException.");
          break;
        } 
      } 
      initialSeedUniquifier ^= 0x3255ECDC33BAE119L;
      initialSeedUniquifier ^= Long.reverse(System.nanoTime());
      ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;
      if (interrupted) {
        Thread.currentThread().interrupt();
        generatorThread.interrupt();
      } 
    } 
    return initialSeedUniquifier;
  }
  
  private static long newSeed() {
    long startTime = System.nanoTime();
    while (true) {
      long current = seedUniquifier.get();
      long actualCurrent = (current != 0L) ? current : getInitialSeedUniquifier();
      long next = actualCurrent * 181783497276652981L;
      if (seedUniquifier.compareAndSet(current, next)) {
        if (current == 0L && logger.isDebugEnabled())
          logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", new Object[] { Long.valueOf(actualCurrent), Long.valueOf(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)) })); 
        return next ^ System.nanoTime();
      } 
    } 
  }
  
  ThreadLocalRandom() {
    super(newSeed());
    this.initialized = true;
  }
  
  public static ThreadLocalRandom current() {
    return InternalThreadLocalMap.get().random();
  }
  
  public void setSeed(long seed) {
    if (this.initialized)
      throw new UnsupportedOperationException(); 
    this.rnd = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
  }
  
  protected int next(int bits) {
    this.rnd = this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
    return (int)(this.rnd >>> 48 - bits);
  }
  
  public int nextInt(int least, int bound) {
    if (least >= bound)
      throw new IllegalArgumentException(); 
    return nextInt(bound - least) + least;
  }
  
  public long nextLong(long n) {
    if (n <= 0L)
      throw new IllegalArgumentException("n must be positive"); 
    long offset = 0L;
    while (n >= 2147483647L) {
      int bits = next(2);
      long half = n >>> 1L;
      long nextn = ((bits & 0x2) == 0) ? half : (n - half);
      if ((bits & 0x1) == 0)
        offset += n - nextn; 
      n = nextn;
    } 
    return offset + nextInt((int)n);
  }
  
  public long nextLong(long least, long bound) {
    if (least >= bound)
      throw new IllegalArgumentException(); 
    return nextLong(bound - least) + least;
  }
  
  public double nextDouble(double n) {
    if (n <= 0.0D)
      throw new IllegalArgumentException("n must be positive"); 
    return nextDouble() * n;
  }
  
  public double nextDouble(double least, double bound) {
    if (least >= bound)
      throw new IllegalArgumentException(); 
    return nextDouble() * (bound - least) + least;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\ThreadLocalRandom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */