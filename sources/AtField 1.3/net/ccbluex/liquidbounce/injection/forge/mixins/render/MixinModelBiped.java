/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelBiped$ArmPose
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={ModelBiped.class})
public class MixinModelBiped {
    @Shadow
    public ModelRenderer field_178723_h;
    @Shadow
    public ModelRenderer field_78116_c;
    @Shadow
    public ModelBiped.ArmPose field_187076_m;

    @Inject(method={"setRotationAngles"}, at={@At(value="FIELD", target="Lnet/minecraft/client/model/ModelBiped;swingProgress:F")})
    private void revertSwordAnimation(float f, float f2, float f3, float f4, float f5, float f6, Entity entity, CallbackInfo callbackInfo) {
        if (this.field_187076_m == ModelBiped.ArmPose.BOW_AND_ARROW) {
            this.field_178723_h.field_78796_g = 0.0f;
        }
        if (LiquidBounce.moduleManager.getModule(Rotations.class).getState() && RotationUtils.serverRotation != null && entity instanceof EntityPlayer && entity.equals((Object)Minecraft.func_71410_x().field_71439_g)) {
            this.field_78116_c.field_78795_f = RotationUtils.serverRotation.getPitch() / 57.295776f;
        }
    }
}

