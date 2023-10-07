// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders;

@Deprecated
public class Holder<V>
{
    private V value;
    
    public Holder() {
    }
    
    public Holder(final V defaultValue) {
        this.value = defaultValue;
    }
    
    public void set(final V value) {
        this.value = value;
    }
    
    public V get() {
        return this.value;
    }
}
