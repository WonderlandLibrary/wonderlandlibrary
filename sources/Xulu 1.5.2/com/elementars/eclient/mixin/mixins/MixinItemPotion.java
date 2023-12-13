// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.event.events.EventDrinkPotion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemPotion;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemPotion.class })
public class MixinItemPotion
{
    @Inject(method = { "onItemUseFinish" }, at = { @At("HEAD") }, cancellable = true)
    private void onItemUseFinish(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving, final CallbackInfoReturnable info) {
        final EventDrinkPotion event = new EventDrinkPotion(entityLiving, stack);
        event.call();
    }
}
