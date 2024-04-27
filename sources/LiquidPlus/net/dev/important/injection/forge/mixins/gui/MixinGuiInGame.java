/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraft.client.gui.GuiPlayerTabOverlay
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.injection.forge.mixins.gui;

import net.dev.important.Client;
import net.dev.important.event.Render2DEvent;
import net.dev.important.gui.font.AWTFontRenderer;
import net.dev.important.injection.forge.mixins.gui.MixinGui;
import net.dev.important.modules.module.modules.misc.AutoHypixel;
import net.dev.important.modules.module.modules.render.AntiBlind;
import net.dev.important.modules.module.modules.render.Crosshair;
import net.dev.important.modules.module.modules.render.HUD;
import net.dev.important.utils.ClassUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiIngame.class})
public abstract class MixinGuiInGame
extends MixinGui {
    @Shadow
    @Final
    protected static ResourceLocation field_110330_c;
    @Shadow
    public GuiPlayerTabOverlay field_175196_v;
    public boolean shouldCallPop = true;

    @Shadow
    protected abstract void func_175184_a(int var1, int var2, int var3, float var4, EntityPlayer var5);

    @Inject(method={"showCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void injectCrosshair(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        Crosshair crossHair = (Crosshair)Client.moduleManager.getModule(Crosshair.class);
        if (crossHair.getState() && ((Boolean)crossHair.noVanillaCH.get()).booleanValue()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"renderScoreboard"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderScoreboard(ScoreObjective scoreObjective, ScaledResolution scaledResolution, CallbackInfo callbackInfo) {
        AntiBlind antiBlind;
        if (scoreObjective != null) {
            AutoHypixel.gameMode = ColorUtils.stripColor(scoreObjective.func_96678_d());
        }
        if ((antiBlind = (AntiBlind)Client.moduleManager.getModule(AntiBlind.class)).getState() && ((Boolean)antiBlind.getScoreBoard().get()).booleanValue() || Client.moduleManager.getModule(HUD.class).getState()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderBossHealth"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderBossHealth(CallbackInfo callbackInfo) {
        AntiBlind antiBlind = (AntiBlind)Client.moduleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && ((Boolean)antiBlind.getBossHealth().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        HUD hud = (HUD)Client.moduleManager.getModule(HUD.class);
        GlStateManager.func_179109_b((float)0.0f, (float)(-RenderUtils.yPosOffset), (float)0.0f);
        if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer && hud.getState()) {
            Minecraft mc = Minecraft.func_71410_x();
            EntityPlayer entityPlayer = (EntityPlayer)mc.func_175606_aa();
            boolean blackHB = hud.getState() && (Boolean)hud.getBlackHotbarValue().get() != false;
            int middleScreen = sr.func_78326_a() / 2;
            float posInv = hud.getAnimPos((float)entityPlayer.field_71071_by.field_70461_c * 20.0f);
            GlStateManager.func_179117_G();
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            mc.func_110434_K().func_110577_a(field_110330_c);
            float f = this.field_73735_i;
            this.field_73735_i = -90.0f;
            GlStateManager.func_179117_G();
            if (blackHB) {
                RenderUtils.originalRoundedRect(middleScreen - 91, sr.func_78328_b() - 2, middleScreen + 91, sr.func_78328_b() - 22, 3.0f, Integer.MIN_VALUE);
                RenderUtils.originalRoundedRect((float)(middleScreen - 91) + posInv, sr.func_78328_b() - 2, (float)(middleScreen - 91) + posInv + 22.0f, sr.func_78328_b() - 22, 3.0f, Integer.MAX_VALUE);
            } else {
                this.func_175174_a((float)middleScreen - 91.0f, sr.func_78328_b() - 22, 0, 0, 182, 22);
                this.func_175174_a((float)middleScreen - 91.0f + posInv - 1.0f, sr.func_78328_b() - 22 - 1, 0, 22, 24, 22);
            }
            this.field_73735_i = f;
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179091_B();
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            RenderHelper.func_74520_c();
            for (int j = 0; j < 9; ++j) {
                int k = sr.func_78326_a() / 2 - 90 + j * 20 + 2;
                int l = sr.func_78328_b() - 16 - (blackHB ? 4 : 3);
                this.func_175184_a(j, k, l, partialTicks, entityPlayer);
            }
            RenderHelper.func_74518_a();
            GlStateManager.func_179101_C();
            GlStateManager.func_179084_k();
            GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
            Client.eventManager.callEvent(new Render2DEvent(partialTicks));
            AWTFontRenderer.Companion.garbageCollectionTick();
            this.shouldCallPop = false;
            callbackInfo.cancel();
            return;
        }
        this.shouldCallPop = true;
    }

    @Inject(method={"renderTooltip"}, at={@At(value="RETURN")})
    private void renderTooltipPost(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        if (this.shouldCallPop) {
            GlStateManager.func_179109_b((float)0.0f, (float)RenderUtils.yPosOffset, (float)0.0f);
        }
        if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            Client.eventManager.callEvent(new Render2DEvent(partialTicks));
            AWTFontRenderer.Companion.garbageCollectionTick();
        }
    }

    @Inject(method={"renderPumpkinOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderPumpkinOverlay(CallbackInfo callbackInfo) {
        AntiBlind antiBlind = (AntiBlind)Client.moduleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && ((Boolean)antiBlind.getPumpkinEffect().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

