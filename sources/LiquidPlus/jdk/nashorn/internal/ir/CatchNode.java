/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class CatchNode
extends Statement {
    private static final long serialVersionUID = 1L;
    private final IdentNode exception;
    private final Expression exceptionCondition;
    private final Block body;
    private final boolean isSyntheticRethrow;

    public CatchNode(int lineNumber, long token, int finish, IdentNode exception, Expression exceptionCondition, Block body, boolean isSyntheticRethrow) {
        super(lineNumber, token, finish);
        this.exception = exception == null ? null : exception.setIsInitializedHere();
        this.exceptionCondition = exceptionCondition;
        this.body = body;
        this.isSyntheticRethrow = isSyntheticRethrow;
    }

    private CatchNode(CatchNode catchNode, IdentNode exception, Expression exceptionCondition, Block body, boolean isSyntheticRethrow) {
        super(catchNode);
        this.exception = exception;
        this.exceptionCondition = exceptionCondition;
        this.body = body;
        this.isSyntheticRethrow = isSyntheticRethrow;
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterCatchNode(this)) {
            return visitor.leaveCatchNode(this.setException((IdentNode)this.exception.accept(visitor)).setExceptionCondition(this.exceptionCondition == null ? null : (Expression)this.exceptionCondition.accept(visitor)).setBody((Block)this.body.accept(visitor)));
        }
        return this;
    }

    @Override
    public boolean isTerminal() {
        return this.body.isTerminal();
    }

    @Override
    public void toString(StringBuilder sb, boolean printTypes) {
        sb.append(" catch (");
        this.exception.toString(sb, printTypes);
        if (this.exceptionCondition != null) {
            sb.append(" if ");
            this.exceptionCondition.toString(sb, printTypes);
        }
        sb.append(')');
    }

    public IdentNode getException() {
        return this.exception;
    }

    public Expression getExceptionCondition() {
        return this.exceptionCondition;
    }

    public CatchNode setExceptionCondition(Expression exceptionCondition) {
        if (this.exceptionCondition == exceptionCondition) {
            return this;
        }
        return new CatchNode(this, this.exception, exceptionCondition, this.body, this.isSyntheticRethrow);
    }

    public Block getBody() {
        return this.body;
    }

    public CatchNode setException(IdentNode exception) {
        if (this.exception == exception) {
            return this;
        }
        return new CatchNode(this, exception, this.exceptionCondition, this.body, this.isSyntheticRethrow);
    }

    private CatchNode setBody(Block body) {
        if (this.body == body) {
            return this;
        }
        return new CatchNode(this, this.exception, this.exceptionCondition, body, this.isSyntheticRethrow);
    }

    public boolean isSyntheticRethrow() {
        return this.isSyntheticRethrow;
    }
}

