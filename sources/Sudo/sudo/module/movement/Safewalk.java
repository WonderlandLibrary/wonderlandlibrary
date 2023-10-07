package sudo.module.movement;

import sudo.core.event.EventTarget;
import sudo.events.EventClipAtLedge;
import sudo.module.Mod;

public class Safewalk extends Mod {

	public Safewalk() {
		super("Safewalk", "Prevents the player from falling off the side of blocks", Category.MOVEMENT, 0);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

    @EventTarget
    private void onClipAtLedge(EventClipAtLedge event) {
        if (!mc.player.isSneaking()) event.setClip(true);
    }
}
