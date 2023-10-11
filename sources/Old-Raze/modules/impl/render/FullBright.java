package markgg.modules.impl.render;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.NumberSetting;

@ModuleInfo(name = "FullBright", category = Module.Category.RENDER)
public class FullBright extends Module {

	public NumberSetting gammaSetting1 = new NumberSetting("Gamma", this, 5, 0, 5, 0.1);

	@EventHandler
	private final Listener<MotionEvent> eventListener = e -> {
		mc.gameSettings.gammaSetting = (float) gammaSetting1.getValue();
	};

	public void onDisable() {
		mc.gameSettings.gammaSetting = 1.0F;
	}
}
