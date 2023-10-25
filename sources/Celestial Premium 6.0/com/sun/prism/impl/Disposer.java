/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.impl.PrismSettings;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.LinkedList;

public class Disposer {
    private static Disposer disposerInstance;
    private static final int WEAK = 0;
    private static final int PHANTOM = 1;
    private static final int SOFT = 2;
    private static int refType;
    private final ReferenceQueue queue = new ReferenceQueue();
    private final Hashtable records = new Hashtable();
    private final LinkedList<Record> disposalQueue = new LinkedList();

    private Disposer() {
    }

    public static void addRecord(Object object, Record record) {
        disposerInstance.add(object, record);
    }

    public static void disposeRecord(Record record) {
        disposerInstance.addToDisposalQueue(record);
    }

    public static void cleanUp() {
        disposerInstance.disposeUnreachables();
        disposerInstance.processDisposalQueue();
    }

    private synchronized void add(Object object, Record record) {
        if (object instanceof Target) {
            object = ((Target)object).getDisposerReferent();
        }
        Reference reference = refType == 1 ? new PhantomReference<Object>(object, this.queue) : (refType == 2 ? new SoftReference<Object>(object, this.queue) : new WeakReference<Object>(object, this.queue));
        this.records.put(reference, record);
    }

    private synchronized void addToDisposalQueue(Record record) {
        this.disposalQueue.add(record);
    }

    private synchronized void disposeUnreachables() {
        Reference reference;
        while ((reference = this.queue.poll()) != null) {
            try {
                reference.clear();
                Record record = (Record)this.records.remove(reference);
                record.dispose();
                reference = null;
                record = null;
            }
            catch (Exception exception) {
                System.out.println("Exception while removing reference: " + exception);
                exception.printStackTrace();
            }
        }
    }

    private synchronized void processDisposalQueue() {
        while (!this.disposalQueue.isEmpty()) {
            this.disposalQueue.remove().dispose();
        }
    }

    static {
        refType = 1;
        String string = PrismSettings.refType;
        if (string != null) {
            if (string.equals("weak")) {
                refType = 0;
                if (PrismSettings.verbose) {
                    System.err.println("Using WEAK refs");
                }
            } else if (string.equals("soft")) {
                refType = 2;
                if (PrismSettings.verbose) {
                    System.err.println("Using SOFT refs");
                }
            } else {
                refType = 1;
                if (PrismSettings.verbose) {
                    System.err.println("Using PHANTOM refs");
                }
            }
        }
        disposerInstance = new Disposer();
    }

    public static interface Record {
        public void dispose();
    }

    public static interface Target {
        public Object getDisposerReferent();
    }
}

