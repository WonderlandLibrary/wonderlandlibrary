package net.minecraft.client;

import appu26j.Cache;
import net.minecraft.client.gui.ScaledResolution; import appu26j.Cache;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;

public class LoadingScreenRenderer implements IProgressUpdate
{
    private String message = "";

    /** A reference to the Minecraft object. */
    private Minecraft mc;

    /**
     * The text currently displayed (i.e. the argument to the last call to printText or displayString)
     */
    private String currentlyDisplayedText = "";

    /** The system's time represented in milliseconds. */
    private long systemTime = Minecraft.getSystemTime();

    /** True if the loading ended with a success */
    private boolean loadingSuccess;
    private ScaledResolution scaledResolution;
    private Framebuffer framebuffer;

    public LoadingScreenRenderer(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.scaledResolution = Cache.getSR();
        this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        this.framebuffer.setFramebufferFilter(9728);
    }

    /**
     * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
     * and the WorkingString to "working...".
     */
    public void resetProgressAndMessage(String message)
    {
        this.loadingSuccess = false;
        this.displayString(message);
    }

    /**
     * Shows the 'Saving level' string.
     */
    public void displaySavingString(String message)
    {
        this.loadingSuccess = true;
        this.displayString(message);
    }

    private void displayString(String message)
    {
        this.currentlyDisplayedText = message;

        if (!this.mc.running)
        {
            if (!this.loadingSuccess)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();

            if (OpenGlHelper.isFramebufferEnabled())
            {
                int i = this.scaledResolution.getScaleFactor();
                GlStateManager.ortho(0.0D, (double)(this.scaledResolution.getScaledWidth() * i), (double)(this.scaledResolution.getScaledHeight() * i), 0.0D, 100.0D, 300.0D);
            }
            else
            {
                ScaledResolution scaledresolution = Cache.getSR();
                GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            }

            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);
        }
    }

    /**
     * Displays a string on the loading screen supposed to indicate what is being done currently.
     */
    public void displayLoadingString(String message)
    {
        if (!this.mc.running)
        {
            if (!this.loadingSuccess)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            this.systemTime = 0L;
            this.message = message;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    public void setLoadingProgress(int progress)
    {
        if (!this.mc.running)
        {
            if (!this.loadingSuccess)
            {
                throw new MinecraftError();
            }
        }
    }

    public void setDoneWorking()
    {
    }
}
