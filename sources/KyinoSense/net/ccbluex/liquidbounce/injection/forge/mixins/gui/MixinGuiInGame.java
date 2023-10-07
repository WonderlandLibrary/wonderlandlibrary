/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import me.report.liquidware.modules.player.AutoHypixel;
import me.report.liquidware.modules.render.Camera;
import me.report.liquidware.modules.render.Crosshair;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiIngame.class})
public abstract class MixinGuiInGame {
    @Shadow
    protected abstract void func_175184_a(int var1, int var2, int var3, float var4, EntityPlayer var5);

    @Inject(method={"renderScoreboard"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderScoreboard(ScoreObjective scoreObjective, ScaledResolution scaledResolution, CallbackInfo callbackInfo) {
        Camera camera;
        if (scoreObjective != null) {
            AutoHypixel.gameMode = ColorUtils.stripColor(scoreObjective.func_96678_d());
        }
        if ((camera = (Camera)LiquidBounce.moduleManager.getModule(Camera.class)).getState() && ((Boolean)camera.getScoreBoard().get()).booleanValue() && ((Boolean)camera.getAntiBlindValue().get()).booleanValue() || LiquidBounce.moduleManager.getModule(HUD.class).getState()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderBossHealth"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderBossHealth(CallbackInfo callbackInfo) {
        Camera camera = (Camera)LiquidBounce.moduleManager.getModule(Camera.class);
        if (camera.getState() && ((Boolean)camera.getBossHealth().get()).booleanValue() && ((Boolean)camera.getAntiBlindValue().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"showCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void injectCrosshair(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        Crosshair crossHair = (Crosshair)LiquidBounce.moduleManager.getModule(Crosshair.class);
        if (crossHair.getState() && ((Boolean)crossHair.noVanillaCH.get()).booleanValue()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"renderTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer && hud.getState() && ((Boolean)hud.blackHotbarValue.get()).booleanValue()) {
            EntityPlayer entityPlayer = (EntityPlayer)Minecraft.func_71410_x().func_175606_aa();
            int middleScreen = sr.func_78326_a() / 2;
            if (!((Boolean)HUD.hotbarof.get()).booleanValue()) {
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GuiIngame.func_73734_a((int)(middleScreen - 91), (int)(sr.func_78328_b() - 24), (int)(middleScreen + 90), (int)sr.func_78328_b(), (int)Integer.MIN_VALUE);
                GuiIngame.func_73734_a((int)(middleScreen - 91 - 1 + entityPlayer.field_71071_by.field_70461_c * 20 + 1), (int)(sr.func_78328_b() - 24), (int)(middleScreen - 91 - 1 + entityPlayer.field_71071_by.field_70461_c * 20 + 22), (int)(sr.func_78328_b() - 22 - 1 + 24), (int)Integer.MAX_VALUE);
                GlStateManager.func_179091_B();
                GlStateManager.func_179147_l();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderHelper.func_74520_c();
                for (int j = 0; j < 9; ++j) {
                    int k = sr.func_78326_a() / 2 - 90 + j * 20 + 2;
                    int l = sr.func_78328_b() - 16 - 3;
                    this.func_175184_a(j, k, l, partialTicks, entityPlayer);
                }
                AWTFontRenderer.Companion.garbageCollectionTick();
            }
            RenderHelper.func_74518_a();
            GlStateManager.func_179101_C();
            GlStateManager.func_179084_k();
            LiquidBounce.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderTooltip"}, at={@At(value="RETURN")})
    private void renderTooltipPost(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            LiquidBounce.eventManager.callEvent(new Render2DEvent(partialTicks));
            AWTFontRenderer.Companion.garbageCollectionTick();
        }
    }

    @Inject(method={"renderPumpkinOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderPumpkinOverlay(CallbackInfo callbackInfo) {
        Camera camera = (Camera)LiquidBounce.moduleManager.getModule(Camera.class);
        if (camera.getState() && ((Boolean)camera.getPumpkinEffect().get()).booleanValue() && ((Boolean)camera.getAntiBlindValue().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

