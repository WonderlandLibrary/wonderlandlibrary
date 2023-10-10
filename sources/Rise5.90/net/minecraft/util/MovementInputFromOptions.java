package net.minecraft.util;

import dev.rise.event.impl.other.Button;
import dev.rise.event.impl.other.MoveButtonEvent;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
        final MoveButtonEvent event = new MoveButtonEvent(new Button(this.gameSettings.keyBindLeft.isKeyDown(), 90), new Button(this.gameSettings.keyBindRight.isKeyDown(), -90), new Button(this.gameSettings.keyBindBack.isKeyDown(), 180), new Button(this.gameSettings.keyBindForward.isKeyDown(), 0), this.gameSettings.keyBindSneak.isKeyDown(), this.gameSettings.keyBindJump.isKeyDown());
        event.call();
        if (event.isCancelled()) return;

        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (event.getForward().isButton()) {
            ++this.moveForward;
        }

        if (event.getBackward().isButton()) {
            --this.moveForward;
        }

        if (event.getLeft().isButton()) {
            ++this.moveStrafe;
        }

        if (event.getRight().isButton()) {
            --this.moveStrafe;
        }

        this.jump = event.isJump();
        this.sneak = event.isSneak();

        if (this.sneak) {
            this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
            this.moveForward = (float) ((double) this.moveForward * 0.3D);
        }
    }
}
