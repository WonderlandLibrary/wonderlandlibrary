package xyz.northclient.util;

public class Stopwatch {
    public long start;

    public Stopwatch() {
        this.reset();
    }
    public boolean elapsed(long period) {
        return (System.currentTimeMillis()-period) > start;
    }
    public boolean elapsed2(double period) {
        return (System.currentTimeMillis()-period) > start;
    }

    public void reset() {
        start = System.currentTimeMillis();
    }
}
