package markgg.modules.impl.player;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;

@ModuleInfo(name = "Respawn", category = Module.Category.PLAYER)
public class Respawn extends Module {

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if(mc.thePlayer.isDead)
				mc.thePlayer.respawnPlayer();
		}
	};
}
