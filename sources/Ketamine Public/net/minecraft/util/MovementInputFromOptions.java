package net.minecraft.util;

import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.module.misc.inventory.Inventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.function.Predicate;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn)
    {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        final Predicate<KeyBinding> isPressed = keyBinding ->
            KetamineClient.getInstance().getModuleManager().getModule(Inventory.class).isEnabled() ?
                Keyboard.isKeyDown(keyBinding.getKeyCode()) && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat) :
                keyBinding.isKeyDown();

        if (isPressed.test(this.gameSettings.keyBindForward))
        {
            ++this.moveForward;
        }

        if (isPressed.test(this.gameSettings.keyBindBack))
        {
            --this.moveForward;
        }

        if (isPressed.test(this.gameSettings.keyBindLeft))
        {
            ++this.moveStrafe;
        }

        if (isPressed.test(this.gameSettings.keyBindRight))
        {
            --this.moveStrafe;
        }

        this.jump = isPressed.test(this.gameSettings.keyBindJump);
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}