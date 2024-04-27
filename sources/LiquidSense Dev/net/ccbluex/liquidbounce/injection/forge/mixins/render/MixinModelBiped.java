/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.AquaVit.liquidSense.modules.misc.Animations;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateModelEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
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
import net.ccbluex.liquidbounce.event.UpdateModelEvent;

import java.util.UUID;

@Mixin(ModelBiped.class)
@SideOnly(Side.CLIENT)
public class MixinModelBiped {

    @Shadow
    public ModelRenderer bipedRightArm;

    @Shadow
    public int heldItemRight;

    @Shadow
    public ModelRenderer bipedHead;

    @Inject(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F"))
    private void revertSwordAnimation(float p_setRotationAngles_1_, float p_setRotationAngles_2_, float p_setRotationAngles_3_, float p_setRotationAngles_4_, float p_setRotationAngles_5_, float p_setRotationAngles_6_, Entity p_setRotationAngles_7_, CallbackInfo callbackInfo) {
        Rotations ra = (Rotations) LiquidBounce.moduleManager.getModule(Rotations.class);
        Animations animations = (Animations) LiquidBounce.moduleManager.getModule(Animations.class);
        UUID uuid = p_setRotationAngles_7_.getUniqueID();
        EntityPlayer entityPlayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(uuid);
        Aura killAura = (Aura) LiquidBounce.moduleManager.getModule(Aura.class);

        if (ra.getState()&&ra.getModeValue().get().equalsIgnoreCase("LiquidBounce") && RotationUtils.serverRotation != null && p_setRotationAngles_7_ instanceof EntityPlayer
                && p_setRotationAngles_7_.equals(Minecraft.getMinecraft().thePlayer)) {
            this.bipedHead.rotateAngleX = RotationUtils.serverRotation.getPitch() / (180F / (float) Math.PI);
        }

        if (entityPlayer == Minecraft.getMinecraft().thePlayer && killAura.getBlockingStatus() || Minecraft.getMinecraft().thePlayer.isBlocking()) {
            switch (animations.GodMode.get()) {
                case "1.7":
                    this.bipedRightArm.rotateAngleY = 0.0F;
                    break;
                case "1.8":
                    break;
                case "Dev":
                    this.bipedRightArm.rotateAngleX = (this.bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * (float) this.heldItemRight) / 2;
                    this.bipedRightArm.rotateAngleY = (-0.5235988F) / 2;
                    break;
            }
        }
    }

}