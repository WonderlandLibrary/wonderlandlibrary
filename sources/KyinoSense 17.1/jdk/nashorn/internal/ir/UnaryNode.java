/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Assignment;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;

@Immutable
public final class UnaryNode
extends Expression
implements Assignment<Expression>,
Optimistic {
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final int programPoint;
    private final Type type;
    @Ignore
    private static final List<TokenType> CAN_OVERFLOW = Collections.unmodifiableList(Arrays.asList(TokenType.ADD, TokenType.SUB, TokenType.DECPREFIX, TokenType.DECPOSTFIX, TokenType.INCPREFIX, TokenType.INCPOSTFIX));

    public UnaryNode(long token, Expression rhs) {
        this(token, Math.min(rhs.getStart(), Token.descPosition(token)), Math.max(Token.descPosition(token) + Token.descLength(token), rhs.getFinish()), rhs);
    }

    public UnaryNode(long token, int start, int finish, Expression expression) {
        super(token, start, finish);
        this.expression = expression;
        this.programPoint = -1;
        this.type = null;
    }

    private UnaryNode(UnaryNode unaryNode, Expression expression, Type type, int programPoint) {
        super(unaryNode);
        this.expression = expression;
        this.programPoint = programPoint;
        this.type = type;
    }

    @Override
    public boolean isAssignment() {
        switch (this.tokenType()) {
            case DECPOSTFIX: 
            case DECPREFIX: 
            case INCPOSTFIX: 
            case INCPREFIX: {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSelfModifying() {
        return this.isAssignment();
    }

    @Override
    public Type getWidestOperationType() {
        switch (this.tokenType()) {
            case ADD: {
                Type operandType = this.getExpression().getType();
                if (operandType == Type.BOOLEAN) {
                    return Type.INT;
                }
                if (operandType.isObject()) {
                    return Type.NUMBER;
                }
                assert (operandType.isNumeric());
                return operandType;
            }
            case SUB: {
                return Type.NUMBER;
            }
            case NOT: 
            case DELETE: {
                return Type.BOOLEAN;
            }
            case BIT_NOT: {
                return Type.INT;
            }
            case VOID: {
                return Type.UNDEFINED;
            }
        }
        return this.isAssignment() ? Type.NUMBER : Type.OBJECT;
    }

    @Override
    public Expression getAssignmentDest() {
        return this.isAssignment() ? this.getExpression() : null;
    }

    public UnaryNode setAssignmentDest(Expression n) {
        return this.setExpression(n);
    }

    @Override
    public Expression getAssignmentSource() {
        return this.getAssignmentDest();
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterUnaryNode(this)) {
            return visitor.leaveUnaryNode(this.setExpression((Expression)this.expression.accept(visitor)));
        }
        return this;
    }

    @Override
    public boolean isLocal() {
        switch (this.tokenType()) {
            case NEW: {
                return false;
            }
            case ADD: 
            case SUB: 
            case NOT: 
            case BIT_NOT: {
                return this.expression.isLocal() && this.expression.getType().isJSPrimitive();
            }
            case DECPOSTFIX: 
            case DECPREFIX: 
            case INCPOSTFIX: 
            case INCPREFIX: {
                return this.expression instanceof IdentNode && this.expression.isLocal() && this.expression.getType().isJSPrimitive();
            }
        }
        return this.expression.isLocal();
    }

    @Override
    public void toString(final StringBuilder sb, final boolean printType) {
        this.toString(sb, new Runnable(){

            @Override
            public void run() {
                UnaryNode.this.getExpression().toString(sb, printType);
            }
        }, printType);
    }

    public void toString(StringBuilder sb, Runnable rhsStringBuilder, boolean printType) {
        boolean isPostfix;
        TokenType tokenType = this.tokenType();
        String name = tokenType.getName();
        boolean bl = isPostfix = tokenType == TokenType.DECPOSTFIX || tokenType == TokenType.INCPOSTFIX;
        if (this.isOptimistic()) {
            sb.append("%");
        }
        boolean rhsParen = tokenType.needsParens(this.getExpression().tokenType(), false);
        if (!isPostfix) {
            if (name == null) {
                sb.append(tokenType.name());
                rhsParen = true;
            } else {
                sb.append(name);
                if (tokenType.ordinal() > TokenType.BIT_NOT.ordinal()) {
                    sb.append(' ');
                }
            }
        }
        if (rhsParen) {
            sb.append('(');
        }
        rhsStringBuilder.run();
        if (rhsParen) {
            sb.append(')');
        }
        if (isPostfix) {
            sb.append(tokenType == TokenType.DECPOSTFIX ? "--" : "++");
        }
    }

    public Expression getExpression() {
        return this.expression;
    }

    public UnaryNode setExpression(Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return new UnaryNode(this, expression, this.type, this.programPoint);
    }

    @Override
    public int getProgramPoint() {
        return this.programPoint;
    }

    @Override
    public UnaryNode setProgramPoint(int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new UnaryNode(this, this.expression, this.type, programPoint);
    }

    @Override
    public boolean canBeOptimistic() {
        return this.getMostOptimisticType() != this.getMostPessimisticType();
    }

    @Override
    public Type getMostOptimisticType() {
        if (CAN_OVERFLOW.contains((Object)this.tokenType())) {
            return Type.INT;
        }
        return this.getMostPessimisticType();
    }

    @Override
    public Type getMostPessimisticType() {
        return this.getWidestOperationType();
    }

    @Override
    public Type getType() {
        Type widest = this.getWidestOperationType();
        if (this.type == null) {
            return widest;
        }
        return Type.narrowest(widest, Type.widest(this.type, this.expression.getType()));
    }

    @Override
    public UnaryNode setType(Type type) {
        if (this.type == type) {
            return this;
        }
        return new UnaryNode(this, this.expression, type, this.programPoint);
    }
}

