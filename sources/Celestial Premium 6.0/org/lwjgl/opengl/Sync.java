/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import org.lwjgl.Sys;

class Sync {
    private static final long NANOS_IN_SECOND = 1000000000L;
    private static long nextFrame = 0L;
    private static boolean initialised = false;
    private static RunningAvg sleepDurations = new RunningAvg(10);
    private static RunningAvg yieldDurations = new RunningAvg(10);

    Sync() {
    }

    public static void sync(int fps) {
        if (fps <= 0) {
            return;
        }
        if (!initialised) {
            Sync.initialise();
        }
        try {
            long t1;
            long t0 = Sync.getTime();
            while (nextFrame - t0 > sleepDurations.avg()) {
                Thread.sleep(1L);
                t1 = Sync.getTime();
                sleepDurations.add(t1 - t0);
                t0 = t1;
            }
            sleepDurations.dampenForLowResTicker();
            t0 = Sync.getTime();
            while (nextFrame - t0 > yieldDurations.avg()) {
                Thread.yield();
                t1 = Sync.getTime();
                yieldDurations.add(t1 - t0);
                t0 = t1;
            }
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        nextFrame = Math.max(nextFrame + 1000000000L / (long)fps, Sync.getTime());
    }

    private static void initialise() {
        initialised = true;
        sleepDurations.init(1000000L);
        yieldDurations.init((int)((double)(-(Sync.getTime() - Sync.getTime())) * 1.333));
        nextFrame = Sync.getTime();
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Win")) {
            Thread timerAccuracyThread = new Thread(new Runnable(){

                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            });
            timerAccuracyThread.setName("LWJGL Timer");
            timerAccuracyThread.setDaemon(true);
            timerAccuracyThread.start();
        }
    }

    private static long getTime() {
        return Sys.getTime() * 1000000000L / Sys.getTimerResolution();
    }

    private static class RunningAvg {
        private final long[] slots;
        private int offset;
        private static final long DAMPEN_THRESHOLD = 10000000L;
        private static final float DAMPEN_FACTOR = 0.9f;

        public RunningAvg(int slotCount) {
            this.slots = new long[slotCount];
            this.offset = 0;
        }

        public void init(long value) {
            while (this.offset < this.slots.length) {
                this.slots[this.offset++] = value;
            }
        }

        public void add(long value) {
            this.slots[this.offset++ % this.slots.length] = value;
            this.offset %= this.slots.length;
        }

        public long avg() {
            long sum = 0L;
            for (int i = 0; i < this.slots.length; ++i) {
                sum += this.slots[i];
            }
            return sum / (long)this.slots.length;
        }

        public void dampenForLowResTicker() {
            if (this.avg() > 10000000L) {
                int i = 0;
                while (i < this.slots.length) {
                    int n = i++;
                    this.slots[n] = (long)((float)this.slots[n] * 0.9f);
                }
            }
        }
    }
}

