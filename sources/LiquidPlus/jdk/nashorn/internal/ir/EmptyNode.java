/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class EmptyNode
extends Statement {
    private static final long serialVersionUID = 1L;

    public EmptyNode(Statement node) {
        super(node);
    }

    public EmptyNode(int lineNumber, long token, int finish) {
        super(lineNumber, token, finish);
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterEmptyNode(this)) {
            return visitor.leaveEmptyNode(this);
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printTypes) {
        sb.append(';');
    }
}

