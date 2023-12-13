package vestige.impl.module.movement;

import org.lwjgl.input.Keyboard;

import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;

@ModuleInfo(name = "Sprint", category = Category.MOVEMENT)
public class Sprint extends Module {
	
	public Sprint() {
		this.setEnabledSilently(true);
	}
	
    @Listener
    public void onUpdate(UpdateEvent e) {
        if(Vestige.getInstance().getModuleManager().getModule(Noslow.class).isEnabled()) {
        	if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !mc.thePlayer.isSneaking()) {
                mc.thePlayer.setSprinting(true);
            }
        } else {
        	if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !mc.thePlayer.isSneaking()) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }

}
