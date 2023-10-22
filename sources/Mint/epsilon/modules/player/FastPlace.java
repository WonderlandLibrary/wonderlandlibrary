package epsilon.modules.player;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastPlace extends Module{
	
	public NumberSetting delay = new NumberSetting("FastPlaceDelay", 0, 0, 4, 0.1);
	public BooleanSetting bo = new BooleanSetting("BlocksOnly", true);
	public BooleanSetting randomize = new BooleanSetting("Randomize", true);
	
	public FastPlace(){
		super("FastPlace", Keyboard.KEY_NONE, Category.PLAYER, "Changes the delay of right click for blocks and interactables");
		this.addSettings(delay);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate && e.isPre()) {
			if (bo.isEnabled() && !(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock))
	            return;

	        int delay = (int) this.delay.getValue();

	        if (delay == 0) {
	            if (randomize.isEnabled()) {
	                delay = RandomUtils.nextInt(0, delay);
	            }

	            mc.rightClickDelayTimer = delay;
	        } else {
	            if (mc.rightClickDelayTimer > delay) {
	                if (randomize.isEnabled()) {
	                    delay = RandomUtils.nextInt(0, delay);
	                }

	                mc.rightClickDelayTimer = delay;
	            }
	        }
			
		}
	}
}