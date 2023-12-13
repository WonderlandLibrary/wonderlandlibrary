/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.PriorityQ;

class PriorityQHeap
extends PriorityQ {
    PriorityQ.PQnode[] nodes = new PriorityQ.PQnode[33];
    PriorityQ.PQhandleElem[] handles;
    int size = 0;
    int max = 32;
    int freeList;
    boolean initialized;
    PriorityQ.Leq leq;

    PriorityQHeap(PriorityQ.Leq leq) {
        int i2;
        for (i2 = 0; i2 < this.nodes.length; ++i2) {
            this.nodes[i2] = new PriorityQ.PQnode();
        }
        this.handles = new PriorityQ.PQhandleElem[33];
        for (i2 = 0; i2 < this.handles.length; ++i2) {
            this.handles[i2] = new PriorityQ.PQhandleElem();
        }
        this.initialized = false;
        this.freeList = 0;
        this.leq = leq;
        this.nodes[1].handle = 1;
        this.handles[1].key = null;
    }

    void pqDeletePriorityQ() {
        this.handles = null;
        this.nodes = null;
    }

    void FloatDown(int curr) {
        PriorityQ.PQnode[] n2 = this.nodes;
        PriorityQ.PQhandleElem[] h2 = this.handles;
        int hCurr = n2[curr].handle;
        while (true) {
            int child;
            if ((child = curr << 1) < this.size && PriorityQHeap.LEQ(this.leq, h2[n2[child + 1].handle].key, h2[n2[child].handle].key)) {
                ++child;
            }
            assert (child <= this.max);
            int hChild = n2[child].handle;
            if (child > this.size || PriorityQHeap.LEQ(this.leq, h2[hCurr].key, h2[hChild].key)) break;
            n2[curr].handle = hChild;
            h2[hChild].node = curr;
            curr = child;
        }
        n2[curr].handle = hCurr;
        h2[hCurr].node = curr;
    }

    void FloatUp(int curr) {
        PriorityQ.PQnode[] n2 = this.nodes;
        PriorityQ.PQhandleElem[] h2 = this.handles;
        int hCurr = n2[curr].handle;
        while (true) {
            int parent = curr >> 1;
            int hParent = n2[parent].handle;
            if (parent == 0 || PriorityQHeap.LEQ(this.leq, h2[hParent].key, h2[hCurr].key)) break;
            n2[curr].handle = hParent;
            h2[hParent].node = curr;
            curr = parent;
        }
        n2[curr].handle = hCurr;
        h2[hCurr].node = curr;
    }

    boolean pqInit() {
        for (int i2 = this.size; i2 >= 1; --i2) {
            this.FloatDown(i2);
        }
        this.initialized = true;
        return true;
    }

    int pqInsert(Object keyNew) {
        int free;
        int curr;
        if ((curr = ++this.size) * 2 > this.max) {
            PriorityQ.PQnode[] saveNodes = this.nodes;
            PriorityQ.PQhandleElem[] saveHandles = this.handles;
            this.max <<= 1;
            PriorityQ.PQnode[] pqNodes = new PriorityQ.PQnode[this.max + 1];
            System.arraycopy(this.nodes, 0, pqNodes, 0, this.nodes.length);
            for (int i2 = this.nodes.length; i2 < pqNodes.length; ++i2) {
                pqNodes[i2] = new PriorityQ.PQnode();
            }
            this.nodes = pqNodes;
            if (this.nodes == null) {
                this.nodes = saveNodes;
                return Integer.MAX_VALUE;
            }
            PriorityQ.PQhandleElem[] pqHandles = new PriorityQ.PQhandleElem[this.max + 1];
            System.arraycopy(this.handles, 0, pqHandles, 0, this.handles.length);
            for (int i3 = this.handles.length; i3 < pqHandles.length; ++i3) {
                pqHandles[i3] = new PriorityQ.PQhandleElem();
            }
            this.handles = pqHandles;
            if (this.handles == null) {
                this.handles = saveHandles;
                return Integer.MAX_VALUE;
            }
        }
        if (this.freeList == 0) {
            free = curr;
        } else {
            free = this.freeList;
            this.freeList = this.handles[free].node;
        }
        this.nodes[curr].handle = free;
        this.handles[free].node = curr;
        this.handles[free].key = keyNew;
        if (this.initialized) {
            this.FloatUp(curr);
        }
        assert (free != Integer.MAX_VALUE);
        return free;
    }

    Object pqExtractMin() {
        PriorityQ.PQnode[] n2 = this.nodes;
        PriorityQ.PQhandleElem[] h2 = this.handles;
        int hMin = n2[1].handle;
        Object min = h2[hMin].key;
        if (this.size > 0) {
            n2[1].handle = n2[this.size].handle;
            h2[n2[1].handle].node = 1;
            h2[hMin].key = null;
            h2[hMin].node = this.freeList;
            this.freeList = hMin;
            if (--this.size > 0) {
                this.FloatDown(1);
            }
        }
        return min;
    }

    void pqDelete(int hCurr) {
        PriorityQ.PQnode[] n2 = this.nodes;
        PriorityQ.PQhandleElem[] h2 = this.handles;
        assert (hCurr >= 1 && hCurr <= this.max && h2[hCurr].key != null);
        int curr = h2[hCurr].node;
        n2[curr].handle = n2[this.size].handle;
        h2[n2[curr].handle].node = curr;
        if (curr <= --this.size) {
            if (curr <= 1 || PriorityQHeap.LEQ(this.leq, h2[n2[curr >> 1].handle].key, h2[n2[curr].handle].key)) {
                this.FloatDown(curr);
            } else {
                this.FloatUp(curr);
            }
        }
        h2[hCurr].key = null;
        h2[hCurr].node = this.freeList;
        this.freeList = hCurr;
    }

    Object pqMinimum() {
        return this.handles[this.nodes[1].handle].key;
    }

    boolean pqIsEmpty() {
        return this.size == 0;
    }
}

