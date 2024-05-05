package epsilon.modules.render;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.NumberSetting;
import net.minecraft.entity.player.EntityPlayer;

public class ViewBobbing extends Module{

	public NumberSetting value = new NumberSetting("Bob Amount", 1, 0, 20, 1);
	public BooleanSetting onlymodule = new BooleanSetting("Only fly", true);
	
	public ViewBobbing() {
		super("ViewBobbing", Keyboard.KEY_NONE, Category.RENDER, "Modifies view bobbing rate if view bobbing setting is enabled");
		this.addSettings(value, onlymodule);
	}
	
	public void onEvent(Event e) {
		if(onlymodule.isEnabled()) {
			for(Module m : Epsilon.modules) {
				if(m.name=="Fly" &&!m.toggled) return;
			}
		}
		
		if(e instanceof EventUpdate && mc.gameSettings.viewBobbing) {
	        mc.thePlayer.cameraYaw = (float) value.getValue()/25;
		}
	}

}
