// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Pair;
import java.util.Objects;
import java.io.Serializable;

public class ObjectIntMutablePair<K> implements ObjectIntPair<K>, Serializable
{
    private static final long serialVersionUID = 0L;
    protected K left;
    protected int right;
    
    public ObjectIntMutablePair(final K left, final int right) {
        this.left = left;
        this.right = right;
    }
    
    public static <K> ObjectIntMutablePair<K> of(final K left, final int right) {
        return new ObjectIntMutablePair<K>(left, right);
    }
    
    @Override
    public K left() {
        return this.left;
    }
    
    @Override
    public ObjectIntMutablePair<K> left(final K l) {
        this.left = l;
        return this;
    }
    
    @Override
    public int rightInt() {
        return this.right;
    }
    
    @Override
    public ObjectIntMutablePair<K> right(final int r) {
        this.right = r;
        return this;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof ObjectIntPair) {
            return Objects.equals(this.left, ((ObjectIntPair)other).left()) && this.right == ((ObjectIntPair)other).rightInt();
        }
        return other instanceof Pair && Objects.equals(this.left, ((Pair)other).left()) && Objects.equals(this.right, ((Pair)other).right());
    }
    
    @Override
    public int hashCode() {
        return ((this.left == null) ? 0 : this.left.hashCode()) * 19 + this.right;
    }
    
    @Override
    public String toString() {
        return "<" + this.left() + "," + this.rightInt() + ">";
    }
}
