/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class WhileNode
extends LoopNode {
    private static final long serialVersionUID = 1L;
    private final boolean isDoWhile;

    public WhileNode(int lineNumber, long token, int finish, boolean isDoWhile) {
        super(lineNumber, token, finish, null, false);
        this.isDoWhile = isDoWhile;
    }

    private WhileNode(WhileNode whileNode, JoinPredecessorExpression test, Block body, boolean controlFlowEscapes, LocalVariableConversion conversion) {
        super(whileNode, test, body, controlFlowEscapes, conversion);
        this.isDoWhile = whileNode.isDoWhile;
    }

    @Override
    public Node ensureUniqueLabels(LexicalContext lc) {
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, this.body, this.controlFlowEscapes, this.conversion));
    }

    @Override
    public boolean hasGoto() {
        return this.test == null;
    }

    @Override
    public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterWhileNode(this)) {
            if (this.isDoWhile()) {
                return visitor.leaveWhileNode(this.setBody(lc, (Block)this.body.accept(visitor)).setTest(lc, (JoinPredecessorExpression)this.test.accept(visitor)));
            }
            return visitor.leaveWhileNode(this.setTest(lc, (JoinPredecessorExpression)this.test.accept(visitor)).setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }

    @Override
    public WhileNode setTest(LexicalContext lc, JoinPredecessorExpression test) {
        if (this.test == test) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, test, this.body, this.controlFlowEscapes, this.conversion));
    }

    @Override
    public Block getBody() {
        return this.body;
    }

    @Override
    public WhileNode setBody(LexicalContext lc, Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, body, this.controlFlowEscapes, this.conversion));
    }

    @Override
    public WhileNode setControlFlowEscapes(LexicalContext lc, boolean controlFlowEscapes) {
        if (this.controlFlowEscapes == controlFlowEscapes) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, this.body, controlFlowEscapes, this.conversion));
    }

    @Override
    JoinPredecessor setLocalVariableConversionChanged(LexicalContext lc, LocalVariableConversion conversion) {
        return Node.replaceInLexicalContext(lc, this, new WhileNode(this, this.test, this.body, this.controlFlowEscapes, conversion));
    }

    public boolean isDoWhile() {
        return this.isDoWhile;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        sb.append("while (");
        this.test.toString(sb, printType);
        sb.append(')');
    }

    @Override
    public boolean mustEnter() {
        if (this.isDoWhile()) {
            return true;
        }
        return this.test == null;
    }

    @Override
    public boolean hasPerIterationScope() {
        return false;
    }
}

