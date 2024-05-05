/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.Labels;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Node;

public interface BreakableNode
extends LexicalContextNode,
JoinPredecessor,
Labels {
    public Node ensureUniqueLabels(LexicalContext var1);

    public boolean isBreakableWithoutLabel();

    public Label getBreakLabel();
}

