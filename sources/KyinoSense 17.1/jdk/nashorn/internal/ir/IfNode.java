/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class IfNode
extends Statement
implements JoinPredecessor {
    private static final long serialVersionUID = 1L;
    private final Expression test;
    private final Block pass;
    private final Block fail;
    private final LocalVariableConversion conversion;

    public IfNode(int lineNumber, long token, int finish, Expression test, Block pass, Block fail) {
        super(lineNumber, token, finish);
        this.test = test;
        this.pass = pass;
        this.fail = fail;
        this.conversion = null;
    }

    private IfNode(IfNode ifNode, Expression test, Block pass, Block fail, LocalVariableConversion conversion) {
        super(ifNode);
        this.test = test;
        this.pass = pass;
        this.fail = fail;
        this.conversion = conversion;
    }

    @Override
    public boolean isTerminal() {
        return this.pass.isTerminal() && this.fail != null && this.fail.isTerminal();
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterIfNode(this)) {
            return visitor.leaveIfNode(this.setTest((Expression)this.test.accept(visitor)).setPass((Block)this.pass.accept(visitor)).setFail(this.fail == null ? null : (Block)this.fail.accept(visitor)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printTypes) {
        sb.append("if (");
        this.test.toString(sb, printTypes);
        sb.append(')');
    }

    public Block getFail() {
        return this.fail;
    }

    private IfNode setFail(Block fail) {
        if (this.fail == fail) {
            return this;
        }
        return new IfNode(this, this.test, this.pass, fail, this.conversion);
    }

    public Block getPass() {
        return this.pass;
    }

    private IfNode setPass(Block pass) {
        if (this.pass == pass) {
            return this;
        }
        return new IfNode(this, this.test, pass, this.fail, this.conversion);
    }

    public Expression getTest() {
        return this.test;
    }

    public IfNode setTest(Expression test) {
        if (this.test == test) {
            return this;
        }
        return new IfNode(this, test, this.pass, this.fail, this.conversion);
    }

    @Override
    public IfNode setLocalVariableConversion(LexicalContext lc, LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new IfNode(this, this.test, this.pass, this.fail, conversion);
    }

    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}

