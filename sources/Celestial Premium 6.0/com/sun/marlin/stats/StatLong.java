/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin.stats;

public class StatLong {
    public final String name;
    public long count = 0L;
    public long sum = 0L;
    public long min = Integer.MAX_VALUE;
    public long max = Integer.MIN_VALUE;

    public StatLong(String string) {
        this.name = string;
    }

    public void reset() {
        this.count = 0L;
        this.sum = 0L;
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
    }

    public void add(int n) {
        ++this.count;
        this.sum += (long)n;
        if ((long)n < this.min) {
            this.min = n;
        }
        if ((long)n > this.max) {
            this.max = n;
        }
    }

    public void add(long l) {
        ++this.count;
        this.sum += l;
        if (l < this.min) {
            this.min = l;
        }
        if (l > this.max) {
            this.max = l;
        }
    }

    public String toString() {
        return this.toString(new StringBuilder(128)).toString();
    }

    public final StringBuilder toString(StringBuilder stringBuilder) {
        stringBuilder.append(this.name).append('[').append(this.count);
        stringBuilder.append("] sum: ").append(this.sum).append(" avg: ");
        stringBuilder.append(StatLong.trimTo3Digits((double)this.sum / (double)this.count));
        stringBuilder.append(" [").append(this.min).append(" | ").append(this.max).append("]");
        return stringBuilder;
    }

    public static double trimTo3Digits(double d) {
        return (double)((long)(1000.0 * d)) / 1000.0;
    }
}

