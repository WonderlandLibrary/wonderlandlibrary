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
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.report.liquidware.modules.render.PlayerEdit;
import me.report.liquidware.modules.render.Rotate;
import me.report.liquidware.modules.render.SilentView;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
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
    public ModelRenderer field_178724_i;
    @Shadow
    public int field_78120_m;
    @Shadow
    public ModelRenderer field_78116_c;

    @Inject(method={"setRotationAngles"}, at={@At(value="FIELD", target="Lnet/minecraft/client/model/ModelBiped;swingProgress:F")})
    private void revertSwordAnimation(float p_setRotationAngles_1_, float p_setRotationAngles_2_, float p_setRotationAngles_3_, float p_setRotationAngles_4_, float p_setRotationAngles_5_, float p_setRotationAngles_6_, Entity p_setRotationAngles_7_, CallbackInfo callbackInfo) {
        if (this.field_78120_m == 3) {
            this.field_178723_h.field_78796_g = 0.0f;
        }
        if (RotationUtils.serverRotation != null && p_setRotationAngles_7_ instanceof EntityPlayer && p_setRotationAngles_7_.equals((Object)Minecraft.func_71410_x().field_71439_g)) {
            this.field_78116_c.field_78795_f = RotationUtils.serverRotation.getPitch() / 57.295776f;
        }
        if (LiquidBounce.moduleManager.getModule(PlayerEdit.class).getState()) {
            this.field_78116_c.field_78795_f = ((Float)PlayerEdit.bipedHeadRotateAngleX.get()).floatValue();
            this.field_78116_c.field_78796_g = ((Float)PlayerEdit.bipedHeadRotateAngleY.get()).floatValue();
            this.field_178723_h.field_78795_f = ((Float)PlayerEdit.bipedRightArmrotateAngleX.get()).floatValue();
            this.field_178723_h.field_78796_g = ((Float)PlayerEdit.bipedRightArmrotateAngleY.get()).floatValue();
            this.field_178724_i.field_78795_f = ((Float)PlayerEdit.BipedLeftArmrotateAngleX.get()).floatValue();
            this.field_178724_i.field_78796_g = ((Float)PlayerEdit.bipedLeftArmrotateAngleY.get()).floatValue();
        }
        if (p_setRotationAngles_7_ instanceof EntityPlayer && p_setRotationAngles_7_.equals((Object)Minecraft.func_71410_x().field_71439_g)) {
            SilentView silentView = (SilentView)LiquidBounce.moduleManager.getModule(SilentView.class);
            Rotate spinBot = (Rotate)LiquidBounce.moduleManager.getModule(Rotate.class);
            KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
            Scaffold scaffold = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class);
            if (spinBot.getState() && !((String)spinBot.getPitchMode().get()).equalsIgnoreCase("none")) {
                this.field_78116_c.field_78795_f = spinBot.getPitch() / 57.295776f;
            }
            if (silentView.getState() && ((String)silentView.getMode().get()).equals("Normal") && killAura.getTarget() != null) {
                this.field_78116_c.field_78795_f = RotationUtils.serverRotation.getPitch() / 57.295776f;
            }
            if (silentView.getState() && ((String)silentView.getMode().get()).equals("Normal") && scaffold.getState()) {
                this.field_78116_c.field_78795_f = RotationUtils.serverRotation.getPitch() / 57.295776f;
            }
        }
    }
}

