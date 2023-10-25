/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.util.AbstractSet;
import java.util.Iterator;

final class SelectedSelectionKeySet
extends AbstractSet<SelectionKey> {
    private SelectionKey[] keysA = new SelectionKey[1024];
    private boolean isA = true;
    private int keysBSize;
    private SelectionKey[] keysB = (SelectionKey[])this.keysA.clone();
    private int keysASize;

    private void doubleCapacityB() {
        SelectionKey[] selectionKeyArray = new SelectionKey[this.keysB.length << 1];
        System.arraycopy(this.keysB, 0, selectionKeyArray, 0, this.keysBSize);
        this.keysB = selectionKeyArray;
    }

    SelectionKey[] flip() {
        if (this.isA) {
            this.isA = false;
            this.keysA[this.keysASize] = null;
            this.keysBSize = 0;
            return this.keysA;
        }
        this.isA = true;
        this.keysB[this.keysBSize] = null;
        this.keysASize = 0;
        return this.keysB;
    }

    @Override
    public Iterator<SelectionKey> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(SelectionKey selectionKey) {
        if (selectionKey == null) {
            return false;
        }
        if (this.isA) {
            int n = this.keysASize;
            this.keysA[n++] = selectionKey;
            this.keysASize = n;
            if (n == this.keysA.length) {
                this.doubleCapacityA();
            }
        } else {
            int n = this.keysBSize;
            this.keysB[n++] = selectionKey;
            this.keysBSize = n;
            if (n == this.keysB.length) {
                this.doubleCapacityB();
            }
        }
        return true;
    }

    @Override
    public boolean remove(Object object) {
        return false;
    }

    SelectedSelectionKeySet() {
    }

    private void doubleCapacityA() {
        SelectionKey[] selectionKeyArray = new SelectionKey[this.keysA.length << 1];
        System.arraycopy(this.keysA, 0, selectionKeyArray, 0, this.keysASize);
        this.keysA = selectionKeyArray;
    }

    @Override
    public int size() {
        if (this.isA) {
            return this.keysASize;
        }
        return this.keysBSize;
    }

    @Override
    public boolean contains(Object object) {
        return false;
    }
}

