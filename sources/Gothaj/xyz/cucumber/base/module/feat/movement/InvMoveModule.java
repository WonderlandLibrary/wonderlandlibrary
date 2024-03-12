package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to move while inv is opened", name = "Inv Move", key = Keyboard.KEY_NONE)
public class InvMoveModule extends Mod {

	KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
			mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump };

	public void onDisable() {
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null) {
			mc.gameSettings.keyBindForward.pressed = false;
		}

		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null) {
			mc.gameSettings.keyBindBack.pressed = false;
		}

		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null) {
			mc.gameSettings.keyBindRight.pressed = false;
		}

		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null) {
			mc.gameSettings.keyBindLeft.pressed = false;
		}

		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null) {
			mc.gameSettings.keyBindJump.pressed = false;
		}

		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null) {
			mc.gameSettings.keyBindSprint.pressed = false;
		}
	}

	@EventListener
	public void onMotion(EventMotion e) {
		if (!(mc.currentScreen instanceof GuiChat)) {
			for (KeyBinding bind : moveKeys) {
				KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
			}
		}
	}
}
