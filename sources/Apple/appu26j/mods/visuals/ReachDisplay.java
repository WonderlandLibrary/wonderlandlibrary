package appu26j.mods.visuals;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;

@ModInterface(name = "Reach Display", description = "Allows you to see your reach.", category = Category.VISUALS, width = 67, height = 15)
public class ReachDisplay extends Mod
{
	public ReachDisplay()
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
		float size = this.getSetting("Size").getSliderValue();
		int[] colors = this.getSetting("Text Color (RGB)").getColors();
		int color = new Color(colors[0], colors[1], colors[2]).getRGB();
		
		if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
		{
			color = ColorUtil.getRainbowColor();
		}
		
		if (this.mc.pointedEntity == null)
		{
			this.width = this.getStringWidth("Reach: None") + 7;
			this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
			this.drawString("Reach: None", this.x + (4 * size), this.y + (4 * size), 8 * size, color);
		}
		
		else
		{
			BigDecimal bigDecimal = new BigDecimal(this.mc.thePlayer.getDistanceToEntity(this.mc.pointedEntity));
			bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
			double reach = bigDecimal.doubleValue();
			this.width = this.getStringWidth("Reach: " + reach) + 7;
			this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
			this.drawString("Reach: " + reach, this.x + (4 * size), this.y + (4 * size), 8 * size, color);
		}
	}
}
