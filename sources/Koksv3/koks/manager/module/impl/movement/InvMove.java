package koks.manager.module.impl.movement;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventPacket;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

/**
 * @author kroko
 * @created on 26.09.2020 : 03:01
 */

@ModuleInfo(name = "InvMove", description = "You can walk in the inventory", category = Module.Category.MOVEMENT)
public class InvMove extends Module {

    public Setting jump = new Setting("Jump", true, this);
    public Setting sprint = new Setting("Sprint", true, this);
    public Setting look = new Setting("Look", true, this);

    public Setting cancelPacket = new Setting("CancelPacket", false, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventPacket) {
            if(((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                if(((EventPacket) event).getPacket() instanceof S2EPacketCloseWindow && cancelPacket.isToggled()) {
                    event.setCanceled(true);
                }
            }
        }
        if (event instanceof EventUpdate) {
            if(mc.currentScreen instanceof GuiChat)return;
            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
            mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
            mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
            mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
            if (jump.isToggled())
                mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
            if (sprint.isToggled())
                mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindPlayerList.getKeyCode()) && look.isToggled()) {
                if (mc.currentScreen != null) {
                    if (timeHelper.hasReached(200)) {
                        mc.inGameHasFocus = !mc.inGameHasFocus;
                        if (mc.inGameHasFocus) {
                            mc.mouseHelper.grabMouseCursor();
                        } else {
                            KeyBinding.unPressAllKeys();
                            mc.mouseHelper.ungrabMouseCursor();
                        }
                        timeHelper.reset();
                    }

                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
