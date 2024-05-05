package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class TimeChanger extends Module {
	
	public static NumberSetting time = new NumberSetting("Time", 0, 0, 18000, 1);
	
	public static boolean isEnabled = false;
	
	public TimeChanger() {
		super("Ambiance", "Ambiance", Keyboard.KEY_NONE, Category.VISUALS);
		this.loadSettings(time);
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
		mc.theWorld.setWorldTime((long) time.getValue());
	}
	
	
	
 @Override
public void onEventReceivePacket(EventReceivePacket event) {
	 if (event.getPacket() instanceof S03PacketTimeUpdate) {
		 event.setCancelled(true);
		}
}
	
}
