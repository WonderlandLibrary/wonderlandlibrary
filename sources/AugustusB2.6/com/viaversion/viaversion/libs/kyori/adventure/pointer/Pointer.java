// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.pointer;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface Pointer<V> extends Examinable
{
    @NotNull
    default <V> Pointer<V> pointer(@NotNull final Class<V> type, @NotNull final Key key) {
        return new PointerImpl<V>(type, key);
    }
    
    @NotNull
    Class<V> type();
    
    @NotNull
    Key key();
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("type", this.type()), ExaminableProperty.of("key", this.key()) });
    }
}
