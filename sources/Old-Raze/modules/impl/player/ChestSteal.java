	package markgg.modules.impl.player;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import markgg.util.timer.Timer;
import net.minecraft.inventory.ContainerChest;

@ModuleInfo(name = "ChestSteal", category = Module.Category.PLAYER)
public class ChestSteal extends Module{	

	public Timer timer = new Timer();

	public BooleanSetting autoClose = new BooleanSetting("Auto Close", this, true);
	public NumberSetting delay = new NumberSetting("Delay", this, 1, 1, 10, 0.1);
	private int ticksInChest;


	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
				ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
				ticksInChest++;
				for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
					if (container.getLowerChestInventory().getStackInSlot(i) != null) {
						if (timer.hasTimeElapsed((long) (delay.getValue() * 10), true)) {
							mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
							timer.reset();
						}
					}
				}
			}
			if(autoClose.getValue() && ticksInChest > 10) {
				if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
					ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
					for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
						if (container.getLowerChestInventory().getStackInSlot(i) == null) {
							if (timer.hasTimeElapsed((long) (delay.getValue() * 10), true)) {
								mc.thePlayer.closeScreen();
								ticksInChest = 0;
							}
						}
					}
				}
			}
		}
	};

	public void onEnable() {
		ticksInChest = 0;
	}
}
