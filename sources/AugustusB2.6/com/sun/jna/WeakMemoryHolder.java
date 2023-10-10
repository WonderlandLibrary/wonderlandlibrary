// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import java.util.IdentityHashMap;
import java.lang.ref.ReferenceQueue;

public class WeakMemoryHolder
{
    ReferenceQueue<Object> referenceQueue;
    IdentityHashMap<Reference<Object>, Memory> backingMap;
    
    public WeakMemoryHolder() {
        this.referenceQueue = new ReferenceQueue<Object>();
        this.backingMap = new IdentityHashMap<Reference<Object>, Memory>();
    }
    
    public synchronized void put(final Object o, final Memory m) {
        this.clean();
        final Reference<Object> reference = new WeakReference<Object>(o, this.referenceQueue);
        this.backingMap.put(reference, m);
    }
    
    public synchronized void clean() {
        for (Reference ref = this.referenceQueue.poll(); ref != null; ref = this.referenceQueue.poll()) {
            this.backingMap.remove(ref);
        }
    }
}
