package vestige.impl.module.ghost;

import java.util.List;

import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.NumberSetting;

@ModuleInfo(name = "Hitbox", category = Category.GHOST)
public class Hitbox extends Module {
	
	public final NumberSetting expand = new NumberSetting("Expand", this, 1, 0.1, 10, 0.1, false);
	public final NumberSetting multiplier = new NumberSetting("Multiplier", this, 1, 0.1, 5, 0.1, false);
	
	public Hitbox() {
		this.registerSettings(expand, multiplier);
	}
	
}
