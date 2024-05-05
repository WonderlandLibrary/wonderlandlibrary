package Velo.impl.Modules.other;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;

public class SoundModifier extends Module {
	
	public static boolean isEnabled = false;
	
	public static BooleanSetting bloodparticlessound = new BooleanSetting("Blood Particle Sound", false);
	public static BooleanSetting hurtsound = new BooleanSetting("Player Hurt Sound", true);
	public static BooleanSetting deathsound = new BooleanSetting("Player Death Sound", true);
	
	public SoundModifier() {
		super("Sound Modifier", "Sound Modifier", Keyboard.KEY_NONE, Category.OTHER);
		this.loadSettings(bloodparticlessound, hurtsound, deathsound);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
	}
}
