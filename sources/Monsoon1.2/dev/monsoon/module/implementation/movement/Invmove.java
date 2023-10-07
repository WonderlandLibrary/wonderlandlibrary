package dev.monsoon.module.implementation.movement;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import dev.monsoon.module.enums.Category;

public class Invmove extends Module {
	public Invmove() {
		super("Invmove", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	

	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(mc.currentScreen instanceof GuiScreen) {
				if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !(this.mc.currentScreen instanceof GuiChat)) mc.thePlayer.rotationYaw += 8F;
				if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !(this.mc.currentScreen instanceof GuiChat)) mc.thePlayer.rotationYaw -= 8F;
				if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !(this.mc.currentScreen instanceof GuiChat)) mc.thePlayer.rotationPitch -= 8F;
				if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !(this.mc.currentScreen instanceof GuiChat)) mc.thePlayer.rotationPitch += 8F;
			}
			block3 : {
	            KeyBinding[] moveKeys;
	            block2 : {
	                moveKeys = new KeyBinding[]{this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindJump, this.mc.gameSettings.keyBindSprint};
	                if (!(this.mc.currentScreen instanceof GuiScreen) || (this.mc.currentScreen instanceof GuiChat)) break block2;
	                for (KeyBinding key : moveKeys) {
	                    key.pressed = Keyboard.isKeyDown((int)key.getKeyCode());
	                }
	                break block3;
	            }
	            //if (!Objects.isNull(this.mc.currentScreen)) break block3;
	            for (KeyBinding bind : moveKeys) {
	                if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
	                KeyBinding.setKeyBindState(bind.getKeyCode(), false);
	            }
	        }
		}
	}
	
}
