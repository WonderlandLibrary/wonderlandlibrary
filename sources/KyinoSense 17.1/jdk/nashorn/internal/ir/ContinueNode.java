/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public class ContinueNode
extends JumpStatement {
    private static final long serialVersionUID = 1L;

    public ContinueNode(int lineNumber, long token, int finish, String labelName) {
        super(lineNumber, token, finish, labelName);
    }

    private ContinueNode(ContinueNode continueNode, LocalVariableConversion conversion) {
        super(continueNode, conversion);
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterContinueNode(this)) {
            return visitor.leaveContinueNode(this);
        }
        return this;
    }

    @Override
    JumpStatement createNewJumpStatement(LocalVariableConversion conversion) {
        return new ContinueNode(this, conversion);
    }

    @Override
    String getStatementName() {
        return "continue";
    }

    @Override
    public BreakableNode getTarget(LexicalContext lc) {
        return lc.getContinueTo(this.getLabelName());
    }

    @Override
    Label getTargetLabel(BreakableNode target) {
        return ((LoopNode)target).getContinueLabel();
    }
}

