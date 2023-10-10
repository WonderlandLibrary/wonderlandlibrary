package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;

@ModInterface(name = "Full Bright", description = "Allows you to see everything brightly.", category = Category.VISUALS)
public class FullBright extends Mod
{
	private float previousBrightness;
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		this.previousBrightness = this.mc.gameSettings.gammaSetting;
		this.mc.gameSettings.gammaSetting = 100;
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		this.mc.gameSettings.gammaSetting = this.previousBrightness;
	}
}
