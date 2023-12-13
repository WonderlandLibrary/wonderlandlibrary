package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class Headless extends Module {

	public Headless() {
		super("Headless", Keyboard.KEY_NONE, Category.FUN, "Your head is gone for other players!");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if (Jigsaw.doDisablePacketSwitch()) {
			return;
		}
		event.pitch = 180f;
		super.onUpdate(event);
	}

}
