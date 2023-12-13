package vestige.impl.module.ghost;

import java.util.concurrent.ThreadLocalRandom;

import vestige.api.event.Listener;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.NumberSetting;
import vestige.util.misc.TimerUtil;

@ModuleInfo(name = "Autoclicker", category = Category.GHOST)
public class Autoclicker extends Module {
	
	private final NumberSetting minCps = new NumberSetting("Min CPS", this, 10, 1, 20, 1, false) {
		@Override
		public void setCurrentValue(double value) {
	        //max = maxCps.getCurrentValue();
	        super.setCurrentValue(value);
	    }
	};
	private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 10, 1, 20, 1, false) {
		@Override
		public void setCurrentValue(double value) {
	        //min = minCps.getCurrentValue();
	        super.setCurrentValue(value);
	    }
	};
	
	private final TimerUtil timer = new TimerUtil();
	
	public Autoclicker() {
		this.registerSettings(minCps, maxCps);
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		double minCpsValue = Math.min(minCps.getCurrentValue(), maxCps.getCurrentValue());
		double maxCpsValue = Math.max(minCps.getCurrentValue(), maxCps.getCurrentValue());
		
		long delay;
		
		if(minCpsValue == maxCpsValue) {
			delay = (long) (1200 / maxCpsValue);
		} else {
			delay = (long) (1200 / ThreadLocalRandom.current().nextDouble(minCpsValue, maxCpsValue));
		}
		
		if(timer.getTimeElapsed() >= delay && mc.thePlayer.ticksExisted % 13 != 0 && mc.thePlayer.ticksExisted % 19 != 0) {
			if(!mc.thePlayer.isUsingItem() && !mc.gameSettings.keyBindUseItem.isKeyDown() && mc.gameSettings.keyBindAttack.isKeyDown()) {
				mc.leftClickCounter = 0;
				mc.clickMouse();
				timer.reset();
			}
		}
	}
	
}