package vestige.impl.module.player;

import vestige.api.event.Listener;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;

@ModuleInfo(name = "FastPlace", category = Category.PLAYER)
public class FastPlace extends Module {
	
	private final BooleanSetting instant = new BooleanSetting("Instant", this, true);
	
	private boolean shouldPlace;
	
	public FastPlace() {
		this.registerSettings(instant);
	}
	
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		if(instant.isEnabled()) {
			mc.rightClickDelayTimer = 0;
		} else {
			if(!mc.gameSettings.keyBindUseItem.isKeyDown()) {
				shouldPlace = true;
			}
			
			if(shouldPlace) {
				mc.rightClickDelayTimer = 0;
			}
			
			if(mc.gameSettings.keyBindUseItem.isKeyDown()) {
				shouldPlace = !shouldPlace;
			}
		}
	}
	
}
