package artemis.mods.Movement;

import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;

public class LongJump extends module {
    public LongJump() {
        super("LongJump", "Allows you to jump long", Keyboard.KEY_L, 1111111, Category.MOVEMENT);
    }
    
    public void onUpdate() {
        if (isToggled()) {
        if((mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindLeft.isPressed() || mc.gameSettings.keyBindRight.isPressed() || mc.gameSettings.keyBindBack.isPressed()) && mc.gameSettings.keyBindJump.isPressed()) {
            float dir = mc.thePlayer.rotationYaw + ((mc.thePlayer.moveForward < 0) ? 180 : 0) + ((mc.thePlayer.moveStrafing > 0) ? (-90F * ((mc.thePlayer.moveForward < 0) ? -.5F : ((mc.thePlayer.moveForward > 0) ? .4F : 1F))) : 0);
            float xDir = (float)Math.cos((dir + 90F) * Math.PI / 180);
            float zDir = (float)Math.sin((dir + 90F) * Math.PI / 180);
            if(mc.thePlayer.isCollidedVertically && (mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindLeft.isPressed() || mc.gameSettings.keyBindRight.isPressed() || mc.gameSettings.keyBindBack.isPressed()) && mc.gameSettings.keyBindJump.isPressed()) {
                mc.thePlayer.motionX = xDir * .29F;
                mc.thePlayer.motionZ = zDir * .29F;
            }
            if(mc.thePlayer.motionY == .33319999363422365 && (mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindLeft.isPressed() || mc.gameSettings.keyBindRight.isPressed() || mc.gameSettings.keyBindBack.isPressed())) {
                if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    mc.thePlayer.motionX = xDir * 1.34;
                    mc.thePlayer.motionZ = zDir * 1.34;
                } else {
                    mc.thePlayer.motionX = xDir * 1.261;
                    mc.thePlayer.motionZ = zDir * 1.261;
                }
            }
        }
    }
  }
}
