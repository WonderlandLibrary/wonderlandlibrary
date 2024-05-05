/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.FoodStats
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import com.mojang.authlib.GameProfile;
import net.ccbluex.liquidbounce.injection.forge.mixins.entity.MixinEntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={EntityPlayer.class})
public abstract class MixinEntityPlayer
extends MixinEntityLivingBase {
    @Shadow
    protected int field_71101_bC;
    @Shadow
    public PlayerCapabilities field_71075_bZ;

    @Override
    @Shadow
    public abstract ItemStack func_70694_bm();

    @Shadow
    public abstract GameProfile func_146103_bH();

    @Shadow
    protected abstract boolean func_70041_e_();

    @Shadow
    protected abstract String func_145776_H();

    @Shadow
    public abstract FoodStats func_71024_bL();

    @Shadow
    public abstract int func_71057_bx();

    @Shadow
    public abstract ItemStack func_71011_bu();

    @Shadow
    public abstract boolean func_71039_bw();
}

