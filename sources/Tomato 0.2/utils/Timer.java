/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

public final class Timer {
    private long time = System.nanoTime() / 1000000;
    private long prevMS = 0;

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean delay(float milliSec) {
        if ((float)(this.time() - this.prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public long time() {
        return System.nanoTime() / 1000000 - this.time;
    }

    public void reset() {
        this.time = System.nanoTime() / 1000000;
    }

    public long getDifference() {
        return this.time() - this.prevMS;
    }
}

