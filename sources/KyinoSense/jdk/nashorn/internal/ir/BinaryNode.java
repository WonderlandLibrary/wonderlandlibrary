/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Assignment;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.TokenType;

@Immutable
public final class BinaryNode
extends Expression
implements Assignment<Expression>,
Optimistic {
    private static final long serialVersionUID = 1L;
    private static final Type OPTIMISTIC_UNDECIDED_TYPE = Type.typeFor(new Object(){}.getClass());
    private final Expression lhs;
    private final Expression rhs;
    private final int programPoint;
    private final Type type;
    private transient Type cachedType;
    @Ignore
    private static final Set<TokenType> CAN_OVERFLOW = Collections.unmodifiableSet(new HashSet<TokenType>(Arrays.asList(TokenType.ADD, TokenType.DIV, TokenType.MOD, TokenType.MUL, TokenType.SUB, TokenType.ASSIGN_ADD, TokenType.ASSIGN_DIV, TokenType.ASSIGN_MOD, TokenType.ASSIGN_MUL, TokenType.ASSIGN_SUB, TokenType.SHR, TokenType.ASSIGN_SHR)));

    public BinaryNode(long token, Expression lhs, Expression rhs) {
        super(token, lhs.getStart(), rhs.getFinish());
        assert (!this.isTokenType(TokenType.AND) && !this.isTokenType(TokenType.OR) || lhs instanceof JoinPredecessorExpression);
        this.lhs = lhs;
        this.rhs = rhs;
        this.programPoint = -1;
        this.type = null;
    }

    private BinaryNode(BinaryNode binaryNode, Expression lhs, Expression rhs, Type type, int programPoint) {
        super(binaryNode);
        this.lhs = lhs;
        this.rhs = rhs;
        this.programPoint = programPoint;
        this.type = type;
    }

    public boolean isComparison() {
        switch (this.tokenType()) {
            case EQ: 
            case EQ_STRICT: 
            case NE: 
            case NE_STRICT: 
            case LE: 
            case LT: 
            case GE: 
            case GT: {
                return true;
            }
        }
        return false;
    }

    public boolean isRelational() {
        switch (this.tokenType()) {
            case LE: 
            case LT: 
            case GE: 
            case GT: {
                return true;
            }
        }
        return false;
    }

    public boolean isLogical() {
        return BinaryNode.isLogical(this.tokenType());
    }

    public static boolean isLogical(TokenType tokenType) {
        switch (tokenType) {
            case AND: 
            case OR: {
                return true;
            }
        }
        return false;
    }

    public Type getWidestOperandType() {
        switch (this.tokenType()) {
            case SHR: 
            case ASSIGN_SHR: {
                return Type.INT;
            }
            case INSTANCEOF: {
                return Type.OBJECT;
            }
        }
        if (this.isComparison()) {
            return Type.OBJECT;
        }
        return this.getWidestOperationType();
    }

    @Override
    public Type getWidestOperationType() {
        switch (this.tokenType()) {
            case ADD: 
            case ASSIGN_ADD: {
                Type lhsType = this.lhs.getType();
                Type rhsType = this.rhs.getType();
                if (lhsType == Type.BOOLEAN && rhsType == Type.BOOLEAN) {
                    return Type.INT;
                }
                if (BinaryNode.isString(lhsType) || BinaryNode.isString(rhsType)) {
                    return Type.CHARSEQUENCE;
                }
                Type widestOperandType = Type.widest(BinaryNode.undefinedToNumber(BinaryNode.booleanToInt(lhsType)), BinaryNode.undefinedToNumber(BinaryNode.booleanToInt(rhsType)));
                if (widestOperandType.isNumeric()) {
                    return Type.NUMBER;
                }
                return Type.OBJECT;
            }
            case SHR: 
            case ASSIGN_SHR: {
                return Type.NUMBER;
            }
            case ASSIGN_SAR: 
            case ASSIGN_SHL: 
            case BIT_AND: 
            case BIT_OR: 
            case BIT_XOR: 
            case ASSIGN_BIT_AND: 
            case ASSIGN_BIT_OR: 
            case ASSIGN_BIT_XOR: 
            case SAR: 
            case SHL: {
                return Type.INT;
            }
            case DIV: 
            case MOD: 
            case ASSIGN_DIV: 
            case ASSIGN_MOD: {
                return Type.NUMBER;
            }
            case MUL: 
            case SUB: 
            case ASSIGN_MUL: 
            case ASSIGN_SUB: {
                Type lhsType = this.lhs.getType();
                Type rhsType = this.rhs.getType();
                if (lhsType == Type.BOOLEAN && rhsType == Type.BOOLEAN) {
                    return Type.INT;
                }
                return Type.NUMBER;
            }
            case VOID: {
                return Type.UNDEFINED;
            }
            case ASSIGN: {
                return this.rhs.getType();
            }
            case INSTANCEOF: {
                return Type.BOOLEAN;
            }
            case COMMALEFT: {
                return this.lhs.getType();
            }
            case COMMARIGHT: {
                return this.rhs.getType();
            }
            case AND: 
            case OR: {
                return Type.widestReturnType(this.lhs.getType(), this.rhs.getType());
            }
        }
        if (this.isComparison()) {
            return Type.BOOLEAN;
        }
        return Type.OBJECT;
    }

    private static boolean isString(Type type) {
        return type == Type.STRING || type == Type.CHARSEQUENCE;
    }

    private static Type booleanToInt(Type type) {
        return type == Type.BOOLEAN ? Type.INT : type;
    }

    private static Type undefinedToNumber(Type type) {
        return type == Type.UNDEFINED ? Type.NUMBER : type;
    }

    @Override
    public boolean isAssignment() {
        switch (this.tokenType()) {
            case ASSIGN_SHR: 
            case ASSIGN_ADD: 
            case ASSIGN_SAR: 
            case ASSIGN_SHL: 
            case ASSIGN_BIT_AND: 
            case ASSIGN_BIT_OR: 
            case ASSIGN_BIT_XOR: 
            case ASSIGN_DIV: 
            case ASSIGN_MOD: 
            case ASSIGN_MUL: 
            case ASSIGN_SUB: 
            case ASSIGN: {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSelfModifying() {
        return this.isAssignment() && !this.isTokenType(TokenType.ASSIGN);
    }

    @Override
    public Expression getAssignmentDest() {
        return this.isAssignment() ? this.lhs() : null;
    }

    public BinaryNode setAssignmentDest(Expression n) {
        return this.setLHS(n);
    }

    @Override
    public Expression getAssignmentSource() {
        return this.rhs();
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterBinaryNode(this)) {
            return visitor.leaveBinaryNode(this.setLHS((Expression)this.lhs.accept(visitor)).setRHS((Expression)this.rhs.accept(visitor)));
        }
        return this;
    }

    @Override
    public boolean isLocal() {
        switch (this.tokenType()) {
            case SHR: 
            case ADD: 
            case BIT_AND: 
            case BIT_OR: 
            case BIT_XOR: 
            case SAR: 
            case SHL: 
            case DIV: 
            case MOD: 
            case MUL: 
            case SUB: {
                return this.lhs.isLocal() && this.lhs.getType().isJSPrimitive() && this.rhs.isLocal() && this.rhs.getType().isJSPrimitive();
            }
            case ASSIGN_SHR: 
            case ASSIGN_ADD: 
            case ASSIGN_SAR: 
            case ASSIGN_SHL: 
            case ASSIGN_BIT_AND: 
            case ASSIGN_BIT_OR: 
            case ASSIGN_BIT_XOR: 
            case ASSIGN_DIV: 
            case ASSIGN_MOD: 
            case ASSIGN_MUL: 
            case ASSIGN_SUB: {
                return this.lhs instanceof IdentNode && this.lhs.isLocal() && this.lhs.getType().isJSPrimitive() && this.rhs.isLocal() && this.rhs.getType().isJSPrimitive();
            }
            case ASSIGN: {
                return this.lhs instanceof IdentNode && this.lhs.isLocal() && this.rhs.isLocal();
            }
        }
        return false;
    }

    @Override
    public boolean isAlwaysFalse() {
        switch (this.tokenType()) {
            case COMMALEFT: {
                return this.lhs.isAlwaysFalse();
            }
            case COMMARIGHT: {
                return this.rhs.isAlwaysFalse();
            }
        }
        return false;
    }

    @Override
    public boolean isAlwaysTrue() {
        switch (this.tokenType()) {
            case COMMALEFT: {
                return this.lhs.isAlwaysTrue();
            }
            case COMMARIGHT: {
                return this.rhs.isAlwaysTrue();
            }
        }
        return false;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        TokenType tokenType = this.tokenType();
        boolean lhsParen = tokenType.needsParens(this.lhs().tokenType(), true);
        boolean rhsParen = tokenType.needsParens(this.rhs().tokenType(), false);
        if (lhsParen) {
            sb.append('(');
        }
        this.lhs().toString(sb, printType);
        if (lhsParen) {
            sb.append(')');
        }
        sb.append(' ');
        switch (tokenType) {
            case COMMALEFT: {
                sb.append(",<");
                break;
            }
            case COMMARIGHT: {
                sb.append(",>");
                break;
            }
            case INCPREFIX: 
            case DECPREFIX: {
                sb.append("++");
                break;
            }
            default: {
                sb.append(tokenType.getName());
            }
        }
        if (this.isOptimistic()) {
            sb.append("%");
        }
        sb.append(' ');
        if (rhsParen) {
            sb.append('(');
        }
        this.rhs().toString(sb, printType);
        if (rhsParen) {
            sb.append(')');
        }
    }

    public Expression lhs() {
        return this.lhs;
    }

    public Expression rhs() {
        return this.rhs;
    }

    public BinaryNode setLHS(Expression lhs) {
        if (this.lhs == lhs) {
            return this;
        }
        return new BinaryNode(this, lhs, this.rhs, this.type, this.programPoint);
    }

    public BinaryNode setRHS(Expression rhs) {
        if (this.rhs == rhs) {
            return this;
        }
        return new BinaryNode(this, this.lhs, rhs, this.type, this.programPoint);
    }

    public BinaryNode setOperands(Expression lhs, Expression rhs) {
        if (this.lhs == lhs && this.rhs == rhs) {
            return this;
        }
        return new BinaryNode(this, lhs, rhs, this.type, this.programPoint);
    }

    @Override
    public int getProgramPoint() {
        return this.programPoint;
    }

    @Override
    public boolean canBeOptimistic() {
        return this.isTokenType(TokenType.ADD) || this.getMostOptimisticType() != this.getMostPessimisticType();
    }

    @Override
    public BinaryNode setProgramPoint(int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new BinaryNode(this, this.lhs, this.rhs, this.type, programPoint);
    }

    @Override
    public Type getMostOptimisticType() {
        TokenType tokenType = this.tokenType();
        if (tokenType == TokenType.ADD || tokenType == TokenType.ASSIGN_ADD) {
            return OPTIMISTIC_UNDECIDED_TYPE;
        }
        if (CAN_OVERFLOW.contains((Object)tokenType)) {
            return Type.INT;
        }
        return this.getMostPessimisticType();
    }

    @Override
    public Type getMostPessimisticType() {
        return this.getWidestOperationType();
    }

    public boolean isOptimisticUndecidedType() {
        return this.type == OPTIMISTIC_UNDECIDED_TYPE;
    }

    @Override
    public Type getType() {
        if (this.cachedType == null) {
            this.cachedType = this.getTypeUncached();
        }
        return this.cachedType;
    }

    private Type getTypeUncached() {
        if (this.type == OPTIMISTIC_UNDECIDED_TYPE) {
            return BinaryNode.decideType(this.lhs.getType(), this.rhs.getType());
        }
        Type widest = this.getWidestOperationType();
        if (this.type == null) {
            return widest;
        }
        if (this.tokenType() == TokenType.ASSIGN_SHR || this.tokenType() == TokenType.SHR) {
            return this.type;
        }
        return Type.narrowest(widest, Type.widest(this.type, Type.widest(this.lhs.getType(), this.rhs.getType())));
    }

    private static Type decideType(Type lhsType, Type rhsType) {
        if (BinaryNode.isString(lhsType) || BinaryNode.isString(rhsType)) {
            return Type.CHARSEQUENCE;
        }
        Type widest = Type.widest(BinaryNode.undefinedToNumber(BinaryNode.booleanToInt(lhsType)), BinaryNode.undefinedToNumber(BinaryNode.booleanToInt(rhsType)));
        return widest.isObject() ? Type.OBJECT : widest;
    }

    public BinaryNode decideType() {
        assert (this.type == OPTIMISTIC_UNDECIDED_TYPE);
        return this.setType(BinaryNode.decideType(this.lhs.getType(), this.rhs.getType()));
    }

    @Override
    public BinaryNode setType(Type type) {
        if (this.type == type) {
            return this;
        }
        return new BinaryNode(this, this.lhs, this.rhs, type, this.programPoint);
    }
}

