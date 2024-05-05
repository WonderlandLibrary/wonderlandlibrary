package net.minecraft.util;

import client.Client;
import client.event.impl.input.MoveInputEvent;
import net.minecraft.client.settings.GameSettings;

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

        if (this.gameSettings.keyBindForward.isKeyDown())
        {
            ++this.moveForward;
        }

        if (this.gameSettings.keyBindBack.isKeyDown())
        {
            --this.moveForward;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown())
        {
            ++this.moveStrafe;
        }

        if (this.gameSettings.keyBindRight.isKeyDown())
        {
            --this.moveStrafe;
        }

        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        final MoveInputEvent event = new MoveInputEvent(moveForward, moveStrafe, jump, sneak, 0.3, 0.3);
        Client.INSTANCE.getEventBus().handle(event);
        moveForward = event.getForward();
        moveStrafe = event.getStrafe();
        jump = event.isJumping();
        sneak = event.isSneaking();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * event.getStrafeSneakMultiplier());
            this.moveForward = (float)((double)this.moveForward * event.getForwardSneakMultiplier());
        }
    }
}
