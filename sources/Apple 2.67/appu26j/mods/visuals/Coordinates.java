package appu26j.mods.visuals;

import java.awt.Color;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import appu26j.utils.Mathematics;
import club.marshadow.ColorUtil;

@ModInterface(name = "Coordinates", description = "Allows you to see your position.", category = Category.VISUALS, width = 73, height = 15)
public class Coordinates extends Mod
{
    public Coordinates()
    {
        this.addSetting(new Setting("Background", this, false));
        this.addSetting(new Setting("Background Transparency", this, false));
        this.addSetting(new Setting("Background Rounded Corners", this, false));
        this.addSetting(new Setting("Text Shadow", this, true));
        this.addSetting(new Setting("Text Color (RGB)", this, new int[]{255, 255, 255}));
        this.addSetting(new Setting("Rainbow Text Color", this, false));
        this.addSetting(new Setting("Size", this, 0.5F, 1, 2, 0.25F));
    }
    
    @Override
    public void onRender()
    {
        super.onRender();
        String text = Mathematics.round(this.mc.thePlayer.posX) + ", " + Mathematics.round(this.mc.thePlayer.posY) + ", " + Mathematics.round(this.mc.thePlayer.posZ);
        float size = this.getSetting("Size").getSliderValue();
        this.width = this.getStringWidth(text) + 7;
        int[] colors = this.getSetting("Text Color (RGB)").getColors();
        int color = new Color(colors[0], colors[1], colors[2]).getRGB();
        
        if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
        {
            color = ColorUtil.getRainbowColor();
        }
        
        this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
        this.drawString(text, this.x + (4 * size), this.y + (4 * size), 8 * size, color);
    }
}
