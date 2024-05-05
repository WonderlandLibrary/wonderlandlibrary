package me.aidanmees.trivia.client.tools;

public class TimeTracker {
    private long last;

    public TimeTracker() {
        this.updateLastTime();
    }

    public boolean hasPassed(double milli) {
        return (double)(this.getTime() - this.last) >= milli;
    }

    public long getTime() {
        return System.nanoTime() / 1000000;
    }

    public long getElapsedTime() {
        return this.getTime() - this.last;
    }

    public void updateLastTime() {
        this.last = this.getTime();
    }
}

