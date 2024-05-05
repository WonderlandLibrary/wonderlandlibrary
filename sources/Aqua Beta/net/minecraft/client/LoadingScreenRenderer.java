package net.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public class LoadingScreenRenderer
implements IProgressUpdate {
    private String message = "";
    private Minecraft mc;
    private String currentlyDisplayedText = "";
    private long systemTime = Minecraft.getSystemTime();
    private boolean loadingSuccess;
    private ScaledResolution scaledResolution;
    private Framebuffer framebuffer;

    public LoadingScreenRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.scaledResolution = new ScaledResolution(mcIn);
        this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        this.framebuffer.setFramebufferFilter(9728);
    }

    public void resetProgressAndMessage(String message) {
        this.loadingSuccess = false;
        this.displayString(message);
    }

    public void displaySavingString(String message) {
        this.loadingSuccess = true;
        this.displayString(message);
    }

    private void displayString(String message) {
        this.currentlyDisplayedText = message;
        if (!this.mc.running) {
            if (!this.loadingSuccess) {
                throw new MinecraftError();
            }
        } else {
            GlStateManager.clear((int)256);
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            if (OpenGlHelper.isFramebufferEnabled()) {
                int i = this.scaledResolution.getScaleFactor();
                GlStateManager.ortho((double)0.0, (double)(this.scaledResolution.getScaledWidth() * i), (double)(this.scaledResolution.getScaledHeight() * i), (double)0.0, (double)100.0, (double)300.0);
            } else {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                GlStateManager.ortho((double)0.0, (double)scaledresolution.getScaledWidth_double(), (double)scaledresolution.getScaledHeight_double(), (double)0.0, (double)100.0, (double)300.0);
            }
            GlStateManager.matrixMode((int)5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)-200.0f);
        }
    }

    public void displayLoadingString(String message) {
        if (!this.mc.running) {
            if (!this.loadingSuccess) {
                throw new MinecraftError();
            }
        } else {
            this.systemTime = 0L;
            this.message = message;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }

    public void setLoadingProgress(int progress) {
        if (!this.mc.running) {
            if (!this.loadingSuccess) {
                throw new MinecraftError();
            }
        } else {
            long i = Minecraft.getSystemTime();
            if (i - this.systemTime >= 100L) {
                Object object;
                this.systemTime = i;
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int j = scaledresolution.getScaleFactor();
                int k = scaledresolution.getScaledWidth();
                int l = scaledresolution.getScaledHeight();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferClear();
                } else {
                    GlStateManager.clear((int)256);
                }
                this.framebuffer.bindFramebuffer(false);
                GlStateManager.matrixMode((int)5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho((double)0.0, (double)scaledresolution.getScaledWidth_double(), (double)scaledresolution.getScaledHeight_double(), (double)0.0, (double)100.0, (double)300.0);
                GlStateManager.matrixMode((int)5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate((float)0.0f, (float)0.0f, (float)-200.0f);
                if (!OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.clear((int)16640);
                }
                boolean flag = true;
                if (Reflector.FMLClientHandler_handleLoadingScreen.exists() && (object = Reflector.call((ReflectorMethod)Reflector.FMLClientHandler_instance, (Object[])new Object[0])) != null) {
                    boolean bl = flag = !Reflector.callBoolean((Object)object, (ReflectorMethod)Reflector.FMLClientHandler_handleLoadingScreen, (Object[])new Object[]{scaledresolution});
                }
                if (flag) {
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    CustomLoadingScreen customloadingscreen = CustomLoadingScreens.getCustomLoadingScreen();
                    if (customloadingscreen != null) {
                        customloadingscreen.drawBackground(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
                    } else {
                        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                        float f = 32.0f;
                        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                        worldrenderer.pos(0.0, (double)l, 0.0).tex(0.0, (double)((float)l / f)).color(64, 64, 64, 255).endVertex();
                        worldrenderer.pos((double)k, (double)l, 0.0).tex((double)((float)k / f), (double)((float)l / f)).color(64, 64, 64, 255).endVertex();
                        worldrenderer.pos((double)k, 0.0, 0.0).tex((double)((float)k / f), 0.0).color(64, 64, 64, 255).endVertex();
                        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
                        tessellator.draw();
                    }
                    if (progress >= 0) {
                        int l1 = 100;
                        int i1 = 2;
                        int j1 = k / 2 - l1 / 2;
                        int k1 = l / 2 + 16;
                        GlStateManager.disableTexture2D();
                        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                        worldrenderer.pos((double)j1, (double)k1, 0.0).color(128, 128, 128, 255).endVertex();
                        worldrenderer.pos((double)j1, (double)(k1 + i1), 0.0).color(128, 128, 128, 255).endVertex();
                        worldrenderer.pos((double)(j1 + l1), (double)(k1 + i1), 0.0).color(128, 128, 128, 255).endVertex();
                        worldrenderer.pos((double)(j1 + l1), (double)k1, 0.0).color(128, 128, 128, 255).endVertex();
                        worldrenderer.pos((double)j1, (double)k1, 0.0).color(128, 255, 128, 255).endVertex();
                        worldrenderer.pos((double)j1, (double)(k1 + i1), 0.0).color(128, 255, 128, 255).endVertex();
                        worldrenderer.pos((double)(j1 + progress), (double)(k1 + i1), 0.0).color(128, 255, 128, 255).endVertex();
                        worldrenderer.pos((double)(j1 + progress), (double)k1, 0.0).color(128, 255, 128, 255).endVertex();
                        tessellator.draw();
                        GlStateManager.enableTexture2D();
                    }
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                    this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (float)((k - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2), (float)(l / 2 - 4 - 16), 0xFFFFFF);
                    this.mc.fontRendererObj.drawStringWithShadow(this.message, (float)((k - this.mc.fontRendererObj.getStringWidth(this.message)) / 2), (float)(l / 2 - 4 + 8), 0xFFFFFF);
                }
                this.framebuffer.unbindFramebuffer();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferRender(k * j, l * j);
                }
                this.mc.updateDisplay();
                try {
                    Thread.yield();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    public void setDoneWorking() {
    }
}
