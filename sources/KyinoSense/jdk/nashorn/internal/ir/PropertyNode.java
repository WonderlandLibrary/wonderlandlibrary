/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.PropertyKey;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class PropertyNode
extends Node {
    private static final long serialVersionUID = 1L;
    private final PropertyKey key;
    private final Expression value;
    private final FunctionNode getter;
    private final FunctionNode setter;

    public PropertyNode(long token, int finish, PropertyKey key, Expression value, FunctionNode getter, FunctionNode setter) {
        super(token, finish);
        this.key = key;
        this.value = value;
        this.getter = getter;
        this.setter = setter;
    }

    private PropertyNode(PropertyNode propertyNode, PropertyKey key, Expression value, FunctionNode getter, FunctionNode setter) {
        super(propertyNode);
        this.key = key;
        this.value = value;
        this.getter = getter;
        this.setter = setter;
    }

    public String getKeyName() {
        return this.key.getPropertyName();
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterPropertyNode(this)) {
            return visitor.leavePropertyNode(this.setKey((PropertyKey)((Object)((Node)((Object)this.key)).accept(visitor))).setValue(this.value == null ? null : (Expression)this.value.accept(visitor)).setGetter(this.getter == null ? null : (FunctionNode)this.getter.accept((NodeVisitor)visitor)).setSetter(this.setter == null ? null : (FunctionNode)this.setter.accept((NodeVisitor)visitor)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        if (this.value instanceof FunctionNode && ((FunctionNode)this.value).getIdent() != null) {
            this.value.toString(sb);
        }
        if (this.value != null) {
            ((Node)((Object)this.key)).toString(sb, printType);
            sb.append(": ");
            this.value.toString(sb, printType);
        }
        if (this.getter != null) {
            sb.append(' ');
            this.getter.toString(sb, printType);
        }
        if (this.setter != null) {
            sb.append(' ');
            this.setter.toString(sb, printType);
        }
    }

    public FunctionNode getGetter() {
        return this.getter;
    }

    public PropertyNode setGetter(FunctionNode getter) {
        if (this.getter == getter) {
            return this;
        }
        return new PropertyNode(this, this.key, this.value, getter, this.setter);
    }

    public Expression getKey() {
        return (Expression)((Object)this.key);
    }

    private PropertyNode setKey(PropertyKey key) {
        if (this.key == key) {
            return this;
        }
        return new PropertyNode(this, key, this.value, this.getter, this.setter);
    }

    public FunctionNode getSetter() {
        return this.setter;
    }

    public PropertyNode setSetter(FunctionNode setter) {
        if (this.setter == setter) {
            return this;
        }
        return new PropertyNode(this, this.key, this.value, this.getter, setter);
    }

    public Expression getValue() {
        return this.value;
    }

    public PropertyNode setValue(Expression value) {
        if (this.value == value) {
            return this;
        }
        return new PropertyNode(this, this.key, value, this.getter, this.setter);
    }
}

