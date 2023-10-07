package vestige.impl.module.ghost;

import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.NumberSetting;

@ModuleInfo(name = "Reach", category = Category.GHOST)
public class Reach extends Module {
	
	public final NumberSetting reach = new NumberSetting("Reach", this, 3.5, 3, 6, 0.05, false);
	
	public Reach() {
		this.registerSettings(reach);
	}

	@Listener
	public void onMotion(MotionEvent event) {
		setSuffix(String.valueOf(reach.getCurrentValue()));
	}

}
