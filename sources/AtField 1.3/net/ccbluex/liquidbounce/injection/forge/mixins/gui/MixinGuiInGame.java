/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.features.module.modules.render.BlurSettings;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGui;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiIngame.class})
public abstract class MixinGuiInGame
extends MixinGui {
    @Shadow
    @Final
    protected static ResourceLocation field_110330_c;
    @Shadow
    @Final
    protected Minecraft field_73839_d;

    @Inject(method={"renderPumpkinOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderPumpkinOverlay(CallbackInfo callbackInfo) {
        AntiBlind antiBlind = (AntiBlind)LiquidBounce.moduleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && ((Boolean)antiBlind.getPumpkinEffect().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderHotbar"}, at={@At(value="RETURN")})
    private void renderTooltipPost(ScaledResolution scaledResolution, float f, CallbackInfo callbackInfo) {
        if (!ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            BlurSettings blurSettings = (BlurSettings)LiquidBounce.moduleManager.getModule(BlurSettings.class);
            blurSettings.drawBlurShadow();
            LiquidBounce.eventManager.callEvent(new Render2DEvent(f));
            AWTFontRenderer.Companion.garbageCollectionTick();
        }
    }

    @Overwrite
    protected void func_180479_a(ScaledResolution scaledResolution, float f) {
        if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer) {
            float f2;
            int n;
            int n2;
            EntityPlayer entityPlayer = (EntityPlayer)this.field_73839_d.func_175606_aa();
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            EnumHandSide enumHandSide = entityPlayer.func_184591_cq().func_188468_a();
            int n3 = scaledResolution.func_78326_a() / 2;
            float f3 = this.field_73735_i;
            int n4 = 182;
            int n5 = 91;
            this.field_73735_i = -90.0f;
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GuiIngame.func_73734_a((int)(n3 - 91), (int)(scaledResolution.func_78328_b() - 24), (int)(n3 + 90), (int)scaledResolution.func_78328_b(), (int)Integer.MIN_VALUE);
            GuiIngame.func_73734_a((int)(n3 - 91 - 1 + entityPlayer.field_71071_by.field_70461_c * 20 + 1), (int)(scaledResolution.func_78328_b() - 24), (int)(n3 - 91 - 1 + entityPlayer.field_71071_by.field_70461_c * 20 + 22), (int)(scaledResolution.func_78328_b() - 22 - 1 + 24), (int)Integer.MAX_VALUE);
            this.field_73839_d.func_110434_K().func_110577_a(field_110330_c);
            this.field_73735_i = f3;
            GlStateManager.func_179091_B();
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            RenderHelper.func_74520_c();
            for (int i = 0; i < 9; ++i) {
                n2 = n3 - 90 + i * 20 + 2;
                n = scaledResolution.func_78328_b() - 16 - 3;
                this.func_184044_a(n2, n, f, entityPlayer, (ItemStack)entityPlayer.field_71071_by.field_70462_a.get(i));
            }
            if (this.field_73839_d.field_71474_y.field_186716_M == 2 && (f2 = this.field_73839_d.field_71439_g.func_184825_o(0.0f)) < 1.0f) {
                n2 = scaledResolution.func_78328_b() - 20;
                n = n3 + 91 + 6;
                if (enumHandSide == EnumHandSide.RIGHT) {
                    n = n3 - 91 - 22;
                }
                this.field_73839_d.func_110434_K().func_110577_a(Gui.field_110324_m);
                int n6 = (int)(f2 * 19.0f);
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                this.func_73729_b(n, n2, 0, 94, 18, 18);
                this.func_73729_b(n, n2 + 18 - n6, 18, 112 - n6, 18, n6);
            }
            RenderHelper.func_74518_a();
            GlStateManager.func_179101_C();
            GlStateManager.func_179084_k();
        }
    }

    @Shadow
    protected abstract void func_184044_a(int var1, int var2, float var3, EntityPlayer var4, ItemStack var5);

    @Inject(method={"renderScoreboard"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderScoreboard(CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(HUD.class).getState() && NoScoreboard.INSTANCE.getState()) {
            callbackInfo.cancel();
        }
    }
}

