// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Pair;
import java.util.Objects;
import java.io.Serializable;

public class IntObjectMutablePair<V> implements IntObjectPair<V>, Serializable
{
    private static final long serialVersionUID = 0L;
    protected int left;
    protected V right;
    
    public IntObjectMutablePair(final int left, final V right) {
        this.left = left;
        this.right = right;
    }
    
    public static <V> IntObjectMutablePair<V> of(final int left, final V right) {
        return new IntObjectMutablePair<V>(left, right);
    }
    
    @Override
    public int leftInt() {
        return this.left;
    }
    
    @Override
    public IntObjectMutablePair<V> left(final int l) {
        this.left = l;
        return this;
    }
    
    @Override
    public V right() {
        return this.right;
    }
    
    @Override
    public IntObjectMutablePair<V> right(final V r) {
        this.right = r;
        return this;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof IntObjectPair) {
            return this.left == ((IntObjectPair)other).leftInt() && Objects.equals(this.right, ((IntObjectPair)other).right());
        }
        return other instanceof Pair && Objects.equals(this.left, ((Pair)other).left()) && Objects.equals(this.right, ((Pair)other).right());
    }
    
    @Override
    public int hashCode() {
        return this.left * 19 + ((this.right == null) ? 0 : this.right.hashCode());
    }
    
    @Override
    public String toString() {
        return "<" + this.leftInt() + "," + this.right() + ">";
    }
}
