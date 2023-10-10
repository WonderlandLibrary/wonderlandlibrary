// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface Key extends Comparable<Key>, Examinable
{
    public static final String MINECRAFT_NAMESPACE = "minecraft";
    
    @NotNull
    default Key key(@NotNull @Pattern("([a-z0-9_\\-.]+:)?[a-z0-9_\\-./]+") final String string) {
        return key(string, ':');
    }
    
    @NotNull
    default Key key(@NotNull final String string, final char character) {
        final int index = string.indexOf(character);
        final String namespace = (index >= 1) ? string.substring(0, index) : "minecraft";
        final String value = (index >= 0) ? string.substring(index + 1) : string;
        return key(namespace, value);
    }
    
    @NotNull
    default Key key(@NotNull final Namespaced namespaced, @NotNull @Pattern("[a-z0-9_\\-./]+") final String value) {
        return key(namespaced.namespace(), value);
    }
    
    @NotNull
    default Key key(@NotNull @Pattern("[a-z0-9_\\-.]+") final String namespace, @NotNull @Pattern("[a-z0-9_\\-./]+") final String value) {
        return new KeyImpl(namespace, value);
    }
    
    @NotNull
    String namespace();
    
    @NotNull
    String value();
    
    @NotNull
    String asString();
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("namespace", this.namespace()), ExaminableProperty.of("value", this.value()) });
    }
    
    default int compareTo(@NotNull final Key that) {
        final int value = this.value().compareTo(that.value());
        if (value != 0) {
            return KeyImpl.clampCompare(value);
        }
        return KeyImpl.clampCompare(this.namespace().compareTo(that.namespace()));
    }
}
