package net.minecraft.util;

import vip.radium.module.ModuleManager;
import vip.radium.module.impl.player.InventoryMove;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (ModuleManager.getInstance(InventoryMove.class).isEnabled()) {
            if (!(Wrapper.getCurrentScreen() instanceof GuiChat)) {
                if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
                    ++this.moveForward;
                }

                if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
                    --this.moveForward;
                }

                if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
                    ++this.moveStrafe;
                }

                if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
                    --this.moveStrafe;
                }

                this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
            } else {
                this.jump = this.gameSettings.keyBindJump.isKeyDown();
            }
        } else {
            if (this.gameSettings.keyBindForward.isKeyDown()) {
                ++this.moveForward;
            }

            if (this.gameSettings.keyBindBack.isKeyDown()) {
                --this.moveForward;
            }

            if (this.gameSettings.keyBindLeft.isKeyDown()) {
                ++this.moveStrafe;
            }

            if (this.gameSettings.keyBindRight.isKeyDown()) {
                --this.moveStrafe;
            }

            this.jump = this.gameSettings.keyBindJump.isKeyDown();
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        }

        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();

        if (this.sneak) {
            this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
            this.moveForward = (float) ((double) this.moveForward * 0.3D);
        }
    }
}
