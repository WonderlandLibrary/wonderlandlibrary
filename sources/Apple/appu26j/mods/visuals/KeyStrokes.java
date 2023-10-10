package appu26j.mods.visuals;

import java.awt.Color;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;

@ModInterface(name = "Keystrokes", description = "Displays the keystrokes on your screen.", category = Category.VISUALS, width = 80, height = 80)
public class KeyStrokes extends Mod
{
	public KeyStrokes()
	{
        this.addSetting(new Setting("Background", this, true));
		this.addSetting(new Setting("Background Transparency", this, false));
        this.addSetting(new Setting("Background Rounded Corners", this, false));
		this.addSetting(new Setting("Text Color (RGB)", this, new int[]{255, 255, 255}));
		this.addSetting(new Setting("Rainbow Text Color", this, false));
        this.addSetting(new Setting("Size", this, 0.5F, 0.66F, 2, 0.25F));
	}
	
	@Override
	public void onRender()
	{
		super.onRender();
		GlStateManager.pushMatrix();
		float size = this.getSetting("Size").getSliderValue();
		GlStateManager.scale(size, size, size);
		float x = this.x / size;
        float y = this.y / size;
        x = x - x % 1;
        y = y - y % 1;
		int[] colors = this.getSetting("Text Color (RGB)").getColors();
		int color = new Color(colors[0], colors[1], colors[2]).getRGB();
		
		if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
		{
			color = ColorUtil.getRainbowColor();
		}
		
		this.drawRect(x + (this.width / 2) - 13, y, x + (this.width / 2) + 13, y + 26, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindForward) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("W", (x + (this.width / 2)) - (this.getStringWidth("W", 12) / 2), y + 8, 12, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindForward) ? this.getHoveredColor(color) : color);
		this.drawRect(x, y + 27, x + (this.width / 2) - 14, y + 53, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindLeft) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("A", (x + 13) - (this.getStringWidth("A", 12) / 2), y + 35, 12, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindLeft) ? this.getHoveredColor(color) : color);
		this.drawRect(x + (this.width / 2) - 13, y + 27, x + (this.width / 2) + 13, y + 53, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindBack) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("S", (x + (this.width / 2)) - (this.getStringWidth("S", 12) / 2), y + 35, 12, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindBack) ? this.getHoveredColor(color) : color);
		this.drawRect(x + (this.width / 2) + 14, y + 27, x + this.width, y + 53, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindRight) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("D", ((x + this.width) - 13) - (this.getStringWidth("D", 12) / 2), y + 35, 12, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindRight) ? this.getHoveredColor(color) : color);
		this.drawRect(x, y + 54, x + this.width, y + 80, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindJump) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("SPACE", (x + (this.width / 2)) - (this.getStringWidth("SPACE", 12) / 2), y + this.height - 18, 12, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindJump) ? this.getHoveredColor(color) : color);
        GlStateManager.popMatrix();
	}
	
	public int getHoveredColor(int color)
	{
	    if (this.getSetting("Background").getCheckBoxValue())
	    {
	        return color;
	    }
	    
	    Color colorClass = new Color(color, true);
	    int temp = colorClass.getRed() + colorClass.getGreen() + colorClass.getBlue();
	    
	    if (temp < 384)
	    {
	        return colorClass.brighter().brighter().getRGB();
	    }
	    
	    else
	    {
	        return colorClass.darker().darker().getRGB();
	    }
	}
}
