// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.settings;

public class OnChangedValue<T>
{
    private final T _old;
    private final T _new;
    
    public OnChangedValue(final T _old, final T _new) {
        this._old = _old;
        this._new = _new;
    }
    
    public T getOld() {
        return this._old;
    }
    
    public T getNew() {
        return this._new;
    }
}
