/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.time;

import java.io.Serializable;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.time.PreciseClock;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive(value={"allocation"})
public class MutableInstant
implements Instant,
Serializable {
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int NANOS_PER_MILLI = 1000000;
    private static final int NANOS_PER_SECOND = 1000000000;
    private long epochSecond;
    private int nanoOfSecond;

    @Override
    public long getEpochSecond() {
        return this.epochSecond;
    }

    @Override
    public int getNanoOfSecond() {
        return this.nanoOfSecond;
    }

    @Override
    public long getEpochMillisecond() {
        int millis = this.nanoOfSecond / 1000000;
        long epochMillisecond = this.epochSecond * 1000L + (long)millis;
        return epochMillisecond;
    }

    @Override
    public int getNanoOfMillisecond() {
        int millis = this.nanoOfSecond / 1000000;
        int nanoOfMillisecond = this.nanoOfSecond - millis * 1000000;
        return nanoOfMillisecond;
    }

    public void initFrom(Instant other) {
        this.epochSecond = other.getEpochSecond();
        this.nanoOfSecond = other.getNanoOfSecond();
    }

    public void initFromEpochMilli(long epochMilli, int nanoOfMillisecond) {
        this.validateNanoOfMillisecond(nanoOfMillisecond);
        this.epochSecond = epochMilli / 1000L;
        this.nanoOfSecond = (int)(epochMilli - this.epochSecond * 1000L) * 1000000 + nanoOfMillisecond;
    }

    private void validateNanoOfMillisecond(int nanoOfMillisecond) {
        if (nanoOfMillisecond < 0 || nanoOfMillisecond >= 1000000) {
            throw new IllegalArgumentException("Invalid nanoOfMillisecond " + nanoOfMillisecond);
        }
    }

    public void initFrom(Clock clock) {
        if (clock instanceof PreciseClock) {
            ((PreciseClock)clock).init(this);
        } else {
            this.initFromEpochMilli(clock.currentTimeMillis(), 0);
        }
    }

    public void initFromEpochSecond(long epochSecond, int nano) {
        this.validateNanoOfSecond(nano);
        this.epochSecond = epochSecond;
        this.nanoOfSecond = nano;
    }

    private void validateNanoOfSecond(int nano) {
        if (nano < 0 || nano >= 1000000000) {
            throw new IllegalArgumentException("Invalid nanoOfSecond " + nano);
        }
    }

    public static void instantToMillisAndNanos(long epochSecond, int nano, long[] result) {
        int millis = nano / 1000000;
        result[0] = epochSecond * 1000L + (long)millis;
        result[1] = nano - millis * 1000000;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MutableInstant)) {
            return false;
        }
        MutableInstant other = (MutableInstant)object;
        return this.epochSecond == other.epochSecond && this.nanoOfSecond == other.nanoOfSecond;
    }

    public int hashCode() {
        int result = 17;
        result = 31 * result + (int)(this.epochSecond ^ this.epochSecond >>> 32);
        result = 31 * result + this.nanoOfSecond;
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        this.formatTo(sb);
        return sb.toString();
    }

    @Override
    public void formatTo(StringBuilder buffer) {
        buffer.append("MutableInstant[epochSecond=").append(this.epochSecond).append(", nano=").append(this.nanoOfSecond).append("]");
    }
}

