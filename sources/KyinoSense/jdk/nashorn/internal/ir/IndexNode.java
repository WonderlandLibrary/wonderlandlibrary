/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.BaseNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class IndexNode
extends BaseNode {
    private static final long serialVersionUID = 1L;
    private final Expression index;

    public IndexNode(long token, int finish, Expression base, Expression index) {
        super(token, finish, base, false);
        this.index = index;
    }

    private IndexNode(IndexNode indexNode, Expression base, Expression index, boolean isFunction, Type type, int programPoint) {
        super(indexNode, base, isFunction, type, programPoint);
        this.index = index;
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterIndexNode(this)) {
            return visitor.leaveIndexNode(this.setBase((Expression)this.base.accept(visitor)).setIndex((Expression)this.index.accept(visitor)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        boolean needsParen = this.tokenType().needsParens(this.base.tokenType(), true);
        if (needsParen) {
            sb.append('(');
        }
        if (printType) {
            this.optimisticTypeToString(sb);
        }
        this.base.toString(sb, printType);
        if (needsParen) {
            sb.append(')');
        }
        sb.append('[');
        this.index.toString(sb, printType);
        sb.append(']');
    }

    public Expression getIndex() {
        return this.index;
    }

    private IndexNode setBase(Expression base) {
        if (this.base == base) {
            return this;
        }
        return new IndexNode(this, base, this.index, this.isFunction(), this.type, this.programPoint);
    }

    public IndexNode setIndex(Expression index) {
        if (this.index == index) {
            return this;
        }
        return new IndexNode(this, this.base, index, this.isFunction(), this.type, this.programPoint);
    }

    @Override
    public IndexNode setType(Type type) {
        if (this.type == type) {
            return this;
        }
        return new IndexNode(this, this.base, this.index, this.isFunction(), type, this.programPoint);
    }

    @Override
    public IndexNode setIsFunction() {
        if (this.isFunction()) {
            return this;
        }
        return new IndexNode(this, this.base, this.index, true, this.type, this.programPoint);
    }

    @Override
    public IndexNode setProgramPoint(int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new IndexNode(this, this.base, this.index, this.isFunction(), this.type, programPoint);
    }
}

