// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Objects;
import com.viaversion.viaversion.libs.fastutil.Pair;
import java.io.Serializable;

public class IntIntMutablePair implements IntIntPair, Serializable
{
    private static final long serialVersionUID = 0L;
    protected int left;
    protected int right;
    
    public IntIntMutablePair(final int left, final int right) {
        this.left = left;
        this.right = right;
    }
    
    public static IntIntMutablePair of(final int left, final int right) {
        return new IntIntMutablePair(left, right);
    }
    
    @Override
    public int leftInt() {
        return this.left;
    }
    
    @Override
    public IntIntMutablePair left(final int l) {
        this.left = l;
        return this;
    }
    
    @Override
    public int rightInt() {
        return this.right;
    }
    
    @Override
    public IntIntMutablePair right(final int r) {
        this.right = r;
        return this;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof IntIntPair) {
            return this.left == ((IntIntPair)other).leftInt() && this.right == ((IntIntPair)other).rightInt();
        }
        return other instanceof Pair && Objects.equals(this.left, ((Pair)other).left()) && Objects.equals(this.right, ((Pair)other).right());
    }
    
    @Override
    public int hashCode() {
        return this.left * 19 + this.right;
    }
    
    @Override
    public String toString() {
        return "<" + this.leftInt() + "," + this.rightInt() + ">";
    }
}
