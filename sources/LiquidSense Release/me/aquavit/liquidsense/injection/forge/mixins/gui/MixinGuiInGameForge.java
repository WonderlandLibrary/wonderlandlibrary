package me.aquavit.liquidsense.injection.forge.mixins.gui;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.modules.hud.HeadLogo;
import me.aquavit.liquidsense.module.modules.hud.Hotbar;
import me.aquavit.liquidsense.utils.render.BlurBuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiInGameForge extends MixinGuiInGame {

    @Inject(method = "renderHealth", at = @At("HEAD"), cancellable = true, remap = false)
    public void renderPlayerStats(CallbackInfo callbackInfo) {
        HeadLogo headLogo = (HeadLogo) LiquidSense.moduleManager.getModule(HeadLogo.class);
        if (headLogo.getState() && headLogo.health.get()) callbackInfo.cancel();
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true, remap = false)
    public void renderFood(CallbackInfo callbackInfo) {
        HeadLogo headLogo = (HeadLogo) LiquidSense.moduleManager.getModule(HeadLogo.class);
        if (headLogo.getState() && headLogo.food.get()) callbackInfo.cancel();
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true, remap = false)
    public void renderArmor(CallbackInfo callbackInfo) {
        HeadLogo headLogo = (HeadLogo) LiquidSense.moduleManager.getModule(HeadLogo.class);
        if (headLogo.getState() && headLogo.armor.get()) callbackInfo.cancel();
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true, remap = false)
    public void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        final Hotbar hotbar = (Hotbar) LiquidSense.moduleManager.getModule(Hotbar.class);
        if (hotbar.getState() && hotbar.cancelHotbar.get()) {
            if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer)
                BlurBuffer.updateBlurBuffer(20f,true);
            LiquidSense.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }

    }
}
