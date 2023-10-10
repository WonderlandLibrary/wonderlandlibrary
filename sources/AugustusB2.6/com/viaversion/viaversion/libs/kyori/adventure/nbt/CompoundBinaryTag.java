// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.Nullable;
import java.util.Set;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public interface CompoundBinaryTag extends BinaryTag, CompoundTagSetter<CompoundBinaryTag>, Iterable<Map.Entry<String, ? extends BinaryTag>>
{
    @NotNull
    default CompoundBinaryTag empty() {
        return CompoundBinaryTagImpl.EMPTY;
    }
    
    @NotNull
    default CompoundBinaryTag from(@NotNull final Map<String, ? extends BinaryTag> tags) {
        if (tags.isEmpty()) {
            return empty();
        }
        return new CompoundBinaryTagImpl(new HashMap<String, BinaryTag>(tags));
    }
    
    @NotNull
    default Builder builder() {
        return new CompoundTagBuilder();
    }
    
    @NotNull
    default BinaryTagType<CompoundBinaryTag> type() {
        return BinaryTagTypes.COMPOUND;
    }
    
    @NotNull
    Set<String> keySet();
    
    @Nullable
    BinaryTag get(final String key);
    
    default boolean getBoolean(@NotNull final String key) {
        return this.getBoolean(key, false);
    }
    
    default boolean getBoolean(@NotNull final String key, final boolean defaultValue) {
        return this.getByte(key) != 0 || defaultValue;
    }
    
    default byte getByte(@NotNull final String key) {
        return this.getByte(key, (byte)0);
    }
    
    byte getByte(@NotNull final String key, final byte defaultValue);
    
    default short getShort(@NotNull final String key) {
        return this.getShort(key, (short)0);
    }
    
    short getShort(@NotNull final String key, final short defaultValue);
    
    default int getInt(@NotNull final String key) {
        return this.getInt(key, 0);
    }
    
    int getInt(@NotNull final String key, final int defaultValue);
    
    default long getLong(@NotNull final String key) {
        return this.getLong(key, 0L);
    }
    
    long getLong(@NotNull final String key, final long defaultValue);
    
    default float getFloat(@NotNull final String key) {
        return this.getFloat(key, 0.0f);
    }
    
    float getFloat(@NotNull final String key, final float defaultValue);
    
    default double getDouble(@NotNull final String key) {
        return this.getDouble(key, 0.0);
    }
    
    double getDouble(@NotNull final String key, final double defaultValue);
    
    byte[] getByteArray(@NotNull final String key);
    
    byte[] getByteArray(@NotNull final String key, final byte[] defaultValue);
    
    @NotNull
    default String getString(@NotNull final String key) {
        return this.getString(key, "");
    }
    
    @NotNull
    String getString(@NotNull final String key, @NotNull final String defaultValue);
    
    @NotNull
    default ListBinaryTag getList(@NotNull final String key) {
        return this.getList(key, ListBinaryTag.empty());
    }
    
    @NotNull
    ListBinaryTag getList(@NotNull final String key, @NotNull final ListBinaryTag defaultValue);
    
    @NotNull
    default ListBinaryTag getList(@NotNull final String key, @NotNull final BinaryTagType<? extends BinaryTag> expectedType) {
        return this.getList(key, expectedType, ListBinaryTag.empty());
    }
    
    @NotNull
    ListBinaryTag getList(@NotNull final String key, @NotNull final BinaryTagType<? extends BinaryTag> expectedType, @NotNull final ListBinaryTag defaultValue);
    
    @NotNull
    default CompoundBinaryTag getCompound(@NotNull final String key) {
        return this.getCompound(key, empty());
    }
    
    @NotNull
    CompoundBinaryTag getCompound(@NotNull final String key, @NotNull final CompoundBinaryTag defaultValue);
    
    int[] getIntArray(@NotNull final String key);
    
    int[] getIntArray(@NotNull final String key, final int[] defaultValue);
    
    long[] getLongArray(@NotNull final String key);
    
    long[] getLongArray(@NotNull final String key, final long[] defaultValue);
    
    public interface Builder extends CompoundTagSetter<Builder>
    {
        @NotNull
        CompoundBinaryTag build();
    }
}
