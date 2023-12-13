package net.minecraft.util;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.cheat.cheats.player.InvMove;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
        if ((CheatManager.getInstance(InvMove.class).isToggled()) && (!
                (Minecraft.getMinecraft().currentScreen instanceof GuiChat))) {
            moveStrafe = 0.0f;
            moveForward = 0.0f;
            if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
                ++moveForward;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
                --moveForward;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
                ++moveStrafe;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
                --moveStrafe;
            }
            jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
            sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
            if (sneak) {
                moveStrafe *= 0.3;
                moveForward *= 0.3;
            }
        } else {
            moveStrafe = 0.0f;
            moveForward = 0.0f;
            if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
                ++moveForward;
            }
            if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
                --moveForward;
            }
            if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
                ++moveStrafe;
            }
            if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
                --moveStrafe;
            }
            jump = this.gameSettings.keyBindJump.getIsKeyPressed();
            sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
            if (sneak) {
                moveStrafe *= 0.3;
                moveForward *= 0.3;
            }
        }
    }
}

