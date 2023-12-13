// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.event.events.EventUseItem;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.ItemEnderPearl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemEnderPearl.class })
public class MixinItemEnderPearl
{
    @Inject(method = { "onItemRightClick" }, at = { @At("HEAD") }, cancellable = true)
    private void useitemrightclick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand, final CallbackInfoReturnable info) {
        final EventUseItem event = new EventUseItem(playerIn);
        event.call();
    }
}
