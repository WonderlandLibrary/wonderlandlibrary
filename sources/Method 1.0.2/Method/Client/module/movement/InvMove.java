
package Method.Client.module.movement;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.proxy.Overrides.MoveOverride;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InvMove extends Module {
    private static boolean Toggled;

    /////////////////////

    public InvMove() {
        super("Inv Move", Keyboard.KEY_NONE, Category.MOVEMENT, "Inventory Move");
    }

    // Yea there is a Bytecode inject for this but honestly this is easier to read and understand.
    // see MoveOverride in Method.Client.utils.proxy.Overrides
    public static boolean runthething() {
        if (InvMove.Toggled) {
            mc.player.movementInput.moveStrafe = 0.0F;
            mc.player.movementInput.moveForward = 0.0F;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                mc.player.rotationYaw = mc.player.rotationYaw - 5;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                mc.player.rotationYaw = mc.player.rotationYaw + 5;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                mc.player.rotationPitch = Math.max(mc.player.rotationPitch - 5, -90);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                mc.player.rotationPitch = Math.min(mc.player.rotationPitch + 5, 90);
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                ++mc.player.movementInput.moveForward;
                mc.player.movementInput.forwardKeyDown = true;
            } else {
                mc.player.movementInput.forwardKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                --mc.player.movementInput.moveForward;
                mc.player.movementInput.backKeyDown = true;
            } else {
                mc.player.movementInput.backKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                ++mc.player.movementInput.moveStrafe;
                mc.player.movementInput.leftKeyDown = true;
            } else {
                mc.player.movementInput.leftKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                --mc.player.movementInput.moveStrafe;
                mc.player.movementInput.rightKeyDown = true;
            } else {
                mc.player.movementInput.rightKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
            mc.player.movementInput.jump = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());

            if (Mouse.isButtonDown(2)) {
                Mouse.setGrabbed(true);
                mc.inGameHasFocus = true;
            } else if (!(mc.currentScreen == null)) {
                Mouse.setGrabbed(false);
                mc.inGameHasFocus = false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        Toggled = true;
        MoveOverride.toggle();
    }

    @Override
    public void onDisable() {
        Toggled = false;
    }

}


