package Velo.impl.Modules.movement;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class InventoryMove extends Module {
	
	public InventoryMove() {
		super("Inventory Move", "Inventory Move", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onDisable() {
		if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null) {
            mc.gameSettings.keyBindForward.pressed = false;
        }

        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null) {
            mc.gameSettings.keyBindBack.pressed = false;
        }

        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null) {
            mc.gameSettings.keyBindRight.pressed = false;
        }

        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null) {
            mc.gameSettings.keyBindLeft.pressed = false;
        }

        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null) {
            mc.gameSettings.keyBindJump.pressed = false;
        }

        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null) {
            mc.gameSettings.keyBindSprint.pressed = false;
        }
	}
	
	public void onUpdate(EventUpdate event) {
		if (mc.currentScreen instanceof GuiContainer) {
            mc.gameSettings.keyBindForward.pressed = mc.gameSettings.isKeyDown(mc.gameSettings.keyBindForward);
            mc.gameSettings.keyBindBack.pressed = mc.gameSettings.isKeyDown(mc.gameSettings.keyBindBack);
            mc.gameSettings.keyBindRight.pressed = mc.gameSettings.isKeyDown(mc.gameSettings.keyBindRight);
            mc.gameSettings.keyBindLeft.pressed = mc.gameSettings.isKeyDown(mc.gameSettings.keyBindLeft);
            mc.gameSettings.keyBindJump.pressed = mc.gameSettings.isKeyDown(mc.gameSettings.keyBindJump);
            mc.gameSettings.keyBindSprint.pressed = mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSprint);
   	 	} 
	}
}
