package markgg.modules.impl.render;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import net.minecraft.util.ResourceLocation;

@ModuleInfo(name = "Cape", category = Module.Category.RENDER)
public class Cape extends Module{

	public ModeSetting capeMode = new ModeSetting("Design", this, "Red", "Red", "Xray", "Minecon1", "Minecon2", "Migration", "Optifine", "Wavy", "Waifu1", "Waifu2", "Waifu3", "Waifu4", "Waifu5", "Official", "Pig");

	@EventHandler
	private final Listener<MotionEvent> eventListener = e -> {
		mc.thePlayer.setLocationOfCape(new ResourceLocation("Raze/Capes/" + capeMode.getMode() + ".png"));
	};

	public void onDisable() {
		if(mc.thePlayer == null)
			return;
		mc.thePlayer.setLocationOfCape(null);
	}

}
