package appu26j.gui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.google.common.io.Files;

import appu26j.Apple;
import appu26j.utils.BlurUtil;
import appu26j.utils.SoundUtil;
import appu26j.utils.TimeUtil;
import net.minecraft.client.gui.GuiScreen;
import quexii.RoundedUtil;

public class ProfilesGUI extends GuiScreen
{
    private TimeUtil timeUtil = new TimeUtil();
    private boolean flag = false;
    private String file = null;
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        if (this.file != null)
        {
            if (this.timeUtil.hasTimePassed(500))
            {
                this.flag = !this.flag;
                this.timeUtil.reset();
            }
        }
        
        if (BlurUtil.enabled())
        {
            BlurUtil.blur(BlurUtil.intensity());
        }
        
        float i = this.width / 2;
        float j = this.height / 2;
        Color temp1 = new Color(this.backgroundColourDarkened, true);
        Color temp2 = new Color(this.backgroundColourLightened, true);
        Color temp3 = new Color(this.backgroundColour, true);
        Color backgroundColourDarkened = new Color(temp1.getRed(), temp1.getGreen(), temp1.getBlue(), 200);
        Color backgroundColourLightened = new Color(temp2.getRed(), temp2.getGreen(), temp2.getBlue(), 200);
        Color backgroundColour = new Color(temp3.getRed(), temp3.getGreen(), temp3.getBlue(), 200);
        RoundedUtil.drawRoundedRect(i - 100, 25, i + 100, this.height - 25, 5, backgroundColourDarkened.getRGB());
        this.drawString("Profiles", i - (this.getStringWidth("Profiles", 16) / 2), 40, 16, -1);
        float yOffset = 0;
        
        for (File file : Apple.DEFAULT_DIRECTORY.listFiles())
        {
            if (file.getName().endsWith(".json"))
            {
                boolean selectedConfig = file.getName().equals(Apple.CLIENT.getConfig().currentConfig.getName());
                String name = selectedConfig && this.file != null ? this.file + (this.flag ? "|" : "") : file.getName().equals("config.json") ? "Default" : file.getName().replace(".json", "");
                this.drawRect(i - (this.getStringWidth(name) / 2) - 5, 65 + yOffset, i + (this.getStringWidth(name) / 2) + 5, 83 + yOffset, selectedConfig ? (this.isInsideBox(mouseX, mouseY, i - (this.getStringWidth(name) / 2) - 5, 65 + yOffset, i + (this.getStringWidth(name) / 2) + 5, 83 + yOffset) ? new Color(0, 150, 50).getRGB() : new Color(0, 200, 75).getRGB()) : (this.isInsideBox(mouseX, mouseY, i - (this.getStringWidth(name) / 2) - 5, 65 + yOffset, i + (this.getStringWidth(name) / 2) + 5, 83 + yOffset) ? backgroundColour.getRGB() : backgroundColourDarkened.getRGB()));
                this.drawString(name, i - (this.getStringWidth(name) / 2), 70 + yOffset, -1);
                yOffset += 23;
            }
        }
        
        this.drawRect(i - (this.getStringWidth("Create new profile") / 2) - 8, this.height - 62, i + (this.getStringWidth("Create new profile") / 2) + 8, this.height - 40, this.isInsideBox(mouseX, mouseY, i - (this.getStringWidth("Create new profile") / 2) - 8, this.height - 62, i + (this.getStringWidth("Create new profile") / 2) + 8, this.height - 40) ? backgroundColour.getRGB() : backgroundColourDarkened.getRGB());
        this.drawString("Create new profile", i - (this.getStringWidth("Create new profile") / 2), this.height - 55, -1);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float i = this.width / 2;
        float j = this.height / 2;
        float yOffset = 0;
        
        if (this.file != null)
        {
            File tempFile = new File(Apple.DEFAULT_DIRECTORY, this.file + ".json");
            
            if (!tempFile.getName().equals(Apple.CLIENT.getConfig().currentConfig.getName()))
            {
                Files.move(Apple.CLIENT.getConfig().currentConfig, tempFile);
                Apple.CLIENT.getConfig().currentConfig = tempFile;
            }
            
            Keyboard.enableRepeatEvents(false);
            this.file = null;
        }
        
        for (File file : Apple.DEFAULT_DIRECTORY.listFiles())
        {
            if (file.getName().endsWith(".json"))
            {
                boolean selectedConfig = file.getName().equals(Apple.CLIENT.getConfig().currentConfig.getName());
                String name = selectedConfig && this.file != null ? this.file + (this.flag ? "|" : "") : file.getName().equals("config.json") ? "Default" : file.getName().replace(".json", "");
                
                if (this.isInsideBox(mouseX, mouseY, i - (this.getStringWidth(name) / 2) - 5, 65 + yOffset, i + (this.getStringWidth(name) / 2) + 5, 83 + yOffset) && mouseButton == 0)
                {
                    SoundUtil.playClickSound();
                    
                    if (!selectedConfig)
                    {
                        Apple.CLIENT.getConfig().currentConfig = file;
                        Apple.CLIENT.getConfig().loadMods();
                    }
                }
                
                yOffset += 23;
            }
        }
        
        if (this.isInsideBox(mouseX, mouseY, i - (this.getStringWidth("Create new profile") / 2) - 8, this.height - 62, i + (this.getStringWidth("Create new profile") / 2) + 8, this.height - 40) && mouseButton == 0)
        {
            SoundUtil.playClickSound();
            int amountOfConfigs = 1;
            
            for (File file : Apple.DEFAULT_DIRECTORY.listFiles())
            {
                if (file.getName().startsWith("config") && file.getName().endsWith(".json"))
                {
                    amountOfConfigs++;
                }
            }
            
            File config = new File(Apple.DEFAULT_DIRECTORY, "config (" + amountOfConfigs + ").json");
            config.createNewFile();
            Apple.CLIENT.getConfig().currentConfig = config;
            Apple.CLIENT.getConfig().saveMods();
            this.file = config.getName().replace(".json", "");
            Keyboard.enableRepeatEvents(true);
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        
        if (this.file != null)
        {
            if (keyCode == 28)
            {
                File tempFile = new File(Apple.DEFAULT_DIRECTORY, this.file + ".json");
                
                if (!tempFile.getName().equals(Apple.CLIENT.getConfig().currentConfig.getName()))
                {
                    Files.move(Apple.CLIENT.getConfig().currentConfig, tempFile);
                    Apple.CLIENT.getConfig().currentConfig = tempFile;
                }
                
                Keyboard.enableRepeatEvents(false);
                this.file = null;
            }
            
            else
            {
                if (keyCode == 14)
                {
                    try
                    {
                        this.file = this.file.substring(0, this.file.length() - 1);
                    }
                    
                    catch (Exception e)
                    {
                        ;
                    }
                }
                
                else
                {
                    this.file += getChar(typedChar);
                }
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    public boolean isInsideBox(int mouseX, int mouseY, float x, float y, float width, float height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
    
    public String getChar(char typedChar)
    {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !@#$%^&*()-_=+[]\\{}|;':\"<>?,./~`";
        String charr = String.valueOf(typedChar);
        
        if (characters.contains(charr))
        {
            return charr;
        }
        
        else
        {
            return "";
        }
    }
}
