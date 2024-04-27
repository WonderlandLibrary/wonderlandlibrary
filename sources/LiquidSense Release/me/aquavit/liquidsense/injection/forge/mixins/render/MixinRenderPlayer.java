package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.LiquidSense;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinRenderPlayer extends MixinRender {
    @Shadow
    public abstract ModelPlayer getMainModel();

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
        ModelPlayer modelplayer = this.getMainModel();

        if (clientPlayer.isSpectator()) {
            modelplayer.setInvisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        } else {
            ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
            modelplayer.setInvisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.heldItemLeft = 0;
            modelplayer.aimedBow = false;
            modelplayer.isSneak = clientPlayer.isSneaking();
            if (itemstack == null) {
                modelplayer.heldItemRight = 0;
            } else {
                modelplayer.heldItemRight = 1;
                if (clientPlayer.getItemInUseCount() > 0) {
                    EnumAction enumaction = itemstack.getItemUseAction();
                    if (enumaction == EnumAction.BLOCK) {
                        modelplayer.heldItemRight = 3;
                    } else if (enumaction == EnumAction.BOW) {
                        modelplayer.aimedBow = true;
                    }
                }
            }
        }
    }

    @Inject(method = "rotateCorpse", at = @At("HEAD"), cancellable = true)
    public void sleep1(AbstractClientPlayer bat, float p_77043_2_, float p_77043_3_, float partialTicks, CallbackInfo callbackInfo) {
        if (bat == Minecraft.getMinecraft().thePlayer && LiquidSense.moduleManager.getModule(RenderChanger.class).getState() && RenderChanger.sleeperValue.get()) {
            GlStateManager.translate(0f, 0.3f, 0f);
            GlStateManager.rotate(-Minecraft.getMinecraft().thePlayer.rotationYaw - 90f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(90f, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
            callbackInfo.cancel();
        }
    }
}
