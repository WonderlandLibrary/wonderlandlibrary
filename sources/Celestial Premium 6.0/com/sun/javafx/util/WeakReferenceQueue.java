/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;

public class WeakReferenceQueue<E> {
    private final ReferenceQueue garbage = new ReferenceQueue();
    private Object strongRef = new Object();
    private ListEntry head = new ListEntry(this.strongRef, this.garbage);
    int size = 0;

    public void add(E e) {
        this.cleanup();
        ++this.size;
        new ListEntry(e, this.garbage).insert(this.head.prev);
    }

    public void remove(E e) {
        this.cleanup();
        ListEntry listEntry = this.head.next;
        while (listEntry != this.head) {
            Object t = listEntry.get();
            if (t == e) {
                --this.size;
                listEntry.remove();
                return;
            }
            listEntry = listEntry.next;
        }
    }

    public void cleanup() {
        ListEntry listEntry;
        while ((listEntry = (ListEntry)this.garbage.poll()) != null) {
            --this.size;
            listEntry.remove();
        }
    }

    public Iterator<? super E> iterator() {
        return new Iterator(){
            private ListEntry index;
            private Object next;
            {
                this.index = WeakReferenceQueue.this.head;
                this.next = null;
            }

            @Override
            public boolean hasNext() {
                ListEntry listEntry;
                this.next = null;
                while (this.next == null && (listEntry = this.index.prev) != WeakReferenceQueue.this.head) {
                    this.next = listEntry.get();
                    if (this.next != null) continue;
                    --WeakReferenceQueue.this.size;
                    listEntry.remove();
                }
                return this.next != null;
            }

            public Object next() {
                this.hasNext();
                this.index = this.index.prev;
                return this.next;
            }

            @Override
            public void remove() {
                if (this.index != WeakReferenceQueue.this.head) {
                    ListEntry listEntry = this.index.next;
                    --WeakReferenceQueue.this.size;
                    this.index.remove();
                    this.index = listEntry;
                }
            }
        };
    }

    private static class ListEntry
    extends WeakReference {
        ListEntry prev = this;
        ListEntry next = this;

        public ListEntry(Object object, ReferenceQueue referenceQueue) {
            super(object, referenceQueue);
        }

        public void insert(ListEntry listEntry) {
            this.prev = listEntry;
            this.next = listEntry.next;
            listEntry.next = this;
            this.next.prev = this;
        }

        public void remove() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
            this.next = this;
            this.prev = this;
        }
    }
}

