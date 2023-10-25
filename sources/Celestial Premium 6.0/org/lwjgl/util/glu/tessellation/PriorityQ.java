/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.tessellation.GLUvertex;
import org.lwjgl.util.glu.tessellation.Geom;
import org.lwjgl.util.glu.tessellation.PriorityQSort;

abstract class PriorityQ {
    public static final int INIT_SIZE = 32;

    PriorityQ() {
    }

    public static boolean LEQ(Leq leq, Object x, Object y) {
        return Geom.VertLeq((GLUvertex)x, (GLUvertex)y);
    }

    static PriorityQ pqNewPriorityQ(Leq leq) {
        return new PriorityQSort(leq);
    }

    abstract void pqDeletePriorityQ();

    abstract boolean pqInit();

    abstract int pqInsert(Object var1);

    abstract Object pqExtractMin();

    abstract void pqDelete(int var1);

    abstract Object pqMinimum();

    abstract boolean pqIsEmpty();

    public static interface Leq {
        public boolean leq(Object var1, Object var2);
    }

    public static class PQhandleElem {
        Object key;
        int node;
    }

    public static class PQnode {
        int handle;
    }
}

