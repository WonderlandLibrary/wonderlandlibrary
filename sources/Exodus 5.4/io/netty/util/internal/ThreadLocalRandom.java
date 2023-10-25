/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadLocalRandom
extends Random {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
    private static final AtomicLong seedUniquifier = new AtomicLong();
    private long pad5;
    private long rnd;
    private static final long addend = 11L;
    private long pad2;
    private long pad0;
    private long pad6;
    boolean initialized = true;
    private static final long serialVersionUID = -5851777807851030925L;
    private long pad1;
    private static volatile long initialSeedUniquifier;
    private long pad3;
    private long pad4;
    private static final long multiplier = 25214903917L;
    private long pad7;
    private static final long mask = 0xFFFFFFFFFFFFL;

    @Override
    public void setSeed(long l) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
        this.rnd = (l ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
    }

    private static long newSeed() {
        long l;
        long l2;
        long l3;
        long l4 = System.nanoTime();
        while (!seedUniquifier.compareAndSet(l3, l2 = (l = (l3 = seedUniquifier.get()) != 0L ? l3 : ThreadLocalRandom.getInitialSeedUniquifier()) * 181783497276652981L)) {
        }
        if (l3 == 0L && logger.isDebugEnabled()) {
            logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", l, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - l4)));
        }
        return l2 ^ System.nanoTime();
    }

    public double nextDouble(double d, double d2) {
        if (d >= d2) {
            throw new IllegalArgumentException();
        }
        return this.nextDouble() * (d2 - d) + d;
    }

    /*
     * Exception decompiling
     */
    public static synchronized long getInitialSeedUniquifier() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[DOLOOP]], but top level block is 0[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public long nextLong(long l, long l2) {
        if (l >= l2) {
            throw new IllegalArgumentException();
        }
        return this.nextLong(l2 - l) + l;
    }

    public static void setInitialSeedUniquifier(long l) {
        initialSeedUniquifier = l;
    }

    public static ThreadLocalRandom current() {
        return InternalThreadLocalMap.get().random();
    }

    @Override
    protected int next(int n) {
        this.rnd = this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
        return (int)(this.rnd >>> 48 - n);
    }

    public double nextDouble(double d) {
        if (d <= 0.0) {
            throw new IllegalArgumentException("n must be positive");
        }
        return this.nextDouble() * d;
    }

    public long nextLong(long l) {
        if (l <= 0L) {
            throw new IllegalArgumentException("n must be positive");
        }
        long l2 = 0L;
        while (l >= Integer.MAX_VALUE) {
            long l3;
            int n = this.next(2);
            long l4 = l >>> 1;
            long l5 = l3 = (n & 2) == 0 ? l4 : l - l4;
            if ((n & 1) == 0) {
                l2 += l - l3;
            }
            l = l3;
        }
        return l2 + (long)this.nextInt((int)l);
    }

    ThreadLocalRandom() {
        super(ThreadLocalRandom.newSeed());
    }

    public int nextInt(int n, int n2) {
        if (n >= n2) {
            throw new IllegalArgumentException();
        }
        return this.nextInt(n2 - n) + n;
    }
}

