package appu26j.mods.visuals;

import java.awt.Color;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModInterface(name = "Ping Display", description = "Not to be confused with Ping Indicator.", category = Category.VISUALS, width = 73, height = 15)
public class PingDisplay extends Mod
{
    public PingDisplay()
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
        NetworkPlayerInfo networkPlayerInfo = null;
        
        try
        {
            networkPlayerInfo = this.mc.getNetHandler().getPlayerInfoMap().stream().filter(player -> player.getGameProfile().getId().toString().equals(this.mc.getNetHandler().getGameProfile().getId().toString())).findFirst().orElse(null);
        }
        
        catch (Exception e)
        {
            ;
        }
        
        if (networkPlayerInfo != null)
        {
            String text = "Ping: " + networkPlayerInfo.getResponseTime();
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
}
