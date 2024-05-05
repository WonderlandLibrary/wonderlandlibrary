package net.minecraft.util;

import club.async.Async;
import club.async.clickgui.dropdown.ClickGUI;
import club.async.interfaces.MinecraftInterface;
import club.async.module.impl.movement.InvMove;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

public class MovementInputFromOptions extends MovementInput implements MinecraftInterface {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
        moveStrafe = 0.0F;
        moveForward = 0.0F;
        if (Async.INSTANCE.getModuleManager().moduleBy(InvMove.class).isEnabled() && !(mc.currentScreen instanceof GuiChat) && ((mc.currentScreen instanceof ClickGUI || mc.currentScreen instanceof club.async.clickgui.flat.ClickGUI) || !Async.INSTANCE.getModuleManager().moduleBy(InvMove.class).clickGuiOnly.get())) {
            if (Keyboard.isKeyDown(gameSettings.keyBindForward.getKeyCode())) ++moveForward;
            if (Keyboard.isKeyDown(gameSettings.keyBindBack.getKeyCode())) --moveForward;
            if (Keyboard.isKeyDown(gameSettings.keyBindLeft.getKeyCode())) ++moveStrafe;
            if (Keyboard.isKeyDown(gameSettings.keyBindRight.getKeyCode())) --moveStrafe;
            jump = Keyboard.isKeyDown(gameSettings.keyBindJump.getKeyCode());
        } else {
            if (gameSettings.keyBindForward.isKeyDown()) ++moveForward;
            if (gameSettings.keyBindBack.isKeyDown()) --moveForward;
            if (gameSettings.keyBindLeft.isKeyDown()) ++moveStrafe;
            if (gameSettings.keyBindRight.isKeyDown()) --moveStrafe;
            jump = gameSettings.keyBindJump.isKeyDown();
        }
        sneak = gameSettings.keyBindSneak.isKeyDown();
        if (sneak) {
            moveStrafe = (float) ((double) moveStrafe * 0.3D);
            moveForward = (float) ((double) moveForward * 0.3D);
        }

    }
}
