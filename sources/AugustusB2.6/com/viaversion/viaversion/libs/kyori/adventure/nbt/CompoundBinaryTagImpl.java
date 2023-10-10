// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"CompoundBinaryTag[length=\" + this.tags.size() + \"]\"", childrenArray = "this.tags.entrySet().toArray()", hasChildren = "!this.tags.isEmpty()")
final class CompoundBinaryTagImpl extends AbstractBinaryTag implements CompoundBinaryTag
{
    static final CompoundBinaryTag EMPTY;
    private final Map<String, BinaryTag> tags;
    private final int hashCode;
    
    CompoundBinaryTagImpl(final Map<String, BinaryTag> tags) {
        this.tags = Collections.unmodifiableMap((Map<? extends String, ? extends BinaryTag>)tags);
        this.hashCode = tags.hashCode();
    }
    
    public boolean contains(@NotNull final String key, @NotNull final BinaryTagType<?> type) {
        final BinaryTag tag = this.tags.get(key);
        return tag != null && type.test(tag.type());
    }
    
    @NotNull
    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet((Set<? extends String>)this.tags.keySet());
    }
    
    @Nullable
    @Override
    public BinaryTag get(final String key) {
        return this.tags.get(key);
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag put(@NotNull final String key, @NotNull final BinaryTag tag) {
        return this.edit(map -> map.put(key, tag));
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag put(@NotNull final CompoundBinaryTag tag) {
        final Iterator<String> iterator;
        String key;
        return this.edit(map -> {
            tag.keySet().iterator();
            while (iterator.hasNext()) {
                key = iterator.next();
                map.put(key, tag.get(key));
            }
        });
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag put(@NotNull final Map<String, ? extends BinaryTag> tags) {
        return this.edit(map -> map.putAll(tags));
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag remove(@NotNull final String key, @Nullable final Consumer<? super BinaryTag> removed) {
        if (!this.tags.containsKey(key)) {
            return this;
        }
        final BinaryTag tag;
        return this.edit(map -> {
            tag = map.remove(key);
            if (removed != null) {
                removed.accept(tag);
            }
        });
    }
    
    @Override
    public byte getByte(@NotNull final String key, final byte defaultValue) {
        if (this.contains(key, BinaryTagTypes.BYTE)) {
            return this.tags.get(key).byteValue();
        }
        return defaultValue;
    }
    
    @Override
    public short getShort(@NotNull final String key, final short defaultValue) {
        if (this.contains(key, BinaryTagTypes.SHORT)) {
            return this.tags.get(key).shortValue();
        }
        return defaultValue;
    }
    
    @Override
    public int getInt(@NotNull final String key, final int defaultValue) {
        if (this.contains(key, BinaryTagTypes.INT)) {
            return this.tags.get(key).intValue();
        }
        return defaultValue;
    }
    
    @Override
    public long getLong(@NotNull final String key, final long defaultValue) {
        if (this.contains(key, BinaryTagTypes.LONG)) {
            return this.tags.get(key).longValue();
        }
        return defaultValue;
    }
    
    @Override
    public float getFloat(@NotNull final String key, final float defaultValue) {
        if (this.contains(key, BinaryTagTypes.FLOAT)) {
            return this.tags.get(key).floatValue();
        }
        return defaultValue;
    }
    
    @Override
    public double getDouble(@NotNull final String key, final double defaultValue) {
        if (this.contains(key, BinaryTagTypes.DOUBLE)) {
            return this.tags.get(key).doubleValue();
        }
        return defaultValue;
    }
    
    @Override
    public byte[] getByteArray(@NotNull final String key) {
        if (this.contains(key, BinaryTagTypes.BYTE_ARRAY)) {
            return this.tags.get(key).value();
        }
        return new byte[0];
    }
    
    @Override
    public byte[] getByteArray(@NotNull final String key, final byte[] defaultValue) {
        if (this.contains(key, BinaryTagTypes.BYTE_ARRAY)) {
            return this.tags.get(key).value();
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public String getString(@NotNull final String key, @NotNull final String defaultValue) {
        if (this.contains(key, BinaryTagTypes.STRING)) {
            return this.tags.get(key).value();
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public ListBinaryTag getList(@NotNull final String key, @NotNull final ListBinaryTag defaultValue) {
        if (this.contains(key, BinaryTagTypes.LIST)) {
            return this.tags.get(key);
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public ListBinaryTag getList(@NotNull final String key, @NotNull final BinaryTagType<? extends BinaryTag> expectedType, @NotNull final ListBinaryTag defaultValue) {
        if (this.contains(key, BinaryTagTypes.LIST)) {
            final ListBinaryTag tag = this.tags.get(key);
            if (expectedType.test(tag.elementType())) {
                return tag;
            }
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag getCompound(@NotNull final String key, @NotNull final CompoundBinaryTag defaultValue) {
        if (this.contains(key, BinaryTagTypes.COMPOUND)) {
            return this.tags.get(key);
        }
        return defaultValue;
    }
    
    @Override
    public int[] getIntArray(@NotNull final String key) {
        if (this.contains(key, BinaryTagTypes.INT_ARRAY)) {
            return this.tags.get(key).value();
        }
        return new int[0];
    }
    
    @Override
    public int[] getIntArray(@NotNull final String key, final int[] defaultValue) {
        if (this.contains(key, BinaryTagTypes.INT_ARRAY)) {
            return this.tags.get(key).value();
        }
        return defaultValue;
    }
    
    @Override
    public long[] getLongArray(@NotNull final String key) {
        if (this.contains(key, BinaryTagTypes.LONG_ARRAY)) {
            return this.tags.get(key).value();
        }
        return new long[0];
    }
    
    @Override
    public long[] getLongArray(@NotNull final String key, final long[] defaultValue) {
        if (this.contains(key, BinaryTagTypes.LONG_ARRAY)) {
            return this.tags.get(key).value();
        }
        return defaultValue;
    }
    
    private CompoundBinaryTag edit(final Consumer<Map<String, BinaryTag>> consumer) {
        final Map<String, BinaryTag> tags = new HashMap<String, BinaryTag>(this.tags);
        consumer.accept(tags);
        return new CompoundBinaryTagImpl(tags);
    }
    
    @Override
    public boolean equals(final Object that) {
        return this == that || (that instanceof CompoundBinaryTagImpl && this.tags.equals(((CompoundBinaryTagImpl)that).tags));
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("tags", this.tags));
    }
    
    @NotNull
    @Override
    public Iterator<Map.Entry<String, ? extends BinaryTag>> iterator() {
        return (Iterator<Map.Entry<String, ? extends BinaryTag>>)this.tags.entrySet().iterator();
    }
    
    @Override
    public void forEach(@NotNull final Consumer<? super Map.Entry<String, ? extends BinaryTag>> action) {
        this.tags.entrySet().forEach(Objects.requireNonNull(action, "action"));
    }
    
    static {
        EMPTY = new CompoundBinaryTagImpl(Collections.emptyMap());
    }
}
