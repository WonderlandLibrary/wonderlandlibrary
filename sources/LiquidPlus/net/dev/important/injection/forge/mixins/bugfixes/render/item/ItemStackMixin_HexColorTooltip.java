/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package net.dev.important.injection.forge.mixins.bugfixes.render.item;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={ItemStack.class})
public class ItemStackMixin_HexColorTooltip {
    @Redirect(method={"getTooltip"}, at=@At(value="INVOKE", target="Ljava/lang/Integer;toHexString(I)Ljava/lang/String;"))
    private String patcher$fixHexColorString(int i) {
        return String.format("%06X", i);
    }
}

