// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

public class Triplet<T, S, U>
{
    T first;
    S second;
    U third;
    
    public Triplet(final T first, final S second, final U third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    public T getFirst() {
        return this.first;
    }
    
    public S getSecond() {
        return this.second;
    }
    
    public U getThird() {
        return this.third;
    }
    
    public void setFirst(final T first) {
        this.first = first;
    }
    
    public void setSecond(final S second) {
        this.second = second;
    }
    
    public void setThird(final U third) {
        this.third = third;
    }
}
