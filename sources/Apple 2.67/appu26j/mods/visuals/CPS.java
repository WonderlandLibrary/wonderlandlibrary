package appu26j.mods.visuals;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;

@ModInterface(name = "CPS Display", description = "Displays your CPS on the screen.", category = Category.VISUALS, width = 53, height = 15)
public class CPS extends Mod
{
	private ArrayList<Long> leftClicks = new ArrayList<>(), rightClicks = new ArrayList<>();
	private boolean aBoolean1 = false, aBoolean2 = false;
	
	public CPS()
	{
        this.addSetting(new Setting("Disable CPS Text", this, false));
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
		String cps = this.getSetting("Disable CPS Text").getCheckBoxValue() ? "" : "CPS: ";
		float size = this.getSetting("Size").getSliderValue();
		this.width = this.getStringWidth(cps + this.getLeftCPS() + " : " + this.getRightCPS()) + 7;
		int[] colors = this.getSetting("Text Color (RGB)").getColors();
		int color = new Color(colors[0], colors[1], colors[2]).getRGB();
		
		if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
		{
			color = ColorUtil.getRainbowColor();
		}
		
		if (this.mc.currentScreen == null)
		{
			if (Mouse.isButtonDown(0))
			{
				if (!this.aBoolean1)
				{
					this.leftClicks.add(System.nanoTime() / 1000000);
					this.aBoolean1 = true;
				}
			}
			
			else
			{
				if (this.aBoolean1)
				{
					this.aBoolean1 = false;
				}
			}
			
			if (Mouse.isButtonDown(1))
			{
				if (!this.aBoolean2)
				{
					this.rightClicks.add(System.nanoTime() / 1000000);
					this.aBoolean2 = true;
				}
			}
			
			else
			{
				if (this.aBoolean2)
				{
					this.aBoolean2 = false;
				}
			}
		}
		
		this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
		this.drawString(cps + this.getLeftCPS() + " : " + this.getRightCPS(), this.x + (4 * size), this.y + (4 * size), 8 * size, color);
	}
	
	public int getLeftCPS()
	{
		long time = System.nanoTime() / 1000000;
		this.leftClicks.removeIf(aLong -> aLong + 1000 < time);
		return this.leftClicks.size();
	}
	
	public int getRightCPS()
	{
		long time = System.nanoTime() / 1000000;
		this.rightClicks.removeIf(aLong -> aLong + 1000 < time);
		return this.rightClicks.size();
	}
}
