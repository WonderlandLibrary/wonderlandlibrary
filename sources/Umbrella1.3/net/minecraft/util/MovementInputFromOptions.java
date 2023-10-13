/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class MovementInputFromOptions
extends MovementInput {
    private final GameSettings gameSettings;
    private static final String __OBFID = "CL_00000937";

    public MovementInputFromOptions(GameSettings p_i1237_1_) {
        this.gameSettings = p_i1237_1_;
    }

    @Override
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
            this.moveForward += 1.0f;
        }
        if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
            this.moveForward -= 1.0f;
        }
        if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
            this.moveStrafe += 1.0f;
        }
        if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
            this.moveStrafe -= 1.0f;
        }
        this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
        if (this.sneak) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3);
            this.moveForward = (float)((double)this.moveForward * 0.3);
        }
    }
}

