/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.util.FoodStats
 *  net.minecraft.util.SoundEvent
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import com.mojang.authlib.GameProfile;
import net.ccbluex.liquidbounce.injection.forge.mixins.entity.MixinEntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={EntityPlayer.class})
public abstract class MixinEntityPlayer
extends MixinEntityLivingBase {
    @Shadow
    @Final
    protected static DataParameter field_184828_bq;
    @Shadow
    protected int field_71101_bC;
    @Shadow
    public PlayerCapabilities field_71075_bZ;

    @Override
    @Shadow
    protected abstract boolean func_70041_e_();

    @Shadow
    public abstract GameProfile func_146103_bH();

    @Shadow
    public abstract ItemStack func_184582_a(EntityEquipmentSlot var1);

    @Shadow
    public abstract FoodStats func_71024_bL();

    @Override
    @Shadow
    protected abstract SoundEvent func_184184_Z();
}

