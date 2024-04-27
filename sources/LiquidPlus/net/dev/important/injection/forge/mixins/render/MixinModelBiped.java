/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.injection.forge.mixins.render;

import net.dev.important.Client;
import net.dev.important.modules.module.modules.misc.SpinBot;
import net.dev.important.modules.module.modules.render.Rotations;
import net.dev.important.utils.RotationUtils;
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
    public int field_78120_m;
    @Shadow
    public ModelRenderer field_78116_c;

    @Inject(method={"setRotationAngles"}, at={@At(value="FIELD", target="Lnet/minecraft/client/model/ModelBiped;swingProgress:F")})
    private void revertSwordAnimation(float p_setRotationAngles_1_, float p_setRotationAngles_2_, float p_setRotationAngles_3_, float p_setRotationAngles_4_, float p_setRotationAngles_5_, float p_setRotationAngles_6_, Entity p_setRotationAngles_7_, CallbackInfo callbackInfo) {
        if (this.field_78120_m == 3) {
            this.field_178723_h.field_78796_g = 0.0f;
        }
        Rotations rotationModule = (Rotations)Client.moduleManager.getModule(Rotations.class);
        if (p_setRotationAngles_7_ instanceof EntityPlayer && p_setRotationAngles_7_.equals((Object)Minecraft.func_71410_x().field_71439_g)) {
            if (Client.moduleManager.getModule(SpinBot.class).getState()) {
                this.field_78116_c.field_78795_f = SpinBot.pitch / 57.295776f;
            } else if (rotationModule.getState() && RotationUtils.serverRotation != null) {
                this.field_78116_c.field_78795_f = RotationUtils.serverRotation.getPitch() / 57.295776f;
            }
        }
    }
}

