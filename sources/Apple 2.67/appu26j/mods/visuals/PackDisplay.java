package appu26j.mods.visuals;

import java.awt.Color;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;

@ModInterface(name = "Pack Display", description = "Displays the current resource pack.", category = Category.VISUALS, width = 100, height = 15)
public class PackDisplay extends Mod
{
	public PackDisplay()
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
		
		String pack = this.mc.getResourcePackRepository().getRepositoryEntries().isEmpty() ? "Default" : this.mc.getResourcePackRepository().getRepositoryEntries().get(0).getResourcePackName();
		
		if (pack.startsWith("!"))
		{
		    pack = pack.replaceFirst("!", "").trim();
		}
		
		this.width = this.getStringWidth("Pack: " + pack) + 7;
		this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
		this.drawString("Pack: " + pack, this.x + (4 * size), this.y + (4 * size), 8 * size, color);
	}
}
