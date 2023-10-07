package net.minecraft.util;

import cc.novoline.Novoline;
import cc.novoline.modules.move.FastSneak;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
        this.setMoveStrafe(0.0F);
        this.setMoveForward(0.0F);

        if (this.gameSettings.keyBindForward.isKeyDown()) {
            this.setMoveForward(this.getMoveForward() + 1);
        }

        if (this.gameSettings.keyBindBack.isKeyDown()) {
            this.setMoveForward(this.getMoveForward() - 1);
        }

        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            this.setMoveStrafe(this.getMoveStrafe() + 1);
        }

        if (this.gameSettings.keyBindRight.isKeyDown()) {
            this.setMoveStrafe(this.getMoveStrafe() - 1);
        }

        this.setJump(this.gameSettings.keyBindJump.isKeyDown());
        this.setSneak(this.gameSettings.keyBindSneak.isKeyDown());

        if (this.sneak() && !Novoline.getInstance().getModuleManager().getModule(FastSneak.class).isEnabled()) {
            this.setMoveStrafe(this.getMoveStrafe() * 0.3F);
            this.setMoveForward(this.getMoveForward() * 0.3F);
        }
    }
}
