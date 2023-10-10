// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Objects;
import com.viaversion.viaversion.libs.fastutil.Pair;
import java.io.Serializable;

public class IntIntImmutablePair implements IntIntPair, Serializable
{
    private static final long serialVersionUID = 0L;
    protected final int left;
    protected final int right;
    
    public IntIntImmutablePair(final int left, final int right) {
        this.left = left;
        this.right = right;
    }
    
    public static IntIntImmutablePair of(final int left, final int right) {
        return new IntIntImmutablePair(left, right);
    }
    
    @Override
    public int leftInt() {
        return this.left;
    }
    
    @Override
    public int rightInt() {
        return this.right;
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
