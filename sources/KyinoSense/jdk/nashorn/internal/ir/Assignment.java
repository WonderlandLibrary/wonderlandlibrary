/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.Node;

public interface Assignment<D extends Expression> {
    public D getAssignmentDest();

    public Expression getAssignmentSource();

    public Node setAssignmentDest(D var1);
}

