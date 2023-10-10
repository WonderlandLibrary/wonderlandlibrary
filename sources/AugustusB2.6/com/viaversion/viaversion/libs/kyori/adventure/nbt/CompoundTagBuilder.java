// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.function.Consumer;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

final class CompoundTagBuilder implements CompoundBinaryTag.Builder
{
    @Nullable
    private Map<String, BinaryTag> tags;
    
    private Map<String, BinaryTag> tags() {
        if (this.tags == null) {
            this.tags = new HashMap<String, BinaryTag>();
        }
        return this.tags;
    }
    
    @Override
    public CompoundBinaryTag.Builder put(@NotNull final String key, @NotNull final BinaryTag tag) {
        this.tags().put(key, tag);
        return this;
    }
    
    @Override
    public CompoundBinaryTag.Builder put(@NotNull final CompoundBinaryTag tag) {
        final Map<String, BinaryTag> tags = this.tags();
        for (final String key : tag.keySet()) {
            tags.put(key, tag.get(key));
        }
        return this;
    }
    
    @Override
    public CompoundBinaryTag.Builder put(@NotNull final Map<String, ? extends BinaryTag> tags) {
        this.tags().putAll(tags);
        return this;
    }
    
    @Override
    public CompoundBinaryTag.Builder remove(@NotNull final String key, @Nullable final Consumer<? super BinaryTag> removed) {
        if (this.tags != null) {
            final BinaryTag tag = this.tags.remove(key);
            if (removed != null) {
                removed.accept(tag);
            }
        }
        return this;
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag build() {
        if (this.tags == null) {
            return CompoundBinaryTag.empty();
        }
        return new CompoundBinaryTagImpl(new HashMap<String, BinaryTag>(this.tags));
    }
}
