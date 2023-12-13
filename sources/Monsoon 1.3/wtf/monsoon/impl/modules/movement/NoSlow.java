package wtf.monsoon.impl.modules.movement;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import wtf.monsoon.api.Wrapper;

public class NoSlow extends Module {
	
	public NoSlow() {
		super("Noslow", "Doesn't slow you down", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onEvent(EventUpdate e) {
		if(Wrapper.mc.currentScreen instanceof GuiScreen) {
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !(this.mc.currentScreen instanceof GuiChat)) Wrapper.mc.thePlayer.rotationYaw += 8F;
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !(this.mc.currentScreen instanceof GuiChat)) Wrapper.mc.thePlayer.rotationYaw -= 8F;
			if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !(this.mc.currentScreen instanceof GuiChat)) Wrapper.mc.thePlayer.rotationPitch -= 8F;
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !(this.mc.currentScreen instanceof GuiChat)) Wrapper.mc.thePlayer.rotationPitch += 8F;
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
