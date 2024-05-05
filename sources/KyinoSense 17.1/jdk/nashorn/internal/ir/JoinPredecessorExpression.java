/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

public class JoinPredecessorExpression
extends Expression
implements JoinPredecessor {
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final LocalVariableConversion conversion;

    public JoinPredecessorExpression() {
        this(null);
    }

    public JoinPredecessorExpression(Expression expression) {
        this(expression, null);
    }

    private JoinPredecessorExpression(Expression expression, LocalVariableConversion conversion) {
        super(expression == null ? 0L : expression.getToken(), expression == null ? 0 : expression.getStart(), expression == null ? 0 : expression.getFinish());
        this.expression = expression;
        this.conversion = conversion;
    }

    @Override
    public JoinPredecessor setLocalVariableConversion(LexicalContext lc, LocalVariableConversion conversion) {
        if (conversion == this.conversion) {
            return this;
        }
        return new JoinPredecessorExpression(this.expression, conversion);
    }

    @Override
    public Type getType() {
        return this.expression.getType();
    }

    @Override
    public boolean isAlwaysFalse() {
        return this.expression != null && this.expression.isAlwaysFalse();
    }

    @Override
    public boolean isAlwaysTrue() {
        return this.expression != null && this.expression.isAlwaysTrue();
    }

    public Expression getExpression() {
        return this.expression;
    }

    public JoinPredecessorExpression setExpression(Expression expression) {
        if (expression == this.expression) {
            return this;
        }
        return new JoinPredecessorExpression(expression, this.conversion);
    }

    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterJoinPredecessorExpression(this)) {
            Expression expr = this.getExpression();
            return visitor.leaveJoinPredecessorExpression(expr == null ? this : this.setExpression((Expression)expr.accept(visitor)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        if (this.expression != null) {
            this.expression.toString(sb, printType);
        }
        if (this.conversion != null) {
            this.conversion.toString(sb);
        }
    }
}

