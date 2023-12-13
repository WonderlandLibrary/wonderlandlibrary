// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.Xulu;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiIngame.class })
public class MixinGuiIngame
{
    @Inject(method = { "renderPotionEffects" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderPotionEffects(final CallbackInfo info) {
        if (Xulu.VALUE_MANAGER.getValueByName("Hide Potions").getValue()) {
            info.cancel();
        }
    }
}
