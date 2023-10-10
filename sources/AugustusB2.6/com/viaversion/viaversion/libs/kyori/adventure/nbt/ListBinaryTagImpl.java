// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.Spliterators;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"ListBinaryTag[type=\" + this.type.toString() + \"]\"", childrenArray = "this.tags.toArray()", hasChildren = "!this.tags.isEmpty()")
final class ListBinaryTagImpl extends AbstractBinaryTag implements ListBinaryTag
{
    static final ListBinaryTag EMPTY;
    private final List<BinaryTag> tags;
    private final BinaryTagType<? extends BinaryTag> elementType;
    private final int hashCode;
    
    ListBinaryTagImpl(final BinaryTagType<? extends BinaryTag> elementType, final List<BinaryTag> tags) {
        this.tags = Collections.unmodifiableList((List<? extends BinaryTag>)tags);
        this.elementType = elementType;
        this.hashCode = tags.hashCode();
    }
    
    @NotNull
    @Override
    public BinaryTagType<? extends BinaryTag> elementType() {
        return this.elementType;
    }
    
    @Override
    public int size() {
        return this.tags.size();
    }
    
    @NotNull
    @Override
    public BinaryTag get(final int index) {
        return this.tags.get(index);
    }
    
    @NotNull
    @Override
    public ListBinaryTag set(final int index, @NotNull final BinaryTag newTag, @Nullable final Consumer<? super BinaryTag> removed) {
        final BinaryTag oldTag;
        return this.edit(tags -> {
            oldTag = tags.set(index, newTag);
            if (removed != null) {
                removed.accept(oldTag);
            }
        }, newTag.type());
    }
    
    @NotNull
    @Override
    public ListBinaryTag remove(final int index, @Nullable final Consumer<? super BinaryTag> removed) {
        final BinaryTag oldTag;
        return this.edit(tags -> {
            oldTag = tags.remove(index);
            if (removed != null) {
                removed.accept(oldTag);
            }
        }, null);
    }
    
    @NotNull
    @Override
    public ListBinaryTag add(final BinaryTag tag) {
        noAddEnd(tag);
        if (this.elementType != BinaryTagTypes.END) {
            mustBeSameType(tag, this.elementType);
        }
        return this.edit(tags -> tags.add(tag), tag.type());
    }
    
    @NotNull
    @Override
    public ListBinaryTag add(final Iterable<? extends BinaryTag> tagsToAdd) {
        if (tagsToAdd instanceof Collection && ((Collection)tagsToAdd).isEmpty()) {
            return this;
        }
        final BinaryTagType<?> type = mustBeSameType(tagsToAdd);
        final Iterator<BinaryTag> iterator;
        BinaryTag tag;
        return this.edit(tags -> {
            tagsToAdd.iterator();
            while (iterator.hasNext()) {
                tag = iterator.next();
                tags.add(tag);
            }
        }, (BinaryTagType<? extends BinaryTag>)type);
    }
    
    static void noAddEnd(final BinaryTag tag) {
        if (tag.type() == BinaryTagTypes.END) {
            throw new IllegalArgumentException(String.format("Cannot add a %s to a %s", BinaryTagTypes.END, BinaryTagTypes.LIST));
        }
    }
    
    static BinaryTagType<?> mustBeSameType(final Iterable<? extends BinaryTag> tags) {
        BinaryTagType<?> type = null;
        for (final BinaryTag tag : tags) {
            if (type == null) {
                type = tag.type();
            }
            else {
                mustBeSameType(tag, (BinaryTagType<? extends BinaryTag>)type);
            }
        }
        return type;
    }
    
    static void mustBeSameType(final BinaryTag tag, final BinaryTagType<? extends BinaryTag> type) {
        if (tag.type() != type) {
            throw new IllegalArgumentException(String.format("Trying to add tag of type %s to list of %s", tag.type(), type));
        }
    }
    
    private ListBinaryTag edit(final Consumer<List<BinaryTag>> consumer, @Nullable final BinaryTagType<? extends BinaryTag> maybeElementType) {
        final List<BinaryTag> tags = new ArrayList<BinaryTag>(this.tags);
        consumer.accept(tags);
        BinaryTagType<? extends BinaryTag> elementType = this.elementType;
        if (maybeElementType != null && elementType == BinaryTagTypes.END) {
            elementType = maybeElementType;
        }
        return new ListBinaryTagImpl(elementType, tags);
    }
    
    @NotNull
    @Override
    public Stream<BinaryTag> stream() {
        return this.tags.stream();
    }
    
    @Override
    public Iterator<BinaryTag> iterator() {
        final Iterator<BinaryTag> iterator = this.tags.iterator();
        return new Iterator<BinaryTag>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public BinaryTag next() {
                return iterator.next();
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super BinaryTag> action) {
                iterator.forEachRemaining(action);
            }
        };
    }
    
    @Override
    public void forEach(final Consumer<? super BinaryTag> action) {
        this.tags.forEach(action);
    }
    
    @Override
    public Spliterator<BinaryTag> spliterator() {
        return Spliterators.spliterator((Collection<? extends BinaryTag>)this.tags, 1040);
    }
    
    @Override
    public boolean equals(final Object that) {
        return this == that || (that instanceof ListBinaryTagImpl && this.tags.equals(((ListBinaryTagImpl)that).tags));
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("tags", this.tags), ExaminableProperty.of("type", this.elementType) });
    }
    
    static {
        EMPTY = new ListBinaryTagImpl(BinaryTagTypes.END, Collections.emptyList());
    }
}
