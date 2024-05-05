package xyz.cucumber.base.module.feat.visuals;

import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;

@ModuleInfo(category = Category.VISUALS, description = "Automatically sets your gamma to max", name = "Full Bright", priority = ArrayPriority.LOW)
public class FullBrightModule extends Mod {

	public void onEnable() {
		mc.gameSettings.gammaSetting = 1000;
	}

	public void onDisable() {
		mc.gameSettings.gammaSetting = 25;
	}

}
