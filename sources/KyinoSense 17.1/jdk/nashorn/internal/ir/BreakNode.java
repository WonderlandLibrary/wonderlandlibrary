/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class BreakNode
extends JumpStatement {
    private static final long serialVersionUID = 1L;

    public BreakNode(int lineNumber, long token, int finish, String labelName) {
        super(lineNumber, token, finish, labelName);
    }

    private BreakNode(BreakNode breakNode, LocalVariableConversion conversion) {
        super(breakNode, conversion);
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterBreakNode(this)) {
            return visitor.leaveBreakNode(this);
        }
        return this;
    }

    @Override
    JumpStatement createNewJumpStatement(LocalVariableConversion conversion) {
        return new BreakNode(this, conversion);
    }

    @Override
    String getStatementName() {
        return "break";
    }

    @Override
    public BreakableNode getTarget(LexicalContext lc) {
        return lc.getBreakable(this.getLabelName());
    }

    @Override
    Label getTargetLabel(BreakableNode target) {
        return target.getBreakLabel();
    }
}

