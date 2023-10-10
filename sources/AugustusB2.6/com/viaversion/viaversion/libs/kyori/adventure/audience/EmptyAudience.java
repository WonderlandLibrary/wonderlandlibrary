// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.audience;

import com.viaversion.viaversion.libs.kyori.adventure.inventory.Book;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identity;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identified;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.pointer.Pointer;

final class EmptyAudience implements Audience
{
    static final EmptyAudience INSTANCE;
    
    @NotNull
    @Override
    public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        return Optional.empty();
    }
    
    @Contract("_, null -> null; _, !null -> !null")
    @Nullable
    @Override
    public <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
        return defaultValue;
    }
    
    @Override
    public <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
        return (T)defaultValue.get();
    }
    
    @NotNull
    @Override
    public Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
        return this;
    }
    
    @Override
    public void forEachAudience(@NotNull final Consumer<? super Audience> action) {
    }
    
    @Override
    public void sendMessage(@NotNull final ComponentLike message) {
    }
    
    @Override
    public void sendMessage(@NotNull final Identified source, @NotNull final ComponentLike message) {
    }
    
    @Override
    public void sendMessage(@NotNull final Identity source, @NotNull final ComponentLike message) {
    }
    
    @Override
    public void sendMessage(@NotNull final ComponentLike message, @NotNull final MessageType type) {
    }
    
    @Override
    public void sendMessage(@NotNull final Identified source, @NotNull final ComponentLike message, @NotNull final MessageType type) {
    }
    
    @Override
    public void sendMessage(@NotNull final Identity source, @NotNull final ComponentLike message, @NotNull final MessageType type) {
    }
    
    @Override
    public void sendActionBar(@NotNull final ComponentLike message) {
    }
    
    @Override
    public void sendPlayerListHeader(@NotNull final ComponentLike header) {
    }
    
    @Override
    public void sendPlayerListFooter(@NotNull final ComponentLike footer) {
    }
    
    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull final ComponentLike header, @NotNull final ComponentLike footer) {
    }
    
    @Override
    public void openBook(final Book.Builder book) {
    }
    
    @Override
    public boolean equals(final Object that) {
        return this == that;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "EmptyAudience";
    }
    
    static {
        INSTANCE = new EmptyAudience();
    }
}
