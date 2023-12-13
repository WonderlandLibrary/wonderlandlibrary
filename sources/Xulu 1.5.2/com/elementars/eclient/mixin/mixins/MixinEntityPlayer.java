// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends MixinEntityLivingBase
{
    @Shadow
    public InventoryEnderChest enderChest;
}
