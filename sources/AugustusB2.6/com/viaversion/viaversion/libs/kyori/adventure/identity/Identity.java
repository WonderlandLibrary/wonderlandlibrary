// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.identity;

import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import java.util.Locale;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.util.UUID;
import com.viaversion.viaversion.libs.kyori.adventure.pointer.Pointer;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface Identity extends Examinable
{
    public static final Pointer<String> NAME = Pointer.pointer(String.class, Key.key("adventure", "name"));
    public static final Pointer<UUID> UUID = Pointer.pointer(UUID.class, Key.key("adventure", "uuid"));
    public static final Pointer<Component> DISPLAY_NAME = Pointer.pointer(Component.class, Key.key("adventure", "display_name"));
    public static final Pointer<Locale> LOCALE = Pointer.pointer(Locale.class, Key.key("adventure", "locale"));
    
    @NotNull
    default Identity nil() {
        return Identities.NIL;
    }
    
    @NotNull
    default Identity identity(@NotNull final UUID uuid) {
        if (uuid.equals(Identities.NIL.uuid())) {
            return Identities.NIL;
        }
        return new IdentityImpl(uuid);
    }
    
    @NotNull
    UUID uuid();
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("uuid", this.uuid()));
    }
}
