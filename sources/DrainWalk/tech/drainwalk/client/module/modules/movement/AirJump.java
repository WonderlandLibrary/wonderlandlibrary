package tech.drainwalk.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.utility.Helper;

public class AirJump extends Module {
    public AirJump(){
        super("AirJump", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if ((!Helper.mc.world.getCollisionBoxes(Helper.mc.player, Helper.mc.player.getEntityBoundingBox().offset(0, -1, 0).expand(0.5, 0, 0.5)).isEmpty() && Helper.mc.player.ticksExisted % 2 == 0)) {
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                Helper.mc.player.jumpTicks = 0;
                Helper.mc.player.fallDistance = 0;
                updateEvent.setOnGround(true);
                Helper.mc.player.onGround = true;
            }
        }
    }
}
