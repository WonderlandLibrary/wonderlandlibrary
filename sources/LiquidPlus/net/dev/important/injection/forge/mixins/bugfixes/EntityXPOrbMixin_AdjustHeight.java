/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={EntityXPOrb.class})
public class EntityXPOrbMixin_AdjustHeight {
    @Redirect(method={"onUpdate"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/EntityPlayer;getEyeHeight()F"))
    private float patcher$lowerHeight(EntityPlayer entityPlayer) {
        return (float)((double)entityPlayer.func_70047_e() / 2.0);
    }
}

