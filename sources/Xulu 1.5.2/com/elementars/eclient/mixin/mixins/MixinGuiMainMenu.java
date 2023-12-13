// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.module.Module;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import com.elementars.eclient.util.GLSLSandboxShader;
import com.elementars.eclient.util.GLSLShaders;
import java.util.Random;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.core.TitleScreenShader;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiScreen;

@Mixin({ GuiMainMenu.class })
public abstract class MixinGuiMainMenu extends GuiScreen
{
    @Shadow
    protected abstract void renderSkybox(final int p0, final int p1, final float p2);
    
    @Inject(method = { "initGui" }, at = { @At("RETURN") }, cancellable = true)
    public void initGui(final CallbackInfo info) {
        try {
            if (Xulu.MODULE_MANAGER.getModuleT(TitleScreenShader.class).mode.getValue().equalsIgnoreCase("Random")) {
                final Random random = new Random();
                final GLSLShaders[] shaders = GLSLShaders.values();
                Xulu.backgroundShader = new GLSLSandboxShader(shaders[random.nextInt(shaders.length)].get());
            }
            else {
                Xulu.backgroundShader = new GLSLSandboxShader(Xulu.MODULE_MANAGER.getModuleT(TitleScreenShader.class).shader.getValue().get());
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to load background shader", e);
        }
        this.buttonList.add(new GuiButton(932, 5, 55, this.fontRenderer.getStringWidth("2b2tpvp.net") + 10, 20, "2b2tpvp.net"));
        this.buttonList.add(new GuiButton(284, 5, 75, this.fontRenderer.getStringWidth("2b2t.org") + 10, 20, "2b2t.org"));
        Xulu.initTime = System.currentTimeMillis();
    }
    
    @Inject(method = { "actionPerformed" }, at = { @At("HEAD") }, cancellable = true)
    public void actionPerformed(final GuiButton button, final CallbackInfo info) {
        if (button.id == 932) {
            this.mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)this, this.mc, "2b2tpvp.net", 25565));
        }
        if (button.id == 284) {
            this.mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)this, this.mc, "2b2t.org", 25565));
        }
    }
    
    @Redirect(method = { "drawScreen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V"))
    private void voided(final GuiMainMenu guiMainMenu, final int mouseX, final int mouseY, final float partialTicks) {
        if (!Xulu.MODULE_MANAGER.getModule(TitleScreenShader.class).isToggled()) {
            this.renderSkybox(mouseX, mouseY, partialTicks);
        }
    }
    
    @Redirect(method = { "drawScreen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 0))
    private void noRect1(final GuiMainMenu guiMainMenu, final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        if (!Xulu.MODULE_MANAGER.getModule(TitleScreenShader.class).isToggled()) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }
    
    @Redirect(method = { "drawScreen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 1))
    private void noRect2(final GuiMainMenu guiMainMenu, final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        if (!Xulu.MODULE_MANAGER.getModule(TitleScreenShader.class).isToggled()) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }
    
    @Inject(method = { "drawScreen" }, at = { @At("HEAD") }, cancellable = true)
    public void drawScreenShader(final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        if (Xulu.MODULE_MANAGER.getModule(TitleScreenShader.class).isToggled()) {
            GlStateManager.disableCull();
            Xulu.backgroundShader.useShader(this.width * 2, this.height * 2, (float)(mouseX * 2), (float)(mouseY * 2), (System.currentTimeMillis() - Xulu.initTime) / 1000.0f);
            GL11.glBegin(7);
            GL11.glVertex2f(-1.0f, -1.0f);
            GL11.glVertex2f(-1.0f, 1.0f);
            GL11.glVertex2f(1.0f, 1.0f);
            GL11.glVertex2f(1.0f, -1.0f);
            GL11.glEnd();
            GL20.glUseProgram(0);
        }
    }
}
