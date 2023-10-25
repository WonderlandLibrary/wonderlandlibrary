/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class Timer
implements Serializable,
StringBuilderFormattable {
    private static final long serialVersionUID = 9175191792439630013L;
    private final String name;
    private Status status;
    private long elapsedTime;
    private final int iterations;
    private static long NANO_PER_SECOND = 1000000000L;
    private static long NANO_PER_MINUTE = NANO_PER_SECOND * 60L;
    private static long NANO_PER_HOUR = NANO_PER_MINUTE * 60L;
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>(){

        @Override
        protected Long initialValue() {
            return 0L;
        }
    };

    public Timer(String name) {
        this(name, 0);
    }

    public Timer(String name, int iterations) {
        this.name = name;
        this.status = Status.Stopped;
        this.iterations = iterations > 0 ? iterations : 0;
    }

    public synchronized void start() {
        this.startTime.set(System.nanoTime());
        this.elapsedTime = 0L;
        this.status = Status.Started;
    }

    public synchronized void startOrResume() {
        if (this.status == Status.Stopped) {
            this.start();
        } else {
            this.resume();
        }
    }

    public synchronized String stop() {
        this.elapsedTime += System.nanoTime() - this.startTime.get();
        this.startTime.set(0L);
        this.status = Status.Stopped;
        return this.toString();
    }

    public synchronized void pause() {
        this.elapsedTime += System.nanoTime() - this.startTime.get();
        this.startTime.set(0L);
        this.status = Status.Paused;
    }

    public synchronized void resume() {
        this.startTime.set(System.nanoTime());
        this.status = Status.Started;
    }

    public String getName() {
        return this.name;
    }

    public long getElapsedTime() {
        return this.elapsedTime / 1000000L;
    }

    public long getElapsedNanoTime() {
        return this.elapsedTime;
    }

    public Status getStatus() {
        return this.status;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        this.formatTo(result);
        return result.toString();
    }

    @Override
    public void formatTo(StringBuilder buffer) {
        buffer.append("Timer ").append(this.name);
        switch (this.status) {
            case Started: {
                buffer.append(" started");
                break;
            }
            case Paused: {
                buffer.append(" paused");
                break;
            }
            case Stopped: {
                long nanoseconds = this.elapsedTime;
                long hours = nanoseconds / NANO_PER_HOUR;
                long minutes = (nanoseconds %= NANO_PER_HOUR) / NANO_PER_MINUTE;
                long seconds = (nanoseconds %= NANO_PER_MINUTE) / NANO_PER_SECOND;
                nanoseconds %= NANO_PER_SECOND;
                String elapsed = "";
                if (hours > 0L) {
                    elapsed = elapsed + hours + " hours ";
                }
                if (minutes > 0L || hours > 0L) {
                    elapsed = elapsed + minutes + " minutes ";
                }
                DecimalFormat numFormat = new DecimalFormat("#0");
                elapsed = elapsed + numFormat.format(seconds) + '.';
                numFormat = new DecimalFormat("000000000");
                elapsed = elapsed + numFormat.format(nanoseconds) + " seconds";
                buffer.append(" stopped. Elapsed time: ").append(elapsed);
                if (this.iterations <= 0) break;
                nanoseconds = this.elapsedTime / (long)this.iterations;
                hours = nanoseconds / NANO_PER_HOUR;
                minutes = (nanoseconds %= NANO_PER_HOUR) / NANO_PER_MINUTE;
                seconds = (nanoseconds %= NANO_PER_MINUTE) / NANO_PER_SECOND;
                nanoseconds %= NANO_PER_SECOND;
                elapsed = "";
                if (hours > 0L) {
                    elapsed = elapsed + hours + " hours ";
                }
                if (minutes > 0L || hours > 0L) {
                    elapsed = elapsed + minutes + " minutes ";
                }
                numFormat = new DecimalFormat("#0");
                elapsed = elapsed + numFormat.format(seconds) + '.';
                numFormat = new DecimalFormat("000000000");
                elapsed = elapsed + numFormat.format(nanoseconds) + " seconds";
                buffer.append(" Average per iteration: ").append(elapsed);
                break;
            }
            default: {
                buffer.append(' ').append((Object)this.status);
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Timer)) {
            return false;
        }
        Timer timer = (Timer)o;
        if (this.elapsedTime != timer.elapsedTime) {
            return false;
        }
        if (this.startTime != timer.startTime) {
            return false;
        }
        if (this.name != null ? !this.name.equals(timer.name) : timer.name != null) {
            return false;
        }
        return !(this.status != null ? !this.status.equals((Object)timer.status) : timer.status != null);
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        result = 29 * result + (this.status != null ? this.status.hashCode() : 0);
        long time = this.startTime.get();
        result = 29 * result + (int)(time ^ time >>> 32);
        result = 29 * result + (int)(this.elapsedTime ^ this.elapsedTime >>> 32);
        return result;
    }

    public static enum Status {
        Started,
        Stopped,
        Paused;

    }
}

