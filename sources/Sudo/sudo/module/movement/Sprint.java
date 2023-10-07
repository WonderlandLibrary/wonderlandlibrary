package sudo.module.movement;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;

public class Sprint extends Mod {

	BooleanSetting smart = new BooleanSetting("Smart", true);

    public Sprint() {
        super("Sprint", "Automatically sprints for you", Category.MOVEMENT, 0);
        addSetting(smart);
    }

    @Override
    public void onTick() {
    	if (this.isEnabled()) {
    		if (smart.isEnabled()) {
    			if (mc.player.forwardSpeed>0) mc.player.setSprinting(true);
    		}
    		else mc.player.setSprinting(true);
    	}
        super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
	
}
