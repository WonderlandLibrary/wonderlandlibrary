/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

public class ICURWLock {
    private Object writeLock = new Object();
    private Object readLock = new Object();
    private int wwc;
    private int rc;
    private int wrc;
    private Stats stats = new Stats();
    private static final int NOTIFY_NONE = 0;
    private static final int NOTIFY_WRITERS = 1;
    private static final int NOTIFY_READERS = 2;

    public synchronized Stats resetStats() {
        Stats result = this.stats;
        this.stats = new Stats();
        return result;
    }

    public synchronized Stats clearStats() {
        Stats result = this.stats;
        this.stats = null;
        return result;
    }

    public synchronized Stats getStats() {
        return this.stats == null ? null : new Stats(this.stats);
    }

    private synchronized boolean gotRead() {
        ++this.rc;
        if (this.stats != null) {
            ++this.stats._rc;
            if (this.rc > 1) {
                ++this.stats._mrc;
            }
        }
        return true;
    }

    private synchronized boolean getRead() {
        if (this.rc >= 0 && this.wwc == 0) {
            return this.gotRead();
        }
        ++this.wrc;
        return false;
    }

    private synchronized boolean retryRead() {
        if (this.stats != null) {
            ++this.stats._wrc;
        }
        if (this.rc >= 0 && this.wwc == 0) {
            --this.wrc;
            return this.gotRead();
        }
        return false;
    }

    private synchronized boolean finishRead() {
        if (this.rc > 0) {
            return 0 == --this.rc && this.wwc > 0;
        }
        throw new IllegalStateException("no current reader to release");
    }

    private synchronized boolean gotWrite() {
        this.rc = -1;
        if (this.stats != null) {
            ++this.stats._wc;
        }
        return true;
    }

    private synchronized boolean getWrite() {
        if (this.rc == 0) {
            return this.gotWrite();
        }
        ++this.wwc;
        return false;
    }

    private synchronized boolean retryWrite() {
        if (this.stats != null) {
            ++this.stats._wwc;
        }
        if (this.rc == 0) {
            --this.wwc;
            return this.gotWrite();
        }
        return false;
    }

    private synchronized int finishWrite() {
        if (this.rc < 0) {
            this.rc = 0;
            if (this.wwc > 0) {
                return 1;
            }
            if (this.wrc > 0) {
                return 2;
            }
            return 0;
        }
        throw new IllegalStateException("no current writer to release");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void acquireRead() {
        if (!this.getRead()) {
            while (true) {
                try {
                    do {
                        Object object = this.readLock;
                        synchronized (object) {
                            this.readLock.wait();
                        }
                    } while (!this.retryRead());
                    return;
                }
                catch (InterruptedException interruptedException) {
                    continue;
                }
                break;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void releaseRead() {
        if (this.finishRead()) {
            Object object = this.writeLock;
            synchronized (object) {
                this.writeLock.notify();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void acquireWrite() {
        if (!this.getWrite()) {
            while (true) {
                try {
                    do {
                        Object object = this.writeLock;
                        synchronized (object) {
                            this.writeLock.wait();
                        }
                    } while (!this.retryWrite());
                    return;
                }
                catch (InterruptedException interruptedException) {
                    continue;
                }
                break;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void releaseWrite() {
        switch (this.finishWrite()) {
            case 1: {
                Object object = this.writeLock;
                synchronized (object) {
                    this.writeLock.notify();
                    break;
                }
            }
            case 2: {
                Object object = this.readLock;
                synchronized (object) {
                    this.readLock.notifyAll();
                    break;
                }
            }
        }
    }

    public static final class Stats {
        public int _rc;
        public int _mrc;
        public int _wrc;
        public int _wc;
        public int _wwc;

        private Stats() {
        }

        private Stats(int rc, int mrc, int wrc, int wc, int wwc) {
            this._rc = rc;
            this._mrc = mrc;
            this._wrc = wrc;
            this._wc = wc;
            this._wwc = wwc;
        }

        private Stats(Stats rhs) {
            this(rhs._rc, rhs._mrc, rhs._wrc, rhs._wc, rhs._wwc);
        }

        public String toString() {
            return " rc: " + this._rc + " mrc: " + this._mrc + " wrc: " + this._wrc + " wc: " + this._wc + " wwc: " + this._wwc;
        }
    }
}

