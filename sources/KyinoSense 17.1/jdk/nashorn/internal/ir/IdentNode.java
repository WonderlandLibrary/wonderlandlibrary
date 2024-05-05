/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionCall;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.PropertyKey;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;

@Immutable
public final class IdentNode
extends Expression
implements PropertyKey,
FunctionCall,
Optimistic,
JoinPredecessor {
    private static final long serialVersionUID = 1L;
    private static final int PROPERTY_NAME = 1;
    private static final int INITIALIZED_HERE = 2;
    private static final int FUNCTION = 4;
    private static final int FUTURESTRICT_NAME = 8;
    private static final int IS_DECLARED_HERE = 16;
    private static final int IS_DEAD = 32;
    private final String name;
    private final Type type;
    private final int flags;
    private final int programPoint;
    private final LocalVariableConversion conversion;
    private Symbol symbol;

    public IdentNode(long token, int finish, String name) {
        super(token, finish);
        this.name = name;
        this.type = null;
        this.flags = 0;
        this.programPoint = -1;
        this.conversion = null;
    }

    private IdentNode(IdentNode identNode, String name, Type type, int flags, int programPoint, LocalVariableConversion conversion) {
        super(identNode);
        this.name = name;
        this.type = type;
        this.flags = flags;
        this.programPoint = programPoint;
        this.conversion = conversion;
        this.symbol = identNode.symbol;
    }

    public IdentNode(IdentNode identNode) {
        super(identNode);
        this.name = identNode.getName();
        this.type = identNode.type;
        this.flags = identNode.flags;
        this.conversion = identNode.conversion;
        this.programPoint = -1;
        this.symbol = identNode.symbol;
    }

    public static IdentNode createInternalIdentifier(Symbol symbol) {
        return new IdentNode(Token.toDesc(TokenType.IDENT, 0, 0), 0, symbol.getName()).setSymbol(symbol);
    }

    @Override
    public Type getType() {
        if (this.type != null) {
            return this.type;
        }
        if (this.symbol != null && this.symbol.isScope()) {
            return Type.OBJECT;
        }
        return Type.UNDEFINED;
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterIdentNode(this)) {
            return visitor.leaveIdentNode(this);
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        if (printType) {
            this.optimisticTypeToString(sb, this.symbol == null || !this.symbol.hasSlot());
        }
        sb.append(this.name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getPropertyName() {
        return this.getName();
    }

    @Override
    public boolean isLocal() {
        return !this.getSymbol().isScope();
    }

    public Symbol getSymbol() {
        return this.symbol;
    }

    public IdentNode setSymbol(Symbol symbol) {
        if (this.symbol == symbol) {
            return this;
        }
        IdentNode newIdent = (IdentNode)this.clone();
        newIdent.symbol = symbol;
        return newIdent;
    }

    public boolean isPropertyName() {
        return (this.flags & 1) == 1;
    }

    public IdentNode setIsPropertyName() {
        if (this.isPropertyName()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 1, this.programPoint, this.conversion);
    }

    public boolean isFutureStrictName() {
        return (this.flags & 8) == 8;
    }

    public IdentNode setIsFutureStrictName() {
        if (this.isFutureStrictName()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 8, this.programPoint, this.conversion);
    }

    public boolean isInitializedHere() {
        return (this.flags & 2) == 2;
    }

    public IdentNode setIsInitializedHere() {
        if (this.isInitializedHere()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 2, this.programPoint, this.conversion);
    }

    public boolean isDead() {
        return (this.flags & 0x20) != 0;
    }

    public IdentNode markDead() {
        return new IdentNode(this, this.name, this.type, this.flags | 0x20, this.programPoint, this.conversion);
    }

    public boolean isDeclaredHere() {
        return (this.flags & 0x10) != 0;
    }

    public IdentNode setIsDeclaredHere() {
        if (this.isDeclaredHere()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 0x10, this.programPoint, this.conversion);
    }

    public boolean isCompileTimePropertyName() {
        return this.name.equals(CompilerConstants.__DIR__.symbolName()) || this.name.equals(CompilerConstants.__FILE__.symbolName()) || this.name.equals(CompilerConstants.__LINE__.symbolName());
    }

    @Override
    public boolean isFunction() {
        return (this.flags & 4) == 4;
    }

    @Override
    public IdentNode setType(Type type) {
        if (this.type == type) {
            return this;
        }
        return new IdentNode(this, this.name, type, this.flags, this.programPoint, this.conversion);
    }

    public IdentNode setIsFunction() {
        if (this.isFunction()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags | 4, this.programPoint, this.conversion);
    }

    public IdentNode setIsNotFunction() {
        if (!this.isFunction()) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags & 0xFFFFFFFB, this.programPoint, this.conversion);
    }

    @Override
    public int getProgramPoint() {
        return this.programPoint;
    }

    @Override
    public Optimistic setProgramPoint(int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags, programPoint, this.conversion);
    }

    @Override
    public Type getMostOptimisticType() {
        return Type.INT;
    }

    @Override
    public Type getMostPessimisticType() {
        return Type.OBJECT;
    }

    @Override
    public boolean canBeOptimistic() {
        return true;
    }

    @Override
    public JoinPredecessor setLocalVariableConversion(LexicalContext lc, LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new IdentNode(this, this.name, this.type, this.flags, this.programPoint, conversion);
    }

    public boolean isInternal() {
        assert (this.name != null);
        return this.name.charAt(0) == ':';
    }

    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}

