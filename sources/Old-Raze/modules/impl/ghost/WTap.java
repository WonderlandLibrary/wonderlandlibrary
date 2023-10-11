package markgg.modules.impl.ghost;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.NumberSetting;
import net.minecraft.entity.Entity;

@ModuleInfo(name = "WTap", category = Module.Category.GHOST)
public class WTap extends Module {

	public NumberSetting dist = new NumberSetting("Distance", this, 3.5, 1, 6, 0.5);


	@EventHandler
	private final Listener<MotionEvent> motionEventListener = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			for (Object o : mc.theWorld.loadedEntityList) {
				if (!(o instanceof Entity))
					continue;

				Entity entity = (Entity)o;
				if (entity == mc.thePlayer)
					continue;

				if (mc.thePlayer.getDistanceToEntity(entity) > dist.getValue() || !mc.thePlayer.isSprinting())
					continue;

				mc.thePlayer.setSprinting(false);
			}
		}
	};

}
