package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.ChatUtil;
import Velo.impl.Event.EventUpdate;
import net.minecraft.client.audio.SoundCategory;

public class NoHurtCam extends Module {
	
	public static boolean isEnabled = false;
	
	public NoHurtCam() {
		super("NoHurtCam", "NoHurtCam", Keyboard.KEY_NONE, Category.VISUALS);
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
