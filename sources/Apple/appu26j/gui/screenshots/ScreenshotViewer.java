package appu26j.gui.screenshots;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import appu26j.fontrenderer.SizedFontRenderer;
import appu26j.utils.SoundUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class ScreenshotViewer extends GuiScreen
{
    private Screenshot screenshot = null;
    
    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 100).getRGB());
        float widthByTwo = this.width / 2, heightByTwo = this.height / 2;
        
        if (this.screenshot != null && !this.screenshot.getFile().exists())
        {
            this.screenshot = null;
        }
        
        if (this.screenshot == null)
        {
            SizedFontRenderer.drawString("No screenshots...", widthByTwo - (SizedFontRenderer.getStringWidth("No screenshots...", 32) / 2), heightByTwo - 16, 32, -1);
        }
        
        else
        {
            this.drawRect(widthByTwo - 257, heightByTwo - 21, widthByTwo - 227, heightByTwo + 17, this.isInsideBox(mouseX, mouseY, widthByTwo - 257, heightByTwo - 21, widthByTwo - 227, heightByTwo + 17) ? new Color(25, 25, 25, 200).getRGB() : new Color(25, 25, 25, 150).getRGB());
            this.drawRect(widthByTwo + 243, heightByTwo - 21, widthByTwo + 273, heightByTwo + 17, this.isInsideBox(mouseX, mouseY, widthByTwo + 243, heightByTwo - 21, widthByTwo + 273, heightByTwo + 17) ? new Color(25, 25, 25, 200).getRGB() : new Color(25, 25, 25, 150).getRGB());
            SizedFontRenderer.drawString("<", widthByTwo - 250, heightByTwo - 16, 32, -1);
            SizedFontRenderer.drawString(">", widthByTwo + 250, heightByTwo - 16, 32, -1);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableBlend();
            this.mc.getTextureManager().bindTexture(this.screenshot.getImage());
            int imageWidth = this.screenshot.getBufferedImage().getWidth();
            int imageHeight = this.screenshot.getBufferedImage().getHeight();
            imageWidth /= 6;
            imageHeight /= 6;
            this.drawModalRectWithCustomSizedTexture(widthByTwo - (imageWidth / 2), heightByTwo - (imageHeight / 2), 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
            SizedFontRenderer.drawString("Taken on " + this.screenshot.getTime(), widthByTwo - (SizedFontRenderer.getStringWidth("Taken on " + this.screenshot.getTime(), 16) / 2), heightByTwo + (imageHeight / 2) + 24, 16, -1);
        }
    }
    
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float widthByTwo = this.width / 2, heightByTwo = this.height / 2;
        
        if (this.screenshot != null && mouseButton == 0)
        {
            if (this.isInsideBox(mouseX, mouseY, widthByTwo - 257, heightByTwo - 21, widthByTwo - 227, heightByTwo + 17))
            {
                SoundUtil.playClickSound();
                File screenshotsFolder = new File("screenshots");
                
                if (screenshotsFolder.exists() && screenshotsFolder.listFiles() != null && screenshotsFolder.listFiles().length != 0)
                {
                    int currentIndex = -1;
                    
                    for (File file : screenshotsFolder.listFiles())
                    {
                        if (file.getName().equals(this.screenshot.getFile().getName()))
                        {
                            break;
                        }
                        
                        currentIndex++;
                    }
                    
                    if (currentIndex != -1)
                    {
                        this.screenshot = new Screenshot(screenshotsFolder.listFiles()[currentIndex]);
                    }
                }
            }
            
            if (this.isInsideBox(mouseX, mouseY, widthByTwo + 243, heightByTwo - 21, widthByTwo + 273, heightByTwo + 17))
            {
                SoundUtil.playClickSound();
                File screenshotsFolder = new File("screenshots");
                
                if (screenshotsFolder.exists() && screenshotsFolder.listFiles() != null && screenshotsFolder.listFiles().length != 0)
                {
                    int currentIndex = 1;
                    
                    for (File file : screenshotsFolder.listFiles())
                    {
                        if (file.getName().equals(this.screenshot.getFile().getName()))
                        {
                            break;
                        }
                        
                        currentIndex++;
                    }
                    
                    if (currentIndex < screenshotsFolder.listFiles().length)
                    {
                        this.screenshot = new Screenshot(screenshotsFolder.listFiles()[currentIndex]);
                    }
                }
            }
        }
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui()
    {
        super.initGui();
        
        if (this.screenshot == null)
        {
            File screenshotsFolder = new File("screenshots");
            
            if (screenshotsFolder.exists() && screenshotsFolder.listFiles() != null && screenshotsFolder.listFiles().length != 0)
            {
                this.screenshot = new Screenshot(screenshotsFolder.listFiles()[0]);
            }
        }
    }
    
    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    public boolean isInsideBox(int mouseX, int mouseY, float x, float y, float width, float height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
