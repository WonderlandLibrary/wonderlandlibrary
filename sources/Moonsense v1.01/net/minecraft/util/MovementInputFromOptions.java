// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;
    private static final String __OBFID = "CL_00000937";
    
    public MovementInputFromOptions(final GameSettings p_i1237_1_) {
        this.gameSettings = p_i1237_1_;
    }
    
    @Override
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
            ++this.moveForward;
        }
        if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
            --this.moveForward;
        }
        if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
            ++this.moveStrafe;
        }
        if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
            --this.moveStrafe;
        }
        this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
        if (this.sneak) {
            this.moveStrafe *= (float)0.3;
            this.moveForward *= (float)0.3;
        }
    }
}
