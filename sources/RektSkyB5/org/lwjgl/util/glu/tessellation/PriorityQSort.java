/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.PriorityQ;
import org.lwjgl.util.glu.tessellation.PriorityQHeap;

class PriorityQSort
extends PriorityQ {
    PriorityQHeap heap;
    Object[] keys;
    int[] order;
    int size;
    int max;
    boolean initialized;
    PriorityQ.Leq leq;

    PriorityQSort(PriorityQ.Leq leq) {
        this.heap = new PriorityQHeap(leq);
        this.keys = new Object[32];
        this.size = 0;
        this.max = 32;
        this.initialized = false;
        this.leq = leq;
    }

    void pqDeletePriorityQ() {
        if (this.heap != null) {
            this.heap.pqDeletePriorityQ();
        }
        this.order = null;
        this.keys = null;
    }

    private static boolean LT(PriorityQ.Leq leq, Object x2, Object y2) {
        return !PriorityQHeap.LEQ(leq, y2, x2);
    }

    private static boolean GT(PriorityQ.Leq leq, Object x2, Object y2) {
        return !PriorityQHeap.LEQ(leq, x2, y2);
    }

    private static void Swap(int[] array, int a2, int b2) {
        int tmp = array[a2];
        array[a2] = array[b2];
        array[b2] = tmp;
    }

    boolean pqInit() {
        int i2;
        Stack[] stack = new Stack[50];
        for (int k2 = 0; k2 < stack.length; ++k2) {
            stack[k2] = new Stack();
        }
        int top = 0;
        int seed = 2016473283;
        this.order = new int[this.size + 1];
        int p2 = 0;
        int r2 = this.size - 1;
        int piv = 0;
        for (i2 = p2; i2 <= r2; ++i2) {
            this.order[i2] = piv++;
        }
        stack[top].p = p2;
        stack[top].r = r2;
        ++top;
        while (--top >= 0) {
            int j2;
            p2 = stack[top].p;
            r2 = stack[top].r;
            while (r2 > p2 + 10) {
                seed = Math.abs(seed * 1539415821 + 1);
                i2 = p2 + seed % (r2 - p2 + 1);
                piv = this.order[i2];
                this.order[i2] = this.order[p2];
                this.order[p2] = piv;
                i2 = p2 - 1;
                j2 = r2 + 1;
                while (true) {
                    if (PriorityQSort.GT(this.leq, this.keys[this.order[++i2]], this.keys[piv])) {
                        continue;
                    }
                    while (PriorityQSort.LT(this.leq, this.keys[this.order[--j2]], this.keys[piv])) {
                    }
                    PriorityQSort.Swap(this.order, i2, j2);
                    if (i2 >= j2) break;
                }
                PriorityQSort.Swap(this.order, i2, j2);
                if (i2 - p2 < r2 - j2) {
                    stack[top].p = j2 + 1;
                    stack[top].r = r2;
                    ++top;
                    r2 = i2 - 1;
                    continue;
                }
                stack[top].p = p2;
                stack[top].r = i2 - 1;
                ++top;
                p2 = j2 + 1;
            }
            for (i2 = p2 + 1; i2 <= r2; ++i2) {
                piv = this.order[i2];
                for (j2 = i2; j2 > p2 && PriorityQSort.LT(this.leq, this.keys[this.order[j2 - 1]], this.keys[piv]); --j2) {
                    this.order[j2] = this.order[j2 - 1];
                }
                this.order[j2] = piv;
            }
        }
        this.max = this.size;
        this.initialized = true;
        this.heap.pqInit();
        return true;
    }

    int pqInsert(Object keyNew) {
        if (this.initialized) {
            return this.heap.pqInsert(keyNew);
        }
        int curr = this.size++;
        if (this.size >= this.max) {
            Object[] saveKey = this.keys;
            this.max <<= 1;
            Object[] pqKeys = new Object[this.max];
            System.arraycopy(this.keys, 0, pqKeys, 0, this.keys.length);
            this.keys = pqKeys;
            if (this.keys == null) {
                this.keys = saveKey;
                return Integer.MAX_VALUE;
            }
        }
        assert (curr != Integer.MAX_VALUE);
        this.keys[curr] = keyNew;
        return -(curr + 1);
    }

    Object pqExtractMin() {
        Object heapMin;
        if (this.size == 0) {
            return this.heap.pqExtractMin();
        }
        Object sortMin = this.keys[this.order[this.size - 1]];
        if (!this.heap.pqIsEmpty() && PriorityQSort.LEQ(this.leq, heapMin = this.heap.pqMinimum(), sortMin)) {
            return this.heap.pqExtractMin();
        }
        do {
            --this.size;
        } while (this.size > 0 && this.keys[this.order[this.size - 1]] == null);
        return sortMin;
    }

    Object pqMinimum() {
        Object heapMin;
        if (this.size == 0) {
            return this.heap.pqMinimum();
        }
        Object sortMin = this.keys[this.order[this.size - 1]];
        if (!this.heap.pqIsEmpty() && PriorityQHeap.LEQ(this.leq, heapMin = this.heap.pqMinimum(), sortMin)) {
            return heapMin;
        }
        return sortMin;
    }

    boolean pqIsEmpty() {
        return this.size == 0 && this.heap.pqIsEmpty();
    }

    void pqDelete(int curr) {
        if (curr >= 0) {
            this.heap.pqDelete(curr);
            return;
        }
        curr = -(curr + 1);
        assert (curr < this.max && this.keys[curr] != null);
        this.keys[curr] = null;
        while (this.size > 0 && this.keys[this.order[this.size - 1]] == null) {
            --this.size;
        }
    }

    private static class Stack {
        int p;
        int r;

        private Stack() {
        }
    }
}

