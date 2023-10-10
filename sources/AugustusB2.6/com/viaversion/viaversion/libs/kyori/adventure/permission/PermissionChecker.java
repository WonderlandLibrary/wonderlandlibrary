// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.permission;

import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.util.TriState;
import com.viaversion.viaversion.libs.kyori.adventure.pointer.Pointer;
import java.util.function.Predicate;

public interface PermissionChecker extends Predicate<String>
{
    public static final Pointer<PermissionChecker> POINTER = Pointer.pointer(PermissionChecker.class, Key.key("adventure", "permission"));
    
    @NotNull
    default PermissionChecker always(final TriState state) {
        if (state == TriState.TRUE) {
            return PermissionCheckers.TRUE;
        }
        if (state == TriState.FALSE) {
            return PermissionCheckers.FALSE;
        }
        return PermissionCheckers.NOT_SET;
    }
    
    @NotNull
    TriState value(final String permission);
    
    default boolean test(final String permission) {
        return this.value(permission) == TriState.TRUE;
    }
}
