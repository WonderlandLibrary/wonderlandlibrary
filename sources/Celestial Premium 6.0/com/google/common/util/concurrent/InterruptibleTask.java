/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;

@GwtCompatible(emulated=true)
abstract class InterruptibleTask
implements Runnable {
    private volatile Thread runner;
    private volatile boolean doneInterrupting;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log;

    InterruptibleTask() {
    }

    @Override
    public final void run() {
        if (!ATOMIC_HELPER.compareAndSetRunner(this, null, Thread.currentThread())) {
            return;
        }
        try {
            this.runInterruptibly();
        }
        finally {
            if (this.wasInterrupted()) {
                while (!this.doneInterrupting) {
                    Thread.yield();
                }
            }
        }
    }

    abstract void runInterruptibly();

    abstract boolean wasInterrupted();

    final void interruptTask() {
        Thread currentRunner = this.runner;
        if (currentRunner != null) {
            currentRunner.interrupt();
        }
        this.doneInterrupting = true;
    }

    static {
        AtomicHelper helper;
        log = Logger.getLogger(InterruptibleTask.class.getName());
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(InterruptibleTask.class, Thread.class, "runner"));
        }
        catch (Throwable reflectionFailure) {
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }

    private static final class SynchronizedAtomicHelper
    extends AtomicHelper {
        private SynchronizedAtomicHelper() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
            InterruptibleTask interruptibleTask = task;
            synchronized (interruptibleTask) {
                if (task.runner == expect) {
                    task.runner = update;
                }
            }
            return true;
        }
    }

    private static final class SafeAtomicHelper
    extends AtomicHelper {
        final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater runnerUpdater) {
            this.runnerUpdater = runnerUpdater;
        }

        @Override
        boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
            return this.runnerUpdater.compareAndSet(task, expect, update);
        }
    }

    private static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        abstract boolean compareAndSetRunner(InterruptibleTask var1, Thread var2, Thread var3);
    }
}

