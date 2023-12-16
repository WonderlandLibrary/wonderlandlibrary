// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.splash;

import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import moonsense.utils.CustomFontRenderer;

public class SplashScreen
{
    private static final int MAX = 3;
    private static int PROGRESS;
    private static String CURRENT;
    private static CustomFontRenderer cfr;
    
    static {
        SplashScreen.PROGRESS = 0;
        SplashScreen.CURRENT = "";
    }
    
    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
            return;
        }
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }
    
    public static void setProgress(final int givenProgress, final String givenText) {
        SplashScreen.PROGRESS = givenProgress;
        SplashScreen.CURRENT = givenText;
        update();
    }
    
    public static void drawSplash(final TextureManager tm) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final int scaleFactor = sr.getScaleFactor();
        final Framebuffer fb = new Framebuffer(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor, true);
        fb.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, sr.getScaledWidth(), sr.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.func_179117_G();
        GL11.glColor3f(0.0f, 0.0f, 0.0f);
        GL11.glVertex2i(0, 0);
        GL11.glVertex2i(0, sr.getScaledWidth());
        GL11.glVertex2i(sr.getScaledWidth(), sr.getScaledHeight());
        GL11.glVertex2i(sr.getScaledHeight(), 0);
        drawProgress(sr);
        fb.unbindFramebuffer();
        fb.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        Minecraft.getMinecraft().func_175601_h();
    }
    
    private static void drawProgress(final ScaledResolution sr) {
        GuiUtils.drawRoundedOutline(sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 - 7, sr.getScaledWidth() / 2 + 100, sr.getScaledHeight() / 2 + 7, 5.0f, 2.0f, MoonsenseClient.getMainColor(255));
        final int totalWidth = 196;
        final float progress = (float)SplashScreen.PROGRESS;
        final float currentWidth = totalWidth * (progress / 3.0f);
        if (SplashScreen.cfr == null) {
            SplashScreen.cfr = new CustomFontRenderer("Raleway Black", 25.0f);
        }
        if (SplashScreen.PROGRESS != 0) {
            GlStateManager.func_179117_G();
            resetTextureState();
            GuiUtils.drawRoundedRect((float)(sr.getScaledWidth() / 2 - 98), (float)(sr.getScaledHeight() / 2 - 5), sr.getScaledWidth() / 2 - 98 + currentWidth, (float)(sr.getScaledHeight() / 2 + 5), 4.0f, MoonsenseClient.getMainColor(255));
            GlStateManager.func_179117_G();
            resetTextureState();
        }
        GlStateManager.func_179117_G();
        resetTextureState();
    }
    
    private static void resetTextureState() {
    }
}
