package net.minecraft.util;

import intent.AquaDev.aqua.Aqua;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class MovementInputFromOptions
extends MovementInput {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            this.moveForward += 1.0f;
        }
        if (this.gameSettings.keyBindBack.isKeyDown()) {
            this.moveForward -= 1.0f;
        }
        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            this.moveStrafe += 1.0f;
        }
        if (this.gameSettings.keyBindRight.isKeyDown()) {
            this.moveStrafe -= 1.0f;
        }
        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        if (this.sneak) {
            boolean scaffold;
            boolean bl = scaffold = Aqua.setmgr.getSetting("ScaffoldLegitPlace").isState() && Aqua.moduleManager.getModuleByName("Scaffold").isToggled() && !Minecraft.getMinecraft().gameSettings.keyBindJump.pressed;
            if (scaffold) {
                this.moveStrafe = (float)((double)this.moveStrafe * 0.9);
                this.moveForward = (float)((double)this.moveForward * 0.9);
            } else {
                this.moveStrafe = (float)((double)this.moveStrafe * 0.3);
                this.moveForward = (float)((double)this.moveForward * 0.3);
            }
        }
    }
}
