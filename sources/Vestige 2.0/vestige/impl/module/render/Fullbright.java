package vestige.impl.module.render;

import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;

@ModuleInfo(name = "Fullbright", category = Category.RENDER)
public class Fullbright extends Module {
	
	public void onEnable() {
		mc.gameSettings.gammaSetting = 100;
	}
	
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1;
	}
	
}
