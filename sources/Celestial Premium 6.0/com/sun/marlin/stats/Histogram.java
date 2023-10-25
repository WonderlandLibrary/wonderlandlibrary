/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin.stats;

import com.sun.marlin.stats.StatLong;

public final class Histogram
extends StatLong {
    static final int BUCKET = 2;
    static final int MAX = 20;
    static final int LAST = 19;
    static final int[] STEPS = new int[20];
    private final StatLong[] stats = new StatLong[20];

    static int bucket(int n) {
        for (int i = 1; i < 20; ++i) {
            if (n >= STEPS[i]) continue;
            return i - 1;
        }
        return 19;
    }

    public Histogram(String string) {
        super(string);
        for (int i = 0; i < 20; ++i) {
            this.stats[i] = new StatLong(String.format("%5s .. %5s", STEPS[i], i + 1 < 20 ? Integer.valueOf(STEPS[i + 1]) : "~"));
        }
    }

    @Override
    public void reset() {
        super.reset();
        for (int i = 0; i < 20; ++i) {
            this.stats[i].reset();
        }
    }

    @Override
    public void add(int n) {
        super.add(n);
        this.stats[Histogram.bucket(n)].add(n);
    }

    @Override
    public void add(long l) {
        this.add((int)l);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(2048);
        super.toString(stringBuilder).append(" { ");
        for (int i = 0; i < 20; ++i) {
            if (this.stats[i].count == 0L) continue;
            stringBuilder.append("\n        ").append(this.stats[i].toString());
        }
        return stringBuilder.append(" }").toString();
    }

    static {
        Histogram.STEPS[0] = 0;
        Histogram.STEPS[1] = 1;
        for (int i = 2; i < 20; ++i) {
            Histogram.STEPS[i] = STEPS[i - 1] * 2;
        }
    }
}

