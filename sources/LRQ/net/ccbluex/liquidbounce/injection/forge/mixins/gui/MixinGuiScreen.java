/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
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
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImplKt;
import net.ccbluex.liquidbounce.ui.client.GuiBackground;
import net.ccbluex.liquidbounce.utils.render.ParticleUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.BackgroundShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
    public Minecraft field_146297_k;
    @Shadow
    public List<GuiButton> field_146292_n;
    @Shadow
    public int field_146294_l;
    @Shadow
    public int field_146295_m;
    @Shadow
    public FontRenderer field_146289_q;

    @Shadow
    public void func_73876_c() {
    }

    @Shadow
    protected abstract void func_175272_a(ITextComponent var1, int var2, int var3);

    @Shadow
    public abstract void func_146283_a(List<String> var1, int var2, int var3);

    @Shadow
    public abstract void func_146276_q_();

    @Inject(method={"drawWorldBackground"}, at={@At(value="HEAD")})
    private void drawWorldBackground(CallbackInfo callbackInfo) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (((Boolean)hud.getInventoryParticle().get()).booleanValue() && this.field_146297_k.field_71439_g != null) {
            ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
            int width = scaledResolution.func_78326_a();
            int height = scaledResolution.func_78328_b();
            ParticleUtils.drawParticles(Mouse.getX() * width / this.field_146297_k.field_71443_c, height - Mouse.getY() * height / this.field_146297_k.field_71440_d - 1);
        }
    }

    @Inject(method={"drawBackground"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawClientBackground(CallbackInfo callbackInfo) {
        GlStateManager.func_179140_f();
        GlStateManager.func_179106_n();
        if (GuiBackground.Companion.getEnabled()) {
            if (LiquidBounce.INSTANCE.getBackground() == null) {
                BackgroundShader.BACKGROUND_SHADER.startShader();
                Tessellator instance = Tessellator.func_178181_a();
                BufferBuilder worldRenderer = instance.func_178180_c();
                worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
                worldRenderer.func_181662_b(0.0, (double)this.field_146295_m, 0.0).func_181675_d();
                worldRenderer.func_181662_b((double)this.field_146294_l, (double)this.field_146295_m, 0.0).func_181675_d();
                worldRenderer.func_181662_b((double)this.field_146294_l, 0.0, 0.0).func_181675_d();
                worldRenderer.func_181662_b(0.0, 0.0, 0.0).func_181675_d();
                instance.func_78381_a();
                BackgroundShader.BACKGROUND_SHADER.stopShader();
            } else {
                ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
                int width = scaledResolution.func_78326_a();
                int height = scaledResolution.func_78328_b();
                this.field_146297_k.func_110434_K().func_110577_a(ResourceLocationImplKt.unwrap(LiquidBounce.INSTANCE.getBackground()));
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                Gui.func_152125_a((int)0, (int)0, (float)0.0f, (float)0.0f, (int)width, (int)height, (int)width, (int)height, (float)width, (float)height);
            }
            if (GuiBackground.Companion.getParticles()) {
                ParticleUtils.drawParticles(Mouse.getX() * this.field_146294_l / this.field_146297_k.field_71443_c, this.field_146295_m - Mouse.getY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1);
            }
            callbackInfo.cancel();
        }
    }

    @Inject(method={"drawBackground"}, at={@At(value="RETURN")})
    private void drawParticles(CallbackInfo callbackInfo) {
        if (GuiBackground.Companion.getParticles()) {
            ParticleUtils.drawParticles(Mouse.getX() * this.field_146294_l / this.field_146297_k.field_71443_c, this.field_146295_m - Mouse.getY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1);
        }
    }

    @Inject(method={"sendChatMessage(Ljava/lang/String;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void messageSend(String msg, boolean addToChat, CallbackInfo callbackInfo) {
        if (msg.startsWith(String.valueOf(LiquidBounce.commandManager.getPrefix())) && addToChat) {
            this.field_146297_k.field_71456_v.func_146158_b().func_146239_a(msg);
            LiquidBounce.commandManager.executeCommands(msg);
            callbackInfo.cancel();
        }
    }

    @Inject(method={"handleComponentHover"}, at={@At(value="HEAD")})
    private void handleHoverOverComponent(ITextComponent component, int x, int y, CallbackInfo callbackInfo) {
        if (component == null || component.func_150256_b().func_150235_h() == null || !LiquidBounce.moduleManager.getModule(ComponentOnHover.class).getState()) {
            return;
        }
        Style chatStyle = component.func_150256_b();
        ClickEvent clickEvent = chatStyle.func_150235_h();
        HoverEvent hoverEvent = chatStyle.func_150210_i();
        this.func_146283_a(Collections.singletonList("\u00a7c\u00a7l" + clickEvent.func_150669_a().func_150673_b().toUpperCase() + ": \u00a7a" + clickEvent.func_150668_b()), x, y - (hoverEvent != null ? 17 : 0));
    }

    @Overwrite
    protected void func_146284_a(GuiButton button) throws IOException {
        this.injectedActionPerformed(button);
    }

    protected void injectedActionPerformed(GuiButton button) {
    }
}

