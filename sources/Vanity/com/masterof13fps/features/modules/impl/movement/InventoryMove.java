package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "InventoryMove", category = Category.MOVEMENT, description = "Lets you walk inside of inventories")
public class InventoryMove extends Module {

    public Setting cameraSpeed = new Setting("Camera Speed", this, 3, 0.1, 5, false);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {
        notify.notification("InventoryMove", "Du kannst dich mit den Pfeiltasten im Inventar bewegen", NotificationType.INFO, 5);
    }

    @Override
    public void onDisable() {

    }


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiEditSign)) {
                KeyBinding[] moveKeys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindSprint,
                        mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                        mc.gameSettings.keyBindJump};
                KeyBinding[] array;
                int length = (array = moveKeys).length;
                for (int i = 0; i < length; i++) {
                    KeyBinding bind = array[i];
                    KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
                }

                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    mc.thePlayer.rotationYaw -= cameraSpeed.getCurrentValue();
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    mc.thePlayer.rotationYaw += cameraSpeed.getCurrentValue();
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    mc.thePlayer.rotationPitch += cameraSpeed.getCurrentValue();
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    mc.thePlayer.rotationPitch -= cameraSpeed.getCurrentValue();
                }
            }
        }
    }
}