package me.robbanrobbin.jigsaw.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.entity.player.EntityPlayer;

public class MAC_Bypass extends Module {

	public MAC_Bypass() {
		super("AntiBot(Gwen)", Keyboard.KEY_NONE, Category.TARGET, "Tries to not hit the fake player on Mineplex.");
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);
		
		
	}

}
