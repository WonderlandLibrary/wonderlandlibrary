/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;

public interface Optimistic {
    public int getProgramPoint();

    public Optimistic setProgramPoint(int var1);

    public boolean canBeOptimistic();

    public Type getMostOptimisticType();

    public Type getMostPessimisticType();

    public Optimistic setType(Type var1);
}

