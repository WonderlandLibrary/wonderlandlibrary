// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.player;

import java.util.List;

public class Manager<T>
{
    protected final List<T> elements;
    
    public Manager(final List<T> elements) {
        this.elements = elements;
    }
    
    public List<T> getElements() {
        return this.elements;
    }
}
