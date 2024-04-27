package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.ClickWindowEvent;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "InvMove", description = "Allows you to walk while an inventory is opened.", category = ModuleCategory.MOVEMENT)
public class InvMove extends Module {

    private BoolValue moreCarryValue = new BoolValue("MoreCarry",false);
    private BoolValue noDetectableValue = new BoolValue("NoDetectable", false);
    public static BoolValue aacAdditionProValue = new BoolValue("AACAdditionPro", false);
    private BoolValue noMoveClicksValue = new BoolValue("NoMoveClicks", false);
    private BoolValue rotateValue = new BoolValue("Rotate", true);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiIngameMenu) && (!noDetectableValue.get()
                || !(mc.currentScreen instanceof GuiContainer))) {
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindForward);
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindBack);
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindRight);
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindLeft);
            mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
            mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindSprint);
        }

        if (rotateValue.get()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                if (mc.thePlayer.rotationPitch > 0) {
                    mc.thePlayer.rotationPitch -= 5;
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                if (mc.thePlayer.rotationPitch < 90) {
                    mc.thePlayer.rotationPitch += 5;
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                mc.thePlayer.rotationYaw -= 5;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                mc.thePlayer.rotationYaw += 5;
            }
        }
    }

    @EventTarget
    public void onClick(ClickWindowEvent event) {
        if (noMoveClicksValue.get() && MovementUtils.isMoving()) event.cancelEvent();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (moreCarryValue.get()) {
            if (event.getPacket() instanceof C0DPacketCloseWindow) event.cancelEvent();
        }
    }

    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null)
            mc.gameSettings.keyBindForward.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null)
            mc.gameSettings.keyBindBack.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null)
            mc.gameSettings.keyBindRight.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null)
            mc.gameSettings.keyBindLeft.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null)
            mc.gameSettings.keyBindJump.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null)
            mc.gameSettings.keyBindSprint.pressed = false;
    }

    @Override
    public String getTag() {
        if (aacAdditionProValue.get()) {
            return "AACP";
        } else {
            return "Normal";
        }
    }

}
