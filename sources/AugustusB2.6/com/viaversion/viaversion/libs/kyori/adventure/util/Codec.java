// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.NotNull;

public interface Codec<D, E, DX extends Throwable, EX extends Throwable>
{
    @NotNull
    default <D, E, DX extends Throwable, EX extends Throwable> Codec<D, E, DX, EX> of(@NotNull final Decoder<D, E, DX> decoder, @NotNull final Encoder<D, E, EX> encoder) {
        return new Codec<D, E, DX, EX>() {
            @NotNull
            @Override
            public D decode(@NotNull final E encoded) throws DX, Throwable {
                return decoder.decode(encoded);
            }
            
            @NotNull
            @Override
            public E encode(@NotNull final D decoded) throws EX, Throwable {
                return encoder.encode(decoded);
            }
        };
    }
    
    @NotNull
    D decode(@NotNull final E encoded) throws DX, Throwable;
    
    @NotNull
    E encode(@NotNull final D decoded) throws EX, Throwable;
    
    public interface Encoder<D, E, X extends Throwable>
    {
        @NotNull
        E encode(@NotNull final D decoded) throws X, Throwable;
    }
    
    public interface Decoder<D, E, X extends Throwable>
    {
        @NotNull
        D decode(@NotNull final E encoded) throws X, Throwable;
    }
}
