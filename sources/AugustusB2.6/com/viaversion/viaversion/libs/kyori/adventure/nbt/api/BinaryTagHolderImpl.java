// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt.api;

import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

final class BinaryTagHolderImpl implements BinaryTagHolder
{
    private final String string;
    
    BinaryTagHolderImpl(final String string) {
        this.string = Objects.requireNonNull(string, "string");
    }
    
    @NotNull
    @Override
    public String string() {
        return this.string;
    }
    
    @NotNull
    @Override
    public <T, DX extends Exception> T get(@NotNull final Codec<T, String, DX, ?> codec) throws DX, Exception {
        return codec.decode(this.string);
    }
    
    @Override
    public int hashCode() {
        return 31 * this.string.hashCode();
    }
    
    @Override
    public boolean equals(final Object that) {
        return that instanceof BinaryTagHolderImpl && this.string.equals(((BinaryTagHolderImpl)that).string);
    }
    
    @Override
    public String toString() {
        return this.string;
    }
}
