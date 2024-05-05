package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;
import net.minecraft.entity.Entity;

public class TrueSight extends Module {
	
	public TrueSight() {
		super("TrueSight", "TrueSight", Keyboard.KEY_NONE, Category.VISUALS);
	}
	
	public void onUpdate(EventUpdate event) {
		for(Object entity : mc.theWorld.loadedEntityList) {
			((Entity) entity).setInvisible(false);
		}
	}
}
