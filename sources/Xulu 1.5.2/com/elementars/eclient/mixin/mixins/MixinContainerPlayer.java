// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.event.events.CloseInventoryEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ContainerPlayer.class })
public class MixinContainerPlayer
{
    @Inject(method = { "onContainerClosed" }, at = { @At("HEAD") }, cancellable = true)
    public void getPlayerName(final EntityPlayer playerIn, final CallbackInfo ci) {
        final CloseInventoryEvent event = new CloseInventoryEvent();
        event.call();
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
