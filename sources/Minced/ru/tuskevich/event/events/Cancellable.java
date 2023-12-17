// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events;

public interface Cancellable
{
    boolean isCanceled();
    
    void cancel();
}
