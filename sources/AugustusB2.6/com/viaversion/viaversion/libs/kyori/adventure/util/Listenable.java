// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public abstract class Listenable<L>
{
    private final List<L> listeners;
    
    public Listenable() {
        this.listeners = new CopyOnWriteArrayList<L>();
    }
    
    protected final void forEachListener(@NotNull final Consumer<L> consumer) {
        for (final L listener : this.listeners) {
            consumer.accept(listener);
        }
    }
    
    protected final void addListener0(@NotNull final L listener) {
        this.listeners.add(listener);
    }
    
    protected final void removeListener0(@NotNull final L listener) {
        this.listeners.remove(listener);
    }
}
