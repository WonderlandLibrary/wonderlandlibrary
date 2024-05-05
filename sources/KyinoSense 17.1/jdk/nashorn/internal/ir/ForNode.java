/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class ForNode
extends LoopNode {
    private static final long serialVersionUID = 1L;
    private final Expression init;
    private final JoinPredecessorExpression modify;
    private final Symbol iterator;
    public static final int IS_FOR_IN = 1;
    public static final int IS_FOR_EACH = 2;
    public static final int PER_ITERATION_SCOPE = 4;
    private final int flags;

    public ForNode(int lineNumber, long token, int finish, Block body, int flags) {
        super(lineNumber, token, finish, body, false);
        this.flags = flags;
        this.init = null;
        this.modify = null;
        this.iterator = null;
    }

    private ForNode(ForNode forNode, Expression init, JoinPredecessorExpression test, Block body, JoinPredecessorExpression modify, int flags, boolean controlFlowEscapes, LocalVariableConversion conversion, Symbol iterator2) {
        super(forNode, test, body, controlFlowEscapes, conversion);
        this.init = init;
        this.modify = modify;
        this.flags = flags;
        this.iterator = iterator2;
    }

    @Override
    public Node ensureUniqueLabels(LexicalContext lc) {
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }

    @Override
    public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterForNode(this)) {
            return visitor.leaveForNode(this.setInit(lc, this.init == null ? null : (Expression)this.init.accept(visitor)).setTest(lc, this.test == null ? null : (JoinPredecessorExpression)this.test.accept(visitor)).setModify(lc, this.modify == null ? null : (JoinPredecessorExpression)this.modify.accept(visitor)).setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printTypes) {
        sb.append("for");
        LocalVariableConversion.toString(this.conversion, sb).append(' ');
        if (this.isForIn()) {
            this.init.toString(sb, printTypes);
            sb.append(" in ");
            this.modify.toString(sb, printTypes);
        } else {
            if (this.init != null) {
                this.init.toString(sb, printTypes);
            }
            sb.append("; ");
            if (this.test != null) {
                this.test.toString(sb, printTypes);
            }
            sb.append("; ");
            if (this.modify != null) {
                this.modify.toString(sb, printTypes);
            }
        }
        sb.append(')');
    }

    @Override
    public boolean hasGoto() {
        return !this.isForIn() && this.test == null;
    }

    @Override
    public boolean mustEnter() {
        if (this.isForIn()) {
            return false;
        }
        return this.test == null;
    }

    public Expression getInit() {
        return this.init;
    }

    public ForNode setInit(LexicalContext lc, Expression init) {
        if (this.init == init) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }

    public boolean isForIn() {
        return (this.flags & 1) != 0;
    }

    public ForNode setIsForIn(LexicalContext lc) {
        return this.setFlags(lc, this.flags | 1);
    }

    public boolean isForEach() {
        return (this.flags & 2) != 0;
    }

    public ForNode setIsForEach(LexicalContext lc) {
        return this.setFlags(lc, this.flags | 2);
    }

    public Symbol getIterator() {
        return this.iterator;
    }

    public ForNode setIterator(LexicalContext lc, Symbol iterator2) {
        if (this.iterator == iterator2) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, iterator2));
    }

    public JoinPredecessorExpression getModify() {
        return this.modify;
    }

    public ForNode setModify(LexicalContext lc, JoinPredecessorExpression modify) {
        if (this.modify == modify) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }

    @Override
    public ForNode setTest(LexicalContext lc, JoinPredecessorExpression test) {
        if (this.test == test) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, test, this.body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }

    @Override
    public Block getBody() {
        return this.body;
    }

    @Override
    public ForNode setBody(LexicalContext lc, Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, body, this.modify, this.flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }

    @Override
    public ForNode setControlFlowEscapes(LexicalContext lc, boolean controlFlowEscapes) {
        if (this.controlFlowEscapes == controlFlowEscapes) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, controlFlowEscapes, this.conversion, this.iterator));
    }

    private ForNode setFlags(LexicalContext lc, int flags) {
        if (this.flags == flags) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, flags, this.controlFlowEscapes, this.conversion, this.iterator));
    }

    @Override
    JoinPredecessor setLocalVariableConversionChanged(LexicalContext lc, LocalVariableConversion conversion) {
        return Node.replaceInLexicalContext(lc, this, new ForNode(this, this.init, this.test, this.body, this.modify, this.flags, this.controlFlowEscapes, conversion, this.iterator));
    }

    @Override
    public boolean hasPerIterationScope() {
        return (this.flags & 4) != 0;
    }

    public ForNode setPerIterationScope(LexicalContext lc) {
        return this.setFlags(lc, this.flags | 4);
    }
}

