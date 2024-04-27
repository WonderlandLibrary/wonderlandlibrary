/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextStatement;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class WithNode
extends LexicalContextStatement {
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final Block body;

    public WithNode(int lineNumber, long token, int finish) {
        super(lineNumber, token, finish);
        this.expression = null;
        this.body = null;
    }

    private WithNode(WithNode node, Expression expression, Block body) {
        super(node);
        this.expression = expression;
        this.body = body;
    }

    @Override
    public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterWithNode(this)) {
            return visitor.leaveWithNode(this.setExpression(lc, (Expression)this.expression.accept(visitor)).setBody(lc, (Block)this.body.accept(visitor)));
        }
        return this;
    }

    @Override
    public boolean isTerminal() {
        return this.body.isTerminal();
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        sb.append("with (");
        this.expression.toString(sb, printType);
        sb.append(')');
    }

    public Block getBody() {
        return this.body;
    }

    public WithNode setBody(LexicalContext lc, Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WithNode(this, this.expression, body));
    }

    public Expression getExpression() {
        return this.expression;
    }

    public WithNode setExpression(LexicalContext lc, Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new WithNode(this, expression, this.body));
    }
}

