package markgg.modules.impl.render;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import net.minecraft.potion.Potion;

@ModuleInfo(name = "NoBlind", category = Module.Category.RENDER)
public class NoBlind extends Module {


	@EventHandler
	private final Listener<MotionEvent> eventListener = e -> {
		if (mc.thePlayer.getActivePotionEffect(Potion.blindness) != null) {
			mc.thePlayer.removePotionEffect(Potion.blindness.getId());
		}
		if (mc.thePlayer.getActivePotionEffect(Potion.confusion) != null) {
			mc.thePlayer.removePotionEffect(Potion.confusion.getId());
		}
	};
}