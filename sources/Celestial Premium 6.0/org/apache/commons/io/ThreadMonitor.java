/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io;

class ThreadMonitor
implements Runnable {
    private final Thread thread;
    private final long timeout;

    public static Thread start(long timeout) {
        return ThreadMonitor.start(Thread.currentThread(), timeout);
    }

    public static Thread start(Thread thread, long timeout) {
        Thread monitor = null;
        if (timeout > 0L) {
            ThreadMonitor timout = new ThreadMonitor(thread, timeout);
            monitor = new Thread((Runnable)timout, ThreadMonitor.class.getSimpleName());
            monitor.setDaemon(true);
            monitor.start();
        }
        return monitor;
    }

    public static void stop(Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }
    }

    private ThreadMonitor(Thread thread, long timeout) {
        this.thread = thread;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            ThreadMonitor.sleep(this.timeout);
            this.thread.interrupt();
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private static void sleep(long ms) throws InterruptedException {
        long finishAt = System.currentTimeMillis() + ms;
        long remaining = ms;
        do {
            Thread.sleep(remaining);
        } while ((remaining = finishAt - System.currentTimeMillis()) > 0L);
    }
}

