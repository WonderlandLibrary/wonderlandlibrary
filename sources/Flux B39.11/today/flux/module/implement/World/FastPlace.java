package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;

public class FastPlace extends Module {
	public FastPlace() {
		super("FastPlace", Category.World, false);
	}

	private boolean shit;

	@EventTarget
	public void onTick(TickEvent event) {
		if (this.mc.getRightClickDelayTimer() < 4 || (shit && this.mc.getRightClickDelayTimer() < 5)) {
			this.mc.setRightClickDelayTimer(0);
			shit = !shit;
		}
	}
}
