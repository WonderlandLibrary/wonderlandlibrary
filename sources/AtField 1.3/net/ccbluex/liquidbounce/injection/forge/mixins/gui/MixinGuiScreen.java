/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.event.ClickEvent
 *  net.minecraft.util.text.event.HoverEvent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.injection.backend.ClassProviderImpl;
import net.ccbluex.liquidbounce.utils.render.ParticleUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiScreen.class})
public abstract class MixinGuiScreen {
    @Shadow
    public int field_146294_l;
    @Shadow
    public List field_146292_n;
    @Shadow
    public FontRenderer field_146289_q;
    @Shadow
    public int field_146295_m;
    @Shadow
    public Minecraft field_146297_k;

    @Shadow
    public void func_73876_c() {
    }

    @Inject(method={"drawBackground"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawClientBackground(CallbackInfo callbackInfo) {
        GlStateManager.func_179140_f();
        GlStateManager.func_179106_n();
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
        int n = scaledResolution.func_78326_a();
        int n2 = scaledResolution.func_78328_b();
        RenderUtils.drawImage(ClassProviderImpl.INSTANCE.createResourceLocation("atfield/background.png"), 0, 0, n, n2);
        ParticleUtils.drawParticles(Mouse.getX() * n / this.field_146297_k.field_71443_c, n2 - Mouse.getY() * n2 / this.field_146297_k.field_71440_d - 1);
        callbackInfo.cancel();
    }

    @Inject(method={"sendChatMessage(Ljava/lang/String;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void messageSend(String string, boolean bl, CallbackInfo callbackInfo) {
        if (string.startsWith(String.valueOf(LiquidBounce.commandManager.getPrefix())) && bl) {
            this.field_146297_k.field_71456_v.func_146158_b().func_146239_a(string);
            LiquidBounce.commandManager.executeCommands(string);
            callbackInfo.cancel();
        }
    }

    @Shadow
    public abstract void func_146276_q_();

    @Overwrite
    protected void func_146284_a(GuiButton guiButton) throws IOException {
        this.injectedActionPerformed(guiButton);
    }

    @Inject(method={"drawWorldBackground"}, at={@At(value="HEAD")})
    private void drawWorldBackground(CallbackInfo callbackInfo) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (((Boolean)hUD.getInventoryParticle().get()).booleanValue() && this.field_146297_k.field_71439_g != null) {
            ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
            int n = scaledResolution.func_78326_a();
            int n2 = scaledResolution.func_78328_b();
            ParticleUtils.drawParticles(Mouse.getX() * n / this.field_146297_k.field_71443_c, n2 - Mouse.getY() * n2 / this.field_146297_k.field_71440_d - 1);
        }
    }

    protected void injectedActionPerformed(GuiButton guiButton) {
    }

    @Inject(method={"handleComponentHover"}, at={@At(value="HEAD")})
    private void handleHoverOverComponent(ITextComponent iTextComponent, int n, int n2, CallbackInfo callbackInfo) {
        if (iTextComponent == null || iTextComponent.func_150256_b().func_150235_h() == null || !LiquidBounce.moduleManager.getModule(ComponentOnHover.class).getState()) {
            return;
        }
        Style style = iTextComponent.func_150256_b();
        ClickEvent clickEvent = style.func_150235_h();
        HoverEvent hoverEvent = style.func_150210_i();
        this.func_146283_a(Collections.singletonList("\u00a7c\u00a7l" + clickEvent.func_150669_a().func_150673_b().toUpperCase() + ": \u00a7a" + clickEvent.func_150668_b()), n, n2 - (hoverEvent != null ? 17 : 0));
    }

    @Shadow
    protected abstract void func_175272_a(ITextComponent var1, int var2, int var3);

    @Shadow
    public abstract void func_146283_a(List var1, int var2, int var3);
}

