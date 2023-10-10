// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import java.util.Objects;

public class Triple<A, B, C>
{
    private final A first;
    private final B second;
    private final C third;
    
    public Triple(final A first, final B second, final C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    public A first() {
        return this.first;
    }
    
    public B second() {
        return this.second;
    }
    
    public C third() {
        return this.third;
    }
    
    @Deprecated
    public A getFirst() {
        return this.first;
    }
    
    @Deprecated
    public B getSecond() {
        return this.second;
    }
    
    @Deprecated
    public C getThird() {
        return this.third;
    }
    
    @Override
    public String toString() {
        return "Triple{" + this.first + ", " + this.second + ", " + this.third + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Triple<?, ?, ?> triple = (Triple<?, ?, ?>)o;
        return Objects.equals(this.first, triple.first) && Objects.equals(this.second, triple.second) && Objects.equals(this.third, triple.third);
    }
    
    @Override
    public int hashCode() {
        int result = (this.first != null) ? this.first.hashCode() : 0;
        result = 31 * result + ((this.second != null) ? this.second.hashCode() : 0);
        result = 31 * result + ((this.third != null) ? this.third.hashCode() : 0);
        return result;
    }
}
