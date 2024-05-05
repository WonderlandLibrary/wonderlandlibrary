/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.Objects;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class JumpToInlinedFinally
extends JumpStatement {
    private static final long serialVersionUID = 1L;

    public JumpToInlinedFinally(String labelName) {
        super(-1, 0L, 0, Objects.requireNonNull(labelName));
    }

    private JumpToInlinedFinally(JumpToInlinedFinally breakNode, LocalVariableConversion conversion) {
        super(breakNode, conversion);
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterJumpToInlinedFinally(this)) {
            return visitor.leaveJumpToInlinedFinally(this);
        }
        return this;
    }

    @Override
    JumpStatement createNewJumpStatement(LocalVariableConversion conversion) {
        return new JumpToInlinedFinally(this, conversion);
    }

    @Override
    String getStatementName() {
        return ":jumpToInlinedFinally";
    }

    @Override
    public Block getTarget(LexicalContext lc) {
        return lc.getInlinedFinally(this.getLabelName());
    }

    @Override
    public TryNode getPopScopeLimit(LexicalContext lc) {
        return lc.getTryNodeForInlinedFinally(this.getLabelName());
    }

    @Override
    Label getTargetLabel(BreakableNode target) {
        assert (target != null);
        return ((Block)target).getEntryLabel();
    }
}

