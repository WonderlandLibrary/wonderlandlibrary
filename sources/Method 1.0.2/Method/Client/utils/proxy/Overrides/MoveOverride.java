package Method.Client.utils.proxy.Overrides;

import Method.Client.module.movement.AutoHold;
import Method.Client.module.movement.InvMove;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;

import static Method.Client.utils.system.Wrapper.mc;

public class MoveOverride extends MovementInputFromOptions {

    public MoveOverride(GameSettings gameSettingsIn) {
        super(gameSettingsIn);
    }

    public static void toggle() {
        if (mc.player == null)
            return;

        mc.player.movementInput = new MoveOverride(mc.gameSettings);
    }

    @Override
    public void updatePlayerMoveState() {
        if (InvMove.runthething() || AutoHold.runthething()) {
            this.jump = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
            this.sneak = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
            if (this.sneak) {
                this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
                this.moveForward = (float) ((double) this.moveForward * 0.3D);
            }
        } else super.updatePlayerMoveState();

    }
}