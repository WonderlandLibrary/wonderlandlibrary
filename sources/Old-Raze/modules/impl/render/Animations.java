package markgg.modules.impl.render;

import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;

@ModuleInfo(name = "Animations", category = Module.Category.RENDER)
public class Animations extends Module {

	public ModeSetting animMode = new ModeSetting("Mode", this, "1.8", "1.8", "1.7", "Raze", "Stab","Exhibition", "Sigma", "Dabbing", "Flapper");
	
}
