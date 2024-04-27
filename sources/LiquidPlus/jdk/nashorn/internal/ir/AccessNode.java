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
public final class AccessNode
extends BaseNode {
    private static final long serialVersionUID = 1L;
    private final String property;

    public AccessNode(long token, int finish, Expression base, String property) {
        super(token, finish, base, false);
        this.property = property;
    }

    private AccessNode(AccessNode accessNode, Expression base, String property, boolean isFunction, Type type, int id) {
        super(accessNode, base, isFunction, type, id);
        this.property = property;
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterAccessNode(this)) {
            return visitor.leaveAccessNode(this.setBase((Expression)this.base.accept(visitor)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        boolean needsParen = this.tokenType().needsParens(this.getBase().tokenType(), true);
        if (printType) {
            this.optimisticTypeToString(sb);
        }
        if (needsParen) {
            sb.append('(');
        }
        this.base.toString(sb, printType);
        if (needsParen) {
            sb.append(')');
        }
        sb.append('.');
        sb.append(this.property);
    }

    public String getProperty() {
        return this.property;
    }

    private AccessNode setBase(Expression base) {
        if (this.base == base) {
            return this;
        }
        return new AccessNode(this, base, this.property, this.isFunction(), this.type, this.programPoint);
    }

    @Override
    public AccessNode setType(Type type) {
        if (this.type == type) {
            return this;
        }
        return new AccessNode(this, this.base, this.property, this.isFunction(), type, this.programPoint);
    }

    @Override
    public AccessNode setProgramPoint(int programPoint) {
        if (this.programPoint == programPoint) {
            return this;
        }
        return new AccessNode(this, this.base, this.property, this.isFunction(), this.type, programPoint);
    }

    @Override
    public AccessNode setIsFunction() {
        if (this.isFunction()) {
            return this;
        }
        return new AccessNode(this, this.base, this.property, true, this.type, this.programPoint);
    }
}

