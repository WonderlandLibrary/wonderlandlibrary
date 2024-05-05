package appu26j.mods.visuals;

import java.awt.Color;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import appu26j.utils.ClockUtil;
import club.marshadow.ColorUtil;

@ModInterface(name = "Clock", description = "Allows you to see the time in-game.", category = Category.VISUALS, width = 44, height = 15)
public class TimeClock extends Mod
{
	public TimeClock()
	{
		this.addSetting(new Setting("Background", this, false));
		this.addSetting(new Setting("Background Transparency", this, false));
		this.addSetting(new Setting("Text Shadow", this, true));
		this.addSetting(new Setting("Text Color (RGB)", this, new int[]{255, 255, 255}));
		this.addSetting(new Setting("Rainbow Text Color", this, false));
		this.addSetting(new Setting("Format", this, "12 Hour Format", "12 Hour Format", "24 Hour Format"));
		this.addSetting(new Setting("Size", this, 0.5F, 1, 2, 0.25F));
	}
	
	@Override
	public void onRender()
	{
		super.onRender();
		float size = this.getSetting("Size").getSliderValue();
		int[] colors = this.getSetting("Text Color (RGB)").getColors();
		int color = new Color(colors[0], colors[1], colors[2]).getRGB();
		
		if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
		{
			color = ColorUtil.getRainbowColor();
		}
		
		String time = this.getSetting("Format").getModeValue().equals("12 Hour Format") ? ClockUtil.getTime12HourFormat() : ClockUtil.getTime24HourFormat();
		this.width = this.getStringWidth(time) + 8;
		this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
		this.drawString(time, this.x + (4 * size), this.y + (4 * size), 8 * size, color);
	}
}
