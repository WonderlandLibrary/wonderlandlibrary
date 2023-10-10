package club.spectrum.modules.impl.movement;

import club.spectrum.modules.Module;
import club.spectrum.modules.Category;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module
{

    public Sprint() {
        super("Sprint", "Sprint", Keyboard.KEY_N, Category.MOVEMENT);
    }

    public void onUpdate() {
        if (this.isEnabled()) {
            if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }
}
