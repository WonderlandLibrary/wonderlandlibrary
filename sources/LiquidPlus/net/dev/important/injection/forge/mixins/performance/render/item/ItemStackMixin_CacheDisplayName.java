/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package net.dev.important.injection.forge.mixins.performance.render.item;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ItemStack.class})
public class ItemStackMixin_CacheDisplayName {
    private String patcher$cachedDisplayName;

    @Inject(method={"getDisplayName"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$returnCachedDisplayName(CallbackInfoReturnable<String> cir) {
        if (this.patcher$cachedDisplayName != null) {
            cir.setReturnValue(this.patcher$cachedDisplayName);
        }
    }

    @Inject(method={"getDisplayName"}, at={@At(value="RETURN")})
    private void patcher$cacheDisplayName(CallbackInfoReturnable<String> cir) {
        this.patcher$cachedDisplayName = cir.getReturnValue();
    }

    @Inject(method={"setStackDisplayName"}, at={@At(value="HEAD")})
    private void patcher$resetCachedDisplayName(String displayName, CallbackInfoReturnable<ItemStack> cir) {
        this.patcher$cachedDisplayName = null;
    }
}

