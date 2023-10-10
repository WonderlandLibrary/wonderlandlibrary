package appu26j.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import appu26j.Apple;
import appu26j.mods.visuals.FPSSpoofer;
import appu26j.utils.MusicUtil;
import appu26j.utils.SoundUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import quexii.RoundedUtil;

public class MusicPlayerGUI extends GuiScreen
{
    private float index = 0, volume = 25, scrollIndex = 0, maxScrollIndex = -1, scrollDelta = 0;
    private boolean closingGui = false, previousPlaying = false, dragging = false;
    private File musicFolder = new File("music");
    private File currentSong = null;
    private int temp = -1;
    
    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        if (this.closingGui)
        {
            if (this.index > 0)
            {
                float fps = this.mc.getDebugFPS();
                FPSSpoofer fpsSpoofer = (FPSSpoofer) Apple.CLIENT.getModsManager().getMod("FPS Spoofer");
                
                if (fpsSpoofer.isEnabled())
                {
                    fps /= fpsSpoofer.getSetting("Multipler").getSliderValue();
                }
                
                float delta = 1F / fps;
                this.index -= 7.5F * delta;
                
                if (this.index < 0)
                {
                    this.index = 0;
                }
                
                if (this.index == 0)
                {
                    Keyboard.enableRepeatEvents(false);
                    this.mc.displayGuiScreen(null);

                    if (this.mc.currentScreen == null)
                    {
                        this.mc.setIngameFocus();
                    }
                    
                    this.closingGui = false;
                }
            }
        }
        
        else
        {
            if (this.index < 1)
            {
                float fps = this.mc.getDebugFPS();
                FPSSpoofer fpsSpoofer = (FPSSpoofer) Apple.CLIENT.getModsManager().getMod("FPS Spoofer");
                
                if (fpsSpoofer.isEnabled())
                {
                    fps /= fpsSpoofer.getSetting("Multipler").getSliderValue();
                }
                
                float delta = 1F / fps;
                this.index += 7.5F * delta;
                
                if (this.index > 1)
                {
                    this.index = 1;
                }
            }
        }
        
        if (this.scrollDelta != 0)
        {
            this.scrollIndex -= this.scrollDelta;
            
            if (this.scrollDelta < 0)
            {
                float delta = 1F / this.mc.getDebugFPS();
                this.scrollDelta += 25 * delta;
                
                if (this.scrollDelta > 0)
                {
                    this.scrollDelta = 0;
                }
                
                if (this.scrollIndex > 0)
                {
                    this.scrollIndex = 0;
                }
            }
            
            else
            {
                float delta = 1F / this.mc.getDebugFPS();
                this.scrollDelta -= 25 * delta;
                
                if (this.scrollDelta < 0)
                {
                    this.scrollDelta = 0;
                }
                
                if (this.scrollIndex < this.maxScrollIndex)
                {
                    this.scrollIndex = this.maxScrollIndex;
                }
            }
        }
        
        float width = this.width / 2;
        float height = this.height / 2;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.85F + (this.index * 0.15F), 0.85F + (this.index * 0.15F), 0.85F + (this.index * 0.15F));
        width /= 0.85F + (this.index * 0.15F);
        height /= 0.85F + (this.index * 0.15F);
        mouseX /= 0.85F + (this.index * 0.15F);
        mouseY /= 0.85F + (this.index * 0.15F);
        int alpha = (int) (this.index * 255);
        Color temp1 = new Color(this.backgroundColourDarkened, true);
        Color temp2 = new Color(this.backgroundColourLightened, true);
        Color temp3 = new Color(this.backgroundColour, true);
        Color backgroundColourLightened = new Color(temp2.getRed(), temp2.getGreen(), temp2.getBlue(), (int) (this.index * 200));
        Color backgroundColourDarkened = new Color(temp1.getRed(), temp1.getGreen(), temp1.getBlue(), (int) (this.index * 200));
        Color backgroundColour = new Color(temp3.getRed(), temp3.getGreen(), temp3.getBlue(), (int) (this.index * 200));
        this.drawRect(0, 0, this.width / (0.85F + (this.index * 0.15F)), this.height / (0.85F + (this.index * 0.15F)), new Color(0, 0, 0, (int) (this.index * 75)).getRGB());
        RoundedUtil.drawRoundedRect(width - 200, height - 140, width + 200, height + 140, 5, backgroundColourDarkened.getRGB());
        this.drawStringAlpha("Music Player", width - (this.getStringWidth("Music Player", 16) / 2), height - 130, 16, -1, alpha);
        RoundedUtil.drawRoundedRect(width + 85, height - 130, width + 190, height - 110, 2, this.isInsideBox(mouseX, mouseY, width + 85, height - 130, width + 190, height - 110) ? new Color(75, 75, 100, (int) (this.index * 75)).getRGB() :  new Color(0, 0, 25, (int) (this.index * 75)).getRGB());
        this.drawStringAlpha("Open Music Folder", (width + 137.5F) - (this.getStringWidth("Open Music Folder") / 2), height - 124, -1, alpha);
        float xOffset = 0, yOffset = 0;
        boolean aBoolean = false;
        width += 5;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        this.scissor(width - 200, height - 100, width + 200, height + 115, 0.85F + (this.index * 0.15F));
        height += this.scrollIndex;
        
        for (File file : this.musicFolder.listFiles())
        {
            if (this.temp != this.musicFolder.listFiles().length)
            {
                this.maxScrollIndex = -1;
                this.temp = this.musicFolder.listFiles().length;
            }
            
            if (!aBoolean)
            {
                aBoolean = true;
            }
            
            if (xOffset != 0 && xOffset % 3 == 0)
            {
                yOffset += 115;
                xOffset = 0;
            }
            
            RoundedUtil.drawRoundedRect((width - 170) + xOffset, (height - 100) + yOffset, (width - 170) + xOffset + 100, (height - 80) + yOffset + 80, 3, this.isInsideBox(mouseX, mouseY, (width - 170) + xOffset, (height - 100) + yOffset, (width - 170) + xOffset + 100, (height - 80) + yOffset + 80) ? backgroundColour.getRGB() : backgroundColourLightened.getRGB());
            ArrayList<String> letters = new ArrayList<>();
            
            for (int i = 0; i < file.getName().length(); i += 11)
            {
                if ((i + 11) > file.getName().length())
                {
                    letters.add(file.getName().substring(i));
                }
                
                else
                {
                    letters.add(file.getName().substring(i, i + 11));
                }
            }
            
            float temp = 35;
            float y = 5;
            
            for (int i = 1; i < letters.size(); i++)
            {
                temp -= 7;
            }
            
            for (String string : letters)
            {
                if (string.contains(".") && string.equals(letters.get(letters.size() - 1)))
                {
                    string = string.substring(0, string.lastIndexOf('.'));
                }
                
                this.drawStringAlpha(string, ((width - 120) + xOffset) - (int) (this.getStringWidth(string, 12) / 2), (height - 100) + yOffset + temp + y, 12, -1, (int) (this.index * 255));
                y += 18;
            }
            
            xOffset += 115;
        }
        
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        
        if (this.maxScrollIndex == -1)
        {
            int size = (this.musicFolder.listFiles().length - 1) / 3;
            
            if (size < 2)
            {
                this.maxScrollIndex = 0;
            }
            
            else
            {
                this.maxScrollIndex = -(115 * (size - 1));
            }
        }
        
        height -= this.scrollIndex;
        width -= 5;
        
        if (!aBoolean)
        {
            this.drawStringAlpha("No music found! Please add music.", width - (this.getStringWidth("No music found! Please add music.", 12) / 2), height - 6, 12, new Color(150, 150, 150).getRGB(), (int) (this.index * 255));
        }
        
        if (this.currentSong != null)
        {
            this.drawRect(width + 200, height - 100, width + 230, height + 100, backgroundColourDarkened.getRGB());
            this.drawRect(width + 205, height + 75, width + 225, height + 95, this.isInsideBox(mouseX, mouseY, width + 205, height + 75, width + 225, height + 95) ? backgroundColour.getRGB() : backgroundColourLightened.getRGB());
            GlStateManager.enableBlend();
            GlStateManager.color(1, 1, 1, this.index);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/server_selection.png"));
            this.drawModalRectWithCustomSizedTexture(width + 199, height + 73, 0, this.isInsideBox(mouseX, mouseY, width + 205, height + 75, width + 225, height + 95) ? 24 : 0, 24, 24, 191, 191);
            this.drawRect(width + 213, height - 90, width + 217, height + 65, backgroundColourLightened.getRGB());
            float temp = 1 - (this.volume / 100);
            this.drawRect(width + 213, height - (90 - (155 * temp)), width + 217, height + 65, backgroundColourLightened.brighter().brighter().getRGB());
            this.drawRect(width + 211, height - (90 - (155 * temp)) + 4, width + 219, height - (100 - (155 * temp)) + 5, new Color(MusicPlayerGUI.backgroundColourLightened, true).brighter().getRGB());
            
            if (this.dragging)
            {
                temp = 1 - ((mouseY - (height - 90)) / 154);
                
                if (temp > 1)
                {
                    temp = 1;
                }
                
                if (temp < 0)
                {
                    temp = 0;
                }
                
                this.volume = temp * 100;
                
                if (MusicUtil.isPlaying())
                {
                    this.previousPlaying = true;
                    MusicUtil.pause();
                }
            }
        }
        
        GlStateManager.popMatrix();
    }
    
    @Override
    public void handleInput() throws IOException
    {
        super.handleInput();
        int i = Integer.compare(0, Mouse.getDWheel());
        
        if (i != 0)
        {
            if (i < 0)
            {
                this.scrollDelta = -5;
            }
            
            else
            {
                this.scrollDelta = 5;
            }
        }
    }
    
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float width = this.width / 2;
        float height = this.height / 2;
        
        if (this.isInsideBox(mouseX, mouseY, width + 85, height - 130, width + 190, height - 110))
        {
            SoundUtil.playClickSound();
            Desktop.getDesktop().open(this.musicFolder);
        }
        
        float xOffset = 0, yOffset = 0;
        height += this.scrollIndex;
        width += 5;
        
        for (File file : this.musicFolder.listFiles())
        {
            if (xOffset != 0 && xOffset % 3 == 0)
            {
                yOffset += 115;
                xOffset = 0;
            }
            
            if (this.isInsideBox(mouseX, mouseY, (width - 170) + xOffset, (height - 100) + yOffset, (width - 170) + xOffset + 100, (height - 80) + yOffset + 80) && mouseButton == 0)
            {
                SoundUtil.playClickSound();
                this.mc.getSoundHandler().stopSounds();
                this.currentSong = file;
                MusicUtil.playMusic(file, this.volume);
            }
            
            xOffset += 115;
        }

        height -= this.scrollIndex;
        width -= 5;
        
        if (this.currentSong != null)
        {
            if (this.isInsideBox(mouseX, mouseY, width + 205, height + 75, width + 225, height + 95))
            {
                if (!MusicUtil.isPlaying())
                {
                    this.mc.getSoundHandler().stopSounds();
                    MusicUtil.playMusic(this.currentSong, this.volume);
                }
                
                else
                {
                    MusicUtil.pause();
                }
            }
        }
        
        if (this.isInsideBox(mouseX, mouseY, width + 210, height - 95, width + 220, height + 70) && mouseButton == 0)
        {
            this.dragging = true;
        }
    }
    
    /**
     * Called when a mouse button is released. Args : mouseX, mouseY, releasedButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
        
        if (this.previousPlaying)
        {
            MusicUtil.playMusic(this.currentSong, this.volume);
            this.previousPlaying = false;
        }
    }
    
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.closingGui = true;
        }
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        super.initGui();
        this.index = 0;
        this.closingGui = false;
        this.dragging = false;
    }
    
    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    public boolean isInsideBox(int mouseX, int mouseY, float x, float y, float width, float height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
