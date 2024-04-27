/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package net.dev.important.injection.forge.mixins.accessors;

import java.util.UUID;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Item.class})
public interface ItemAccessor {
    @Accessor
    public static UUID getItemModifierUUID() {
        throw new UnsupportedOperationException("Mixin failed to inject!");
    }
}

