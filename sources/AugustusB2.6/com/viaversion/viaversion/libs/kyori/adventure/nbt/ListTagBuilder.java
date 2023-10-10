// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;
import java.util.List;

final class ListTagBuilder<T extends BinaryTag> implements ListBinaryTag.Builder<T>
{
    @Nullable
    private List<BinaryTag> tags;
    private BinaryTagType<? extends BinaryTag> elementType;
    
    ListTagBuilder() {
        this(BinaryTagTypes.END);
    }
    
    ListTagBuilder(final BinaryTagType<? extends BinaryTag> type) {
        this.elementType = type;
    }
    
    @Override
    public ListBinaryTag.Builder<T> add(final BinaryTag tag) {
        ListBinaryTagImpl.noAddEnd(tag);
        if (this.elementType == BinaryTagTypes.END) {
            this.elementType = tag.type();
        }
        ListBinaryTagImpl.mustBeSameType(tag, this.elementType);
        if (this.tags == null) {
            this.tags = new ArrayList<BinaryTag>();
        }
        this.tags.add(tag);
        return this;
    }
    
    @Override
    public ListBinaryTag.Builder<T> add(final Iterable<? extends T> tagsToAdd) {
        for (final T tag : tagsToAdd) {
            this.add((BinaryTag)tag);
        }
        return this;
    }
    
    @NotNull
    @Override
    public ListBinaryTag build() {
        if (this.tags == null) {
            return ListBinaryTag.empty();
        }
        return new ListBinaryTagImpl(this.elementType, new ArrayList<BinaryTag>(this.tags));
    }
}
