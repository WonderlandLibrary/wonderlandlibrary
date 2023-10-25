/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.DisposerRecord;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

public class Disposer
implements Runnable {
    private static final ReferenceQueue queue = new ReferenceQueue();
    private static final Hashtable records = new Hashtable();
    private static Disposer disposerInstance = new Disposer();

    public static WeakReference addRecord(Object object, DisposerRecord disposerRecord) {
        WeakReference<Object> weakReference = new WeakReference<Object>(object, queue);
        records.put(weakReference, disposerRecord);
        return weakReference;
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    Reference reference = queue.remove();
                    reference.clear();
                    DisposerRecord disposerRecord = (DisposerRecord)records.remove(reference);
                    disposerRecord.dispose();
                    reference = null;
                    disposerRecord = null;
                }
            }
            catch (Exception exception) {
                System.out.println("Exception while removing reference: " + exception);
                exception.printStackTrace();
                continue;
            }
            break;
        }
    }

    static {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Object t = AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                Object object = threadGroup;
                while (object != null) {
                    threadGroup = object;
                    object = threadGroup.getParent();
                }
                object = new Thread(threadGroup, disposerInstance, "Prism Font Disposer");
                ((Thread)object).setContextClassLoader(null);
                ((Thread)object).setDaemon(true);
                ((Thread)object).setPriority(10);
                ((Thread)object).start();
                return null;
            }
        });
    }
}

