/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.api.utils.input.Input;
import baritone.utils.InputOverrideHandler;
import net.minecraft.util.MovementInput;

public class PlayerMovementInput
extends MovementInput {
    private final InputOverrideHandler handler;

    PlayerMovementInput(InputOverrideHandler handler) {
        this.handler = handler;
    }

    @Override
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        this.jump = this.handler.isInputForcedDown(Input.JUMP);
        this.forwardKeyDown = this.handler.isInputForcedDown(Input.MOVE_FORWARD);
        if (this.forwardKeyDown) {
            this.moveForward += 1.0f;
        }
        if (this.backKeyDown = this.handler.isInputForcedDown(Input.MOVE_BACK)) {
            this.moveForward -= 1.0f;
        }
        if (this.leftKeyDown = this.handler.isInputForcedDown(Input.MOVE_LEFT)) {
            this.moveStrafe += 1.0f;
        }
        if (this.rightKeyDown = this.handler.isInputForcedDown(Input.MOVE_RIGHT)) {
            this.moveStrafe -= 1.0f;
        }
        if (this.sneak = this.handler.isInputForcedDown(Input.SNEAK)) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3);
            this.moveForward = (float)((double)this.moveForward * 0.3);
        }
    }
}

