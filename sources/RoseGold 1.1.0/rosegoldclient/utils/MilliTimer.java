package rosegoldclient.utils;

public class MilliTimer {
    private long time;

    public MilliTimer() {
        this.time = System.currentTimeMillis();
    }

    public long getTime() {
        return this.time;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.time;
    }

    public boolean hasTimePassed(final long milliseconds) {
        return System.currentTimeMillis() - this.time >= milliseconds;
    }

    public void updateTime() {
        this.time = System.currentTimeMillis();
    }
}
