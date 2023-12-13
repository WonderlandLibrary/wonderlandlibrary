/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public abstract class ICUNotifier {
    private final Object notifyLock = new Object();
    private NotifyThread notifyThread;
    private List<EventListener> listeners;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addListener(EventListener l2) {
        if (l2 == null) {
            throw new NullPointerException();
        }
        if (this.acceptsListener(l2)) {
            Object object = this.notifyLock;
            synchronized (object) {
                if (this.listeners == null) {
                    this.listeners = new ArrayList<EventListener>();
                } else {
                    for (EventListener ll : this.listeners) {
                        if (ll != l2) continue;
                        return;
                    }
                }
                this.listeners.add(l2);
            }
        } else {
            throw new IllegalStateException("Listener invalid for this notifier.");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeListener(EventListener l2) {
        if (l2 == null) {
            throw new NullPointerException();
        }
        Object object = this.notifyLock;
        synchronized (object) {
            if (this.listeners != null) {
                Iterator<EventListener> iter = this.listeners.iterator();
                while (iter.hasNext()) {
                    if (iter.next() != l2) continue;
                    iter.remove();
                    if (this.listeners.size() == 0) {
                        this.listeners = null;
                    }
                    return;
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void notifyChanged() {
        Object object = this.notifyLock;
        synchronized (object) {
            if (this.listeners != null) {
                if (this.notifyThread == null) {
                    this.notifyThread = new NotifyThread(this);
                    this.notifyThread.setDaemon(true);
                    this.notifyThread.start();
                }
                this.notifyThread.queue(this.listeners.toArray(new EventListener[this.listeners.size()]));
            }
        }
    }

    protected abstract boolean acceptsListener(EventListener var1);

    protected abstract void notifyListener(EventListener var1);

    private static class NotifyThread
    extends Thread {
        private final ICUNotifier notifier;
        private final List<EventListener[]> queue = new ArrayList<EventListener[]>();

        NotifyThread(ICUNotifier notifier) {
            this.notifier = notifier;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void queue(EventListener[] list) {
            NotifyThread notifyThread = this;
            synchronized (notifyThread) {
                this.queue.add(list);
                this.notify();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (true) {
                try {
                    block6: while (true) {
                        EventListener[] list;
                        NotifyThread notifyThread = this;
                        synchronized (notifyThread) {
                            while (this.queue.isEmpty()) {
                                this.wait();
                            }
                            list = this.queue.remove(0);
                        }
                        int i2 = 0;
                        while (true) {
                            if (i2 >= list.length) continue block6;
                            this.notifier.notifyListener(list[i2]);
                            ++i2;
                        }
                        break;
                    }
                }
                catch (InterruptedException interruptedException) {
                    continue;
                }
                break;
            }
        }
    }
}

