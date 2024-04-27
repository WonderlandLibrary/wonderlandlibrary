/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 */
package net.dev.important.injection.forge.mixins.accessors;

import java.util.List;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={KeyBinding.class})
public interface KeyBindingAccessor {
    @Accessor
    public static List<KeyBinding> getKeybindArray() {
        throw new UnsupportedOperationException("Mixin failed to inject!");
    }
}

