package appu26j.gui;

import java.awt.Color;

import appu26j.Cache;
import appu26j.fontrenderer.SizedFontRenderer;
import appu26j.interfaces.MinecraftInterface;
import appu26j.utils.Animation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class LoadingScreen implements MinecraftInterface
{
    private static final ResourceLocation resourceLocation = new ResourceLocation("panorama.png");
    private static String progress = "";
    private static String master = "Minecraft";
    private static int currentStep = 0;
    private static final int totalSteps = 12;
    private static final Animation animation = new Animation(0);

    /*
    * Set the current step
    *  */
    public static void setCurrentStep(int currentStep) {
        LoadingScreen.currentStep = currentStep;
    }

    /*
    * Set the master to the current step
    * */
    public static void setMaster(String master) {
        LoadingScreen.master = master;
    }

    /**
     * Draws the loading screen for Apple Client
     */
    public static void drawSplash(TextureManager textureManager)
    {
        ScaledResolution scaledresolution = Cache.getSR();
        int i = scaledresolution.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight() - 2, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        textureManager.bindTexture(resourceLocation);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        textureManager.bindTexture(new ResourceLocation("icons/icon_32x32.png"));
        Gui.drawModalRectWithCustomSizedTexture((scaledresolution.getScaledWidth() / 2) - 48, (scaledresolution.getScaledHeight() / 2) - 64, 0, 0, 96, 96, 96, 96);
        drawProgress(scaledresolution);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
        mc.updateDisplay();
    }



    public static void setProgress(String progress)
    {
        LoadingScreen.progress = progress;
    }
    
    private static void drawProgress(ScaledResolution scaledresolution)
    {
        if (mc.gameSettings == null || mc.getTextureManager() == null || progress.isEmpty())
        {
            return;
        }

        int i = -5;

        SizedFontRenderer.drawString(master, 15, scaledresolution.getScaledHeight() - 42 + i, 16, -1);
        SizedFontRenderer.drawString(progress, 15, scaledresolution.getScaledHeight() - 22 + i, 8, -1);

        /* I removed it for now, add it back if you want.
                                                    -luna */
//        Gui.drawRect(0, scaledresolution.getScaledHeight() - 2, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), new Color(0, 0, 10).getRGB());

        double calculatedProgress = (((double) currentStep / totalSteps)) * (scaledresolution.getScaledWidth() + 72);
        System.out.println("[DEBUG] Calculated progress: " + calculatedProgress);

        // For some reason this does not work.
        animation.setAnimation((float) calculatedProgress, 10);

        Gui.drawRect(15, scaledresolution.getScaledHeight() - 10 + i, (int) scaledresolution.getScaledWidth() - 15, scaledresolution.getScaledHeight() - 8 + i, new Color(37, 37, 37).getRGB());
        Gui.drawRect(15, scaledresolution.getScaledHeight() - 10 + i, (int) animation.getValue(), scaledresolution.getScaledHeight() - 8 + i, new Color(0, 255, 195).getRGB());
    }
    
    public static String getProgress()
    {
        return progress;
    }
}
