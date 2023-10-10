// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Objects;
import java.io.Serializable;
import com.viaversion.viaversion.libs.fastutil.Pair;

public class ObjectObjectImmutablePair<K, V> implements Pair<K, V>, Serializable
{
    private static final long serialVersionUID = 0L;
    protected final K left;
    protected final V right;
    
    public ObjectObjectImmutablePair(final K left, final V right) {
        this.left = left;
        this.right = right;
    }
    
    public static <K, V> ObjectObjectImmutablePair<K, V> of(final K left, final V right) {
        return new ObjectObjectImmutablePair<K, V>(left, right);
    }
    
    @Override
    public K left() {
        return this.left;
    }
    
    @Override
    public V right() {
        return this.right;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other != null && other instanceof Pair && Objects.equals(this.left, ((Pair)other).left()) && Objects.equals(this.right, ((Pair)other).right());
    }
    
    @Override
    public int hashCode() {
        return ((this.left == null) ? 0 : this.left.hashCode()) * 19 + ((this.right == null) ? 0 : this.right.hashCode());
    }
    
    @Override
    public String toString() {
        return "<" + this.left() + "," + this.right() + ">";
    }
}
