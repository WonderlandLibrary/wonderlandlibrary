package vestige.impl.module.combat;

import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.NumberSetting;

@ModuleInfo(name = "TargetStrafe", category = Category.COMBAT)
public class TargetStrafe extends Module {
	
	public final NumberSetting distance = new NumberSetting("Distance", this, 3, 1, 6, 0.1, false);
	
	public boolean direction;
	
	public TargetStrafe() {
		this.registerSettings(distance);
	}
	
}
