/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public final class InvokeLaterDispatcher
extends Thread {
    private final BlockingDeque<Runnable> deque = new LinkedBlockingDeque<Runnable>();
    private final Object LOCK = new StringBuilder("InvokeLaterLock");
    private boolean nestedEventLoopEntered = false;
    private volatile boolean leavingNestedEventLoop = false;
    private final InvokeLaterSubmitter invokeLaterSubmitter;

    public InvokeLaterDispatcher(InvokeLaterSubmitter invokeLaterSubmitter) {
        super("InvokeLaterDispatcher");
        this.setDaemon(true);
        this.invokeLaterSubmitter = invokeLaterSubmitter;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Object object;
                Runnable runnable = this.deque.takeFirst();
                if (this.leavingNestedEventLoop) {
                    this.deque.addFirst(runnable);
                    object = this.LOCK;
                    synchronized (object) {
                        while (this.leavingNestedEventLoop) {
                            this.LOCK.wait();
                        }
                        continue;
                    }
                }
                object = new Future(runnable);
                this.invokeLaterSubmitter.submitForLaterInvocation((Runnable)object);
                Object object2 = this.LOCK;
                synchronized (object2) {
                    try {
                        while (!((Future)object).isDone() && !this.nestedEventLoopEntered) {
                            this.LOCK.wait();
                        }
                    }
                    finally {
                        this.nestedEventLoopEntered = false;
                    }
                }
            }
            catch (InterruptedException interruptedException) {
                return;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void invokeAndWait(Runnable runnable) {
        Future future = new Future(runnable);
        this.invokeLaterSubmitter.submitForLaterInvocation(future);
        Object object = this.LOCK;
        synchronized (object) {
            try {
                while (!future.isDone()) {
                    this.LOCK.wait();
                }
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    public void invokeLater(Runnable runnable) {
        this.deque.addLast(runnable);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void notifyEnteringNestedEventLoop() {
        Object object = this.LOCK;
        synchronized (object) {
            this.nestedEventLoopEntered = true;
            this.LOCK.notifyAll();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void notifyLeavingNestedEventLoop() {
        Object object = this.LOCK;
        synchronized (object) {
            this.leavingNestedEventLoop = true;
            this.LOCK.notifyAll();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void notifyLeftNestedEventLoop() {
        Object object = this.LOCK;
        synchronized (object) {
            this.leavingNestedEventLoop = false;
            this.LOCK.notifyAll();
        }
    }

    public static interface InvokeLaterSubmitter {
        public void submitForLaterInvocation(Runnable var1);
    }

    private class Future
    implements Runnable {
        private boolean done = false;
        private final Runnable runnable;

        public Future(Runnable runnable) {
            this.runnable = runnable;
        }

        public boolean isDone() {
            return this.done;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            try {
                this.runnable.run();
            }
            finally {
                Object object = InvokeLaterDispatcher.this.LOCK;
                synchronized (object) {
                    this.done = true;
                    InvokeLaterDispatcher.this.LOCK.notifyAll();
                }
            }
        }
    }
}

