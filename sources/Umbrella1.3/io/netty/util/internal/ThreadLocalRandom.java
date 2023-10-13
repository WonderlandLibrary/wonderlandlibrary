/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util.internal;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadLocalRandom
extends Random {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
    private static final AtomicLong seedUniquifier = new AtomicLong();
    private static volatile long initialSeedUniquifier;
    private static final long multiplier = 25214903917L;
    private static final long addend = 11L;
    private static final long mask = 0xFFFFFFFFFFFFL;
    private long rnd;
    boolean initialized = true;
    private long pad0;
    private long pad1;
    private long pad2;
    private long pad3;
    private long pad4;
    private long pad5;
    private long pad6;
    private long pad7;
    private static final ThreadLocal<ThreadLocalRandom> localRandom;
    private static final long serialVersionUID = -5851777807851030925L;

    public static void setInitialSeedUniquifier(long initialSeedUniquifier) {
        ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;
    }

    public static synchronized long getInitialSeedUniquifier() {
        long initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
        if (initialSeedUniquifier == 0L) {
            ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L);
        }
        if (initialSeedUniquifier == 0L) {
            final LinkedBlockingQueue queue = new LinkedBlockingQueue();
            Thread generatorThread = new Thread("initialSeedUniquifierGenerator"){

                @Override
                public void run() {
                    SecureRandom random = new SecureRandom();
                    queue.add(random.nextLong());
                }
            };
            generatorThread.start();
            long timeoutSeconds = 3L;
            long deadLine = System.nanoTime() + TimeUnit.SECONDS.toNanos(3L);
            while (true) {
                long waitTime;
                if ((waitTime = deadLine - System.nanoTime()) <= 0L) {
                    logger.warn("Failed to get the secure random number from SecureRandom within {} seconds. Not enough entrophy?", (Object)3L);
                    break;
                }
                try {
                    Long result = (Long)queue.poll(waitTime, TimeUnit.NANOSECONDS);
                    if (result == null) continue;
                    initialSeedUniquifier = result;
                }
                catch (InterruptedException ignore) {
                    continue;
                }
                break;
            }
            initialSeedUniquifier ^= 0x3255ECDC33BAE119L;
            ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier ^= Long.reverse(System.nanoTime());
        }
        return initialSeedUniquifier;
    }

    private static long newSeed() {
        long actualCurrent;
        long next;
        long current;
        while (!seedUniquifier.compareAndSet(current, next = (actualCurrent = (current = seedUniquifier.get()) != 0L ? current : ThreadLocalRandom.getInitialSeedUniquifier()) * 181783497276652981L)) {
        }
        if (current == 0L && logger.isDebugEnabled()) {
            logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x", actualCurrent));
        }
        return next ^ System.nanoTime();
    }

    ThreadLocalRandom() {
        super(ThreadLocalRandom.newSeed());
    }

    public static ThreadLocalRandom current() {
        return localRandom.get();
    }

    @Override
    public void setSeed(long seed) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
        this.rnd = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
    }

    @Override
    protected int next(int bits) {
        this.rnd = this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
        return (int)(this.rnd >>> 48 - bits);
    }

    public int nextInt(int least, int bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return this.nextInt(bound - least) + least;
    }

    public long nextLong(long n) {
        if (n <= 0L) {
            throw new IllegalArgumentException("n must be positive");
        }
        long offset = 0L;
        while (n >= Integer.MAX_VALUE) {
            long nextn;
            int bits = this.next(2);
            long half = n >>> 1;
            long l = nextn = (bits & 2) == 0 ? half : n - half;
            if ((bits & 1) == 0) {
                offset += n - nextn;
            }
            n = nextn;
        }
        return offset + (long)this.nextInt((int)n);
    }

    public long nextLong(long least, long bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return this.nextLong(bound - least) + least;
    }

    public double nextDouble(double n) {
        if (n <= 0.0) {
            throw new IllegalArgumentException("n must be positive");
        }
        return this.nextDouble() * n;
    }

    public double nextDouble(double least, double bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return this.nextDouble() * (bound - least) + least;
    }

    static {
        localRandom = new ThreadLocal<ThreadLocalRandom>(){

            @Override
            protected ThreadLocalRandom initialValue() {
                return new ThreadLocalRandom();
            }
        };
    }
}

