/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.util.ICUCloneNotSupportedException;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedObject
implements Cloneable {
    private AtomicInteger refCount = new AtomicInteger();

    public SharedObject clone() {
        SharedObject c2;
        try {
            c2 = (SharedObject)super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
        c2.refCount = new AtomicInteger();
        return c2;
    }

    public final void addRef() {
        this.refCount.incrementAndGet();
    }

    public final void removeRef() {
        this.refCount.decrementAndGet();
    }

    public final int getRefCount() {
        return this.refCount.get();
    }

    public final void deleteIfZeroRefCount() {
    }

    public static final class Reference<T extends SharedObject>
    implements Cloneable {
        private T ref;

        public Reference(T r2) {
            this.ref = r2;
            if (r2 != null) {
                ((SharedObject)r2).addRef();
            }
        }

        public Reference<T> clone() {
            Reference c2;
            try {
                c2 = (Reference)super.clone();
            }
            catch (CloneNotSupportedException e2) {
                throw new ICUCloneNotSupportedException(e2);
            }
            if (this.ref != null) {
                ((SharedObject)this.ref).addRef();
            }
            return c2;
        }

        public T readOnly() {
            return this.ref;
        }

        public T copyOnWrite() {
            T r2 = this.ref;
            if (((SharedObject)r2).getRefCount() <= 1) {
                return r2;
            }
            SharedObject r22 = ((SharedObject)r2).clone();
            ((SharedObject)r2).removeRef();
            this.ref = r22;
            r22.addRef();
            return (T)r22;
        }

        public void clear() {
            if (this.ref != null) {
                ((SharedObject)this.ref).removeRef();
                this.ref = null;
            }
        }

        protected void finalize() throws Throwable {
            super.finalize();
            this.clear();
        }
    }
}

