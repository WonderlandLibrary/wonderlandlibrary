package appu26j.gui.quickplay;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import appu26j.fontrenderer.SizedFontRenderer;
import appu26j.utils.ServerUtil;
import net.minecraft.client.gui.GuiScreen;

public class QuickPlayGUI extends GuiScreen
{
    public ArrayList<CustomButton> originalButtons = new ArrayList<>(), customButtons = new ArrayList<>();
    
    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        if (ServerUtil.isPlayerOnHypixel())
        {
            this.customButtons.forEach(button -> button.drawScreen(mouseX, mouseY, partialTicks));
        }
        
        else
        {
            this.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 128).getRGB());
            SizedFontRenderer.drawString("You are not on Hypixel :(", (this.width / 2) - (this.getStringWidth("You are not on Hypixel :(", 24) / 2), (this.height / 2) - 16, 24, -1);
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
        float f = this.width / 2;
        float g = this.height / 2;
        float temp = 5 * 41.75F;
        f -= temp;
        g -= 12.5F;
        this.originalButtons.clear();
        this.customButtons.clear();
        this.customButtons.add(new CustomButton("BedWars", f, g - 10));
        this.customButtons.add(new CustomButton("SkyWars", f + 85, g - 10));
        this.customButtons.add(new CustomButton("TNT Games", f + 170, g - 10));
        this.customButtons.add(new CustomButton("Murder Mystery", f + 255, g - 10));
        this.customButtons.add(new CustomButton("Duels", f + 340, g - 10));
        this.customButtons.add(new CustomButton("The Pit", f, g + 15));
        this.customButtons.add(new CustomButton("Dropper", f + 85, g + 15));
        this.customButtons.add(new CustomButton("WoolWars", f + 170, g + 15));
        this.customButtons.add(new CustomButton("UHC", f + 255, g + 15));
        this.customButtons.add(new CustomButton("The Bridge", f + 340, g + 15));
        this.originalButtons.addAll(this.customButtons);
    }
    
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        boolean aBoolean = true;
        
        try
        {
            for (CustomButton customButton : this.customButtons)
            {
                if (!customButton.keyTyped(typedChar, keyCode))
                {
                    aBoolean = false;
                }
            }
        }
        
        catch (Exception e)
        {
            aBoolean = false;
        }
        
        if (aBoolean)
        {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        if (ServerUtil.isPlayerOnHypixel())
        {
            try
            {
                this.customButtons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
            }
            
            catch (Exception e)
            {
                ;
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
}
