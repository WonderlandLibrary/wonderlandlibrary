// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.movement.Speed;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;
    
    public MovementInputFromOptions(final GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }
    
    @Override
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
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
        this.jump = (this.gameSettings.keyBindJump.isKeyDown() && !Tenacity.INSTANCE.getModuleCollection().getModule(Speed.class).shouldPreventJumping());
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        if (this.sneak) {
            this.moveStrafe *= (float)0.3;
            this.moveForward *= (float)0.3;
        }
    }
}
