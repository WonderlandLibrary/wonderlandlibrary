package net.minecraft.util;

import net.minecraft.client.settings.*;
import com.initial.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import org.lwjgl.input.*;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;
    
    public MovementInputFromOptions(final GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }
    
    @Override
    public void updatePlayerMoveState() {
        if (Astomero.instance.moduleManager.getModuleByName("InvMove").isToggled() && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
            this.moveStrafe = 0.0f;
            this.moveForward = 0.0f;
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
            this.jump = this.gameSettings.keyBindJump.isKeyDown();
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
            if (this.sneak) {
                this.moveStrafe *= (float)0.3;
                this.moveForward *= (float)0.3;
            }
        }
        else {
            this.moveStrafe *= 0.0;
            this.moveForward *= 0.0;
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
            if (this.sneak) {
                this.moveStrafe *= (float)0.3;
                this.moveForward *= (float)0.3;
            }
        }
    }
}
