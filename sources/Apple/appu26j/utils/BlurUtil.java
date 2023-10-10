package appu26j.utils;

import org.lwjgl.opengl.GL11;

import appu26j.Apple;
import appu26j.Cache;
import appu26j.interfaces.MinecraftInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class BlurUtil implements MinecraftInterface
{
    private static ResourceLocation shader = new ResourceLocation("shaders/post/gaussian_blur.json");
    private static int lastScale, lastScaleWidth, lastScaleHeight;
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    
    static
    {
        initialize();
    }
    
    private static void initialize()
    {
        try
        {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = blurShader.getMainFramebuffer();
        }
        
        catch (Exception e)
        {
            ;
        }
    }
    
    private static void setShaderConfigs(float intensity, float blurWidth, float blurHeight)
    {
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }
    
    public static void blur(float x, float y, float width, float height, float intensity)
    {
        scissor(x, y, width, height);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        blur(intensity);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
    
    public static void blur(float intensity)
    {
        if (mc.theWorld == null)
        {
            // Don't blur for no reason and lag the game
            return;
        }
        
        ScaledResolution scaledResolution = Cache.getSR();
        int factor = scaledResolution.getScaleFactor();
        int scaleWidth = scaledResolution.getScaledWidth();
        int scaleHeight = scaledResolution.getScaledHeight();
        
        if (lastScale != factor || lastScaleWidth != scaleWidth || lastScaleHeight != scaleHeight || buffer == null || blurShader == null)
        {
            initialize();
            lastScale = factor;
            lastScaleWidth = scaleWidth;
            lastScaleHeight = scaleHeight;
        }
        
        setShaderConfigs(intensity, 0, 1);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
    }
    
    private static void scissor(float x, float y, float width, float height)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = Cache.getSR();
        x *= scaledResolution.getScaleFactor();
        y *= scaledResolution.getScaleFactor();
        width *= scaledResolution.getScaleFactor();
        height *= scaledResolution.getScaleFactor();
        GL11.glScissor((int) x, (int) (mc.displayHeight - height), (int) (width - x), (int) (height - y));
    }
    
    private static void scissor(float x, float y, float width, float height, float size)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = Cache.getSR();
        x *= scaledResolution.getScaleFactor();
        y *= scaledResolution.getScaleFactor();
        width *= scaledResolution.getScaleFactor();
        height *= scaledResolution.getScaleFactor();
        x *= size;
        y *= size;
        width *= size;
        height *= size;
        GL11.glScissor((int) x, (int) (mc.displayHeight - height), (int) (width - x), (int) (height - y));
    }
    
    public static boolean enabled()
    {
        return Apple.CLIENT.getModsManager() != null && Apple.CLIENT.getModsManager().getMod("Menu Blur") != null && Apple.CLIENT.getModsManager().getMod("Menu Blur").isEnabled();
    }
    
    public static float intensity()
    {
        return Apple.CLIENT.getModsManager() == null ? 0 : Apple.CLIENT.getModsManager().getMod("Menu Blur") == null ? 0 : Apple.CLIENT.getSettingsManager().getSetting("Intensity", Apple.CLIENT.getModsManager().getMod("Menu Blur")).getSliderValue();
    }
    
    public static boolean reduceBackgroundDarkening()
    {
        return Apple.CLIENT.getModsManager() != null && Apple.CLIENT.getModsManager().getMod("Menu Blur") != null && Apple.CLIENT.getSettingsManager().getSetting("Reduce background darkening", Apple.CLIENT.getModsManager().getMod("Menu Blur")).getCheckBoxValue(); 
    }
}
